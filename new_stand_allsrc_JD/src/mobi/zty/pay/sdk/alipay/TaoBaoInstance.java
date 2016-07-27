package mobi.zty.pay.sdk.alipay;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.sdk.game.Constants;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.bean.AlipayOrderInfo;
import mobi.zty.sdk.game.callback.AlipayOrderInfoListener;
import mobi.zty.sdk.game.object.parser.AlipayOrderInfoParser;
import mobi.zty.sdk.http.HttpRequest;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

public class TaoBaoInstance extends PaymentInterf {
	private Handler callBHandler = null;
	private static TaoBaoInstance instance;
	private String payOrder;
	public String payResultUrl = "http://211.154.152.59:8080/sdk/orderState";

	public static TaoBaoInstance getInstance() {
		if (instance == null) {
			instance = scyTaoBaopay();
		}
		return instance;
	}

	private static synchronized TaoBaoInstance scyTaoBaopay() {
		if (instance == null) {
			instance = new TaoBaoInstance();
		}
		return instance;
	}

	@Override
	public void init(Context context, Object... parameters) {
		callBHandler = (Handler) parameters[0];
	}

	@Override
	public void pay(final Context context, Object... parameters) {
		payOrder = (String) parameters[0];
		final int costMoney = (Integer) parameters[1];
		final String productName = (String) parameters[2];
		final String payway = (String) parameters[3];
		final String api = String.format(Constants.SERVER_URL, "alipay_sign");
		
		HttpRequest<AlipayOrderInfo> request = new HttpRequest<AlipayOrderInfo>(
				context,null,new AlipayOrderInfoParser(), new AlipayOrderInfoListener(
						callBHandler, (Activity)context));
		try {
			JSONObject payRequest = new JSONObject();
			payRequest.put("device_id", GameSDK.getInstance().deviceId);
			payRequest.put("packet_id", GameSDK.getInstance().packetId);
			payRequest.put("payway", payway);
			payRequest.put("game_id", GameSDK.getInstance().gameId);
			payRequest.put("cp_order_id", payOrder);
			payRequest.put("total_fee", costMoney);
			payRequest.put("payname", productName);
			payRequest.put("ratio", 10);
			payRequest.put("ver", Constants.GAME_SDK_VERSION);
			request.execute(api, payRequest.toString());
		} catch (Exception ex) {
			callBHandler.sendEmptyMessage(PayConfig.ALIPAY_PAY_FAIL);
		}
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	// /**
	// *
	// * @param money
	// * @param trade
	// * @param productName
	// * @return
	// */
	// private String getNewOrderInfo(String money, String trade,String
	// productName) {
	// StringBuilder sb = new StringBuilder();
	// sb.append("partner=\"");
	// sb.append(AlipayKeys.DEFAULT_PARTNER);
	// sb.append("\"&out_trade_no=\"");
	// sb.append(trade);
	// sb.append("\"&subject=\"");
	// sb.append(productName);
	// sb.append("\"&body=\"");
	// sb.append(productName);
	// sb.append("\"&total_fee=\"");
	// sb.append(money.replace("一口价:", ""));
	// sb.append("\"&service=\"mobile.securitypay.pay");
	// sb.append("\"&_input_charset=\"UTF-8");
	// sb.append("\"&return_url=\"");
	// sb.append(URLEncoder.encode("http://m.alipay.com"));
	// sb.append("\"&notify_url=\"");
	// sb.append(URLEncoder.encode("http://"+HttpService.HTTP_HOST+"/alipay/pay/cb"));
	// sb.append("\"&payment_type=\"1");
	// sb.append("\"&seller_id=\"");
	// sb.append(AlipayKeys.DEFAULT_PARTNER);
	// sb.append("\"&it_b_pay=\"1m");
	// sb.append("\"");
	//
	// return new String(sb);
	// }
}
