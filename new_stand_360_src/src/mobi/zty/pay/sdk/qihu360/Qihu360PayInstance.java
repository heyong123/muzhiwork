package mobi.zty.pay.sdk.qihu360;

import org.json.JSONException;
import org.json.JSONObject;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.pay.sdk.ShopInfo;
import mobi.zty.pay.sdk.factory.PaymentFactoy;
import mobi.zty.sdk.game.ExitGameListener;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.GameSDK.QihuCallBake;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.HttpRequestt;
import mobi.zty.sdk.util.QihooUserInfo;
import mobi.zty.sdk.util.QihooUserInfoListener;
import mobi.zty.sdk.util.QihooUserInfoTask;
import mobi.zty.sdk.util.Util_G;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.qihoo.gamecenter.sdk.activity.ContainerActivity;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;
import com.qihoo.gamecenter.sdk.matrix.Matrix;
import com.qihoo.gamecenter.sdk.protocols.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.ProtocolKeys;

public class Qihu360PayInstance extends PaymentInterf {

	private static Qihu360PayInstance instance;
	private Handler callBHandler = null;
	private boolean isInitSCC = false;
	
	private Context  mContext;
	protected QihooUserInfo mQihooUserInfo;

	protected boolean mIsLandscape;

    protected String mAccessToken = null;

    /**
     * AccessToken是否有效
     */
    protected static boolean isAccessTokenValid = true;
    
    protected static boolean isQTValid = true;
    
	private ShopInfo shopInfo = null;
	
	private String payorderid = "";
	public static Qihu360PayInstance getInstance(){
		if(instance==null){
			instance = scyUinconPay();
		}
		return instance;
	}
	private static synchronized Qihu360PayInstance scyUinconPay(){
		if(instance==null){
			instance =  new Qihu360PayInstance();
		}
		return instance;
	}
	
	@Override
	public void init(Context context, Object... parameters) {
		
		Matrix.init((Activity) context);
		String appId = (String) parameters[0];
		callBHandler = (Handler) parameters[1];
		isInitSCC = true;
		shopInfo = PayConfig.getShopInfo(appId,PayConfig.QIHU_PAYTYPE);
		PayResultInfo info = new PayResultInfo();
		if (shopInfo == null) {
			info.resutCode = PayConfig.QIHOO_INIT_FAIL;
			info.retMsg = "gameId不存在-->>" +appId;
			Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_INITRESULT);
			message.obj = info;
			message.sendToTarget();
			Util_G.debug_e("Alppay", info.retMsg);
			return;
		}else{
			info.resutCode = PayConfig.QIHOO_INIT_SUCCESS;
			Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_INITRESULT);
			message.obj = info;
			message.sendToTarget();
			login(context,false);
		}
	}

	/**
     * 生成调用360SDK登录接口
     * @param isLandScape 是否横屏
     * @return intent
     */
	public void login(Context context, boolean isLandScape) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, ContainerActivity.class);

        // 界面相关参数，360SDK界面是否以横屏显示。
        intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // 必需参数，使用360SDK的登录模块。
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_LOGIN);

        //是否显示关闭按钮
        intent.putExtra(ProtocolKeys.IS_LOGIN_SHOW_CLOSE_ICON, true);

        // 可选参数，是否支持离线模式，默认值为false
        intent.putExtra(ProtocolKeys.IS_SUPPORT_OFFLINE, false);

        // 可选参数，是否在自动登录的过程中显示切换账号按钮
        intent.putExtra(ProtocolKeys.IS_SHOW_AUTOLOGIN_SWITCH, false);

        // 可选参数，是否隐藏欢迎界面
        intent.putExtra(ProtocolKeys.IS_HIDE_WELLCOME, false);

        //-- 以下参数仅仅针对自动登录过程的控制
        // 可选参数，自动登录过程中是否不展示任何UI，默认展示。
        intent.putExtra(ProtocolKeys.IS_AUTOLOGIN_NOUI, false);

        // 可选参数，静默自动登录失败后是否显示登录窗口，默认不显示
        intent.putExtra(ProtocolKeys.IS_SHOW_LOGINDLG_ONFAILED_AUTOLOGIN, true);
        // 测试参数，发布时要去掉
        intent.putExtra(ProtocolKeys.IS_SOCIAL_SHARE_DEBUG, true);
//		Intent intent = getLoginIntent(isLandScape);
        IDispatcherCallback callback = mLoginCallback;
       
        Matrix.execute(context, intent, callback);
	}
	
	@Override
	public void pay(Context context, Object... parameters) {
		int mPayIndex = (Integer) parameters[0];//商品id
		String productCode = shopInfo.payCodeMap.get(mPayIndex);
		int moneyAmount = (Integer) parameters[1];//金额
		String payname = (String) parameters[2];//商品名
		String app_name = (String) parameters[3];//应用名
		payorderid = (String) parameters[4];//订单号
		String qihooUserId = (mQihooUserInfo != null) ? mQihooUserInfo.getId() : null;

        Bundle bundle = new Bundle();

        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, false);

        // *** 以下非界面相关参数 ***

        // 设置QihooPay中的参数。

        // 必需参数，360账号id，整数。
        bundle.putString(ProtocolKeys.QIHOO_USER_ID, qihooUserId);

        // 必需参数，所购买商品金额, 以分为单位。金额大于等于100分，360SDK运行定额支付流程； 金额数为0，360SDK运行不定额支付流程。
        bundle.putString(ProtocolKeys.AMOUNT, moneyAmount+"");

        // 必需参数，所购买商品名称，应用指定，建议中文，最大10个中文字。
        bundle.putString(ProtocolKeys.PRODUCT_NAME, payname);

        // 必需参数，购买商品的商品id，应用指定，最大16字符。
        bundle.putString(ProtocolKeys.PRODUCT_ID, productCode);

        // 必需参数，应用方提供的支付结果通知uri，最大255字符。360服务器将把支付接口回调给该uri，具体协议请查看文档中，支付结果通知接口–应用服务器提供接口。
        bundle.putString(ProtocolKeys.NOTIFY_URI, "http://sa.91muzhi.com:8080/sdk/FeeRsq/backkom360.action");

        // 必需参数，游戏或应用名称，最大16中文字。
        bundle.putString(ProtocolKeys.APP_NAME, app_name);

        // 必需参数，应用内的用户名，如游戏角色名。 若应用内绑定360账号和应用账号，则可用360用户名，最大16中文字。（充值不分区服，
        // 充到统一的用户账户，各区服角色均可使用）。
        if(mQihooUserInfo!=null){
        	bundle.putString(ProtocolKeys.APP_USER_NAME, mQihooUserInfo.getName());
        }
        // 必需参数，应用内的用户id。
        // 若应用内绑定360账号和应用账号，充值不分区服，充到统一的用户账户，各区服角色均可使用，则可用360用户ID最大32字符。
        if(qihooUserId != null){
        	 bundle.putString(ProtocolKeys.APP_USER_ID, qihooUserId);
        }
//        // 可选参数，应用扩展信息1，原样返回，最大255字符。
//        bundle.putString(ProtocolKeys.APP_EXT_1, pay.getAppExt1());
//
//        // 可选参数，应用扩展信息2，原样返回，最大255字符。
//        bundle.putString(ProtocolKeys.APP_EXT_2, pay.getAppExt2());

        // 可选参数，应用订单号，应用内必须唯一，最大32字符。
        bundle.putString(ProtocolKeys.APP_ORDER_ID, payorderid);

        // 必需参数，使用360SDK的支付模块。
        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_PAY);

        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtras(bundle);

     // 必需参数，使用360SDK的支付模块。
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_PAY);

        // 启动接口
        Matrix.invokeActivity(context, intent, new IDispatcherCallback() {

            @Override
            public void onFinished(String data) {
//                Log.d(TAG, "mPayCallback, data is " + data);
                if(TextUtils.isEmpty(data)) {
                    return;
                }

//                boolean isCallbackParseOk = false;
                JSONObject jsonRes;
                try {
                    jsonRes = new JSONObject(data);
                    // error_code 状态码： 0 支付成功， -1 支付取消， 1 支付失败， -2 支付进行中, 4010201和4009911 登录状态已失效，引导用户重新登录
                    // error_msg 状态描述
                    int errorCode = jsonRes.optInt("error_code");
//                    isCallbackParseOk = true;
                    final PayResultInfo info = new PayResultInfo();
                    info.payType = "qihu360";
//            		final Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
                    switch (errorCode) {
                        case 0://支付成功
        					new Thread(new Runnable() {
        						
        						@Override
        						public void run() {
        							// TODO Auto-generated method stub
        							//http://211.154.152.59:8080/sdk/orderState?order=e67ac969dd3542
        							int step = 0;
        							while (step<10) {
        								step++;
        								String url = "http://sa.91muzhi.com:8080/sdk/orderState"+"?order="+payorderid;
        								String requestResponse = HttpRequestt.get(url).body();
        								Util_G.debugE("qihooPayInstance--back-->>", requestResponse);
        								if(requestResponse != null && !"".equals(requestResponse) && !"{}".equals(requestResponse)){
        									try {
        										JSONObject ret = new JSONObject(requestResponse);
        										String status = getJsonString(ret,
        												"ret");
        										String amount = getJsonString(ret, "fee");
        										if(status.equals("1")){
        											Util_G.debugE("qihooPayInstance", "支付"+amount+"成功。。。");
        											info.resutCode = PayConfig.BIIL_SUCC;
        											info.retMsg = "支付成功！";
        										}else{
        											info.resutCode = PayConfig.MM_BIIL_FAIL;
        											info.retMsg = "支付失败";
        										}
        										notifyResultt(info);
        										break;
        									} catch (JSONException e) {
        										e.printStackTrace();
        									}
        								}
        								try {
        									Thread.sleep(2000);
        								} catch (InterruptedException e) {
        									e.printStackTrace();
        								}
        							}
        						}
        					}).start();
                        	break;
                        case 1:
                        	info.resutCode = PayConfig.BIIL_CANCER;
                        	info.retMsg = "支付取消";
                        	notifyResultt(info);
                        	break;
                        case -1:
                        	info.resutCode = PayConfig.MM_BIIL_FAIL;
    						info.retMsg = "支付失败";
    						notifyResultt(info);
    						break;
                        case -2: {
                            isAccessTokenValid = true;
                            isQTValid = true;
                            String errorMsg = jsonRes.optString("error_msg");
//                            String text = getString(R.string.pay_callback_toast, errorCode, errorMsg);
//                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case 4010201:
                            //acess_token失效
                            isAccessTokenValid = false;
//                            Toast.makeText(SdkUserBaseActivity.this, R.string.access_token_invalid, Toast.LENGTH_SHORT).show();
                            break;
                        case 4009911:
                            //QT失效
                            isQTValid = false;
//                            Toast.makeText(SdkUserBaseActivity.this, R.string.qt_invalid, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                // 用于测试数据格式是否异常。
//                if (!isCallbackParseOk) {
//                    Toast.makeText(SdkUserBaseActivity.this, getString(R.string.data_format_error),
//                            Toast.LENGTH_LONG).show();
//                }
            }
        });
	}
	
	private void notifyResultt(final PayResultInfo info) {
		Message message = callBHandler
				.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
		message.obj = info;
		message.sendToTarget();
	}
        
	public static String getJsonString(JSONObject json, String key) {
		String result = null;
		try {
			result = json.getString(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	// 登录、注册的回调
    private IDispatcherCallback mLoginCallback = new IDispatcherCallback() {

        @Override
        public void onFinished(String data) {
            // press back
            if (isCancelLogin(data)) {
                return;
            }
            // 显示一下登录结果
//            Toast.makeText(mContext, "登录成功", Toast.LENGTH_LONG).show();
            mQihooUserInfo = null;
//            Log.d(TAG, "mLoginCallback, data is " + data);
            // 解析access_token
            mAccessToken = parseAccessTokenFromLoginResult(data);
            if (!TextUtils.isEmpty(mAccessToken)) {
                // 需要去应用的服务器获取用access_token获取一下带qid的用户信息
            	Util_G.debugE("mAccessToken:", mAccessToken);
                getUserInfo();
            } else {
//                Toast.makeText(SdkUserBaseActivity.this, "get access_token failed!", Toast.LENGTH_LONG).show();
            	Util_G.debug_e("--------", "get access_token failed!");
            }

        }
    };
    private void getUserInfo() {

        isAccessTokenValid = true;
        isQTValid = true;
        final QihooUserInfoTask mUserInfoTask = QihooUserInfoTask.newInstance();

        // 请求应用服务器，用AccessToken换取UserInfo
        mUserInfoTask.doRequest(mContext, mAccessToken, "c2a7ce75d4d78fdfd3991be6e235e131", new QihooUserInfoListener() {

            @Override
            public void onGotUserInfo(QihooUserInfo userInfo) {
//                progress.dismiss();
                if (null == userInfo || !userInfo.isValid()) {
//                    Toast.makeText(SdkUserBaseActivity.this, "从应用服务器获取用户信息失败", Toast.LENGTH_LONG).show();
                } else {
                    Qihu360PayInstance.this.onGotUserInfo(userInfo);
                }
            }
        });
    }
    
    public void onGotUserInfo(QihooUserInfo userInfo) {
    	Log.i("==============","mQihooUserInfo save ");
    	mQihooUserInfo = userInfo;
    	Log.i("==============","mQihooUserInfo : "+mQihooUserInfo);
    	Util_G.debugE("mQihooUserInfo : ", mQihooUserInfo.toString());
    }
    
    private boolean isCancelLogin(String data) {
        try {
            JSONObject joData = new JSONObject(data);
            int errno = joData.optInt("errno", -1);
            if (-1 == errno) {
                Toast.makeText(mContext, data, Toast.LENGTH_LONG).show();
                return true;
            }
        } catch (Exception e) {}
        return false;
    }
    
    private String parseAccessTokenFromLoginResult(String loginRes) {
        try {

            JSONObject joRes = new JSONObject(loginRes);
            JSONObject joData = joRes.getJSONObject("data");
            return joData.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
	@Override
	public void exitsdk(Context context, Object...parameters) {
		// TODO Auto-generated method stub
		
		boolean isLandScape = (Boolean) parameters[0];
		final QihuCallBake listener = (QihuCallBake) parameters[1];
		 Bundle bundle = new Bundle();

	        // 界面相关参数，360SDK界面是否以横屏显示。
	        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

	        // 必需参数，使用360SDK的退出模块。
	        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_QUIT);

	        // 可选参数，登录界面的背景图片路径，必须是本地图片路径
//	        bundle.putString(ProtocolKeys.UI_BACKGROUND_PICTRUE, "");

	        Intent intent = new Intent(context, ContainerActivity.class);
	        intent.putExtras(bundle);

	        Matrix.invokeActivity(context, intent, new IDispatcherCallback() {
				
				@Override
				public void onFinished(String data) {

//	                Log.d(TAG, "mQuitCallback, data is " + data);
	                JSONObject json;
	                try {
	                    json = new JSONObject(data);
	                    int which = json.optInt("which", -1);

	                    switch (which) {
	                        case 0: // 用户关闭退出界面
	                            return;
	                        default:// 退出游戏
	                        	
	                        	listener.callBack(true);
	                        	Util_G.debug_e("exitGame: ", "游戏退出。。。。");
	                            return;
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            
				}
			});
	}
    
	 // 退出的回调
    

}
