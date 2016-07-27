package mobi.zty.pay.sdk.mm;

import java.util.HashMap;

import mm.purchasesdk.OnPurchaseListener;
import mm.purchasesdk.Purchase;
import mm.purchasesdk.core.PurchaseCode;
import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.sdk.util.Util_G;

import org.json.JSONException;
import org.json.JSONObject;

import com.chinaMobile.MobileAgent;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class IAPListener implements OnPurchaseListener {
	private final String TAG = "IAPListener";
	private Context context;
	private Handler iapHandler;

	public IAPListener(Context context, Handler iapHandler) {
		this.context = context;
		this.iapHandler = iapHandler;
	}


	public void onInitFinish(String code) {
		Log.d(TAG, "Init finish, status code = " + code);
		PayResultInfo info = new PayResultInfo();
		info.resutCode = PayConfig.MM_INIT_CODE;
		info.retMsg = Purchase.getReason(code);
		Util_G.debugE("ALLPAY", "MM初始化——》"+info.retMsg);
		Message message = iapHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
		message.obj = info;
		message.sendToTarget();
	}

	@Override
	public void onBillingFinish(String code, HashMap arg1) {
		Message message = iapHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
		PayResultInfo info = new PayResultInfo();
		
		// 商品信息
		String paycode = null;
		// 商品的交易 ID，用户可以根据这个交易ID，查询商品是否已经交易
		String tradeID = null;
		
		if (PurchaseCode.BILL_ORDER_OK.equalsIgnoreCase(code)
				|| PurchaseCode.AUTH_OK.equalsIgnoreCase(code)
				|| PurchaseCode.WEAK_ORDER_OK.equalsIgnoreCase(code)) {
			if (arg1 != null) {
				paycode = (String) arg1.get(OnPurchaseListener.PAYCODE);
				
				tradeID = (String) arg1.get(OnPurchaseListener.TRADEID);
				if (tradeID != null && tradeID.trim().length() != 0) {
					info.retMsg = tradeID;
				}else{
					info.retMsg = "已购买";
				}
			}
			info.resutCode = PayConfig.BIIL_SUCC;
			message.obj = info;
			message.sendToTarget();
		} else if(PurchaseCode.BILL_CANCEL_FAIL.equalsIgnoreCase(code)||
				PurchaseCode.WEAK_BILL_CANCEL_FAIL.equalsIgnoreCase(code))
		{
			info.resutCode = PayConfig.BIIL_CANCER;
			info.retMsg = "支付取消";
			message.obj = info;
			message.sendToTarget();
		}
		else {
			/**
			 * 表示订购失败。
			 */
//			info.resutCode = PayConfig.MM_BIIL_FAIL;
			info.resutCode = PayConfig.MM_INIT_CODE;
//			info.retMsg = "订购结果：" + Purchase.getReason(code);
			info.retMsg = "支付失败：errorCode:"+code;
			message.obj = info;
			message.sendToTarget();
		}
	}

	@Override
	public void onAfterApply() {
		Log.e("onAfterApply", "onAfterApply");
	}

	@Override
	public void onAfterDownload() {
		Log.e("onAfterDownload", "onAfterDownload");
	}

	@Override
	public void onBeforeApply() {
		Log.e("onBeforeApply", "onBeforeApply");
	}

	@Override
	public void onBeforeDownload() {
		Log.e("onBeforeDownload", "onBeforeDownload");
	}



	@Override
	public void onQueryFinish(String code, HashMap arg1) {
		Log.d(TAG, "license finish, status code = " + code);
		String result = "查询成功,该商品已购买";
		// 此次订购的orderID
		String orderID = null;
		// 商品的paycode
		String paycode = null;
		// 商品的有效期(仅租赁类型商品有效)
		String leftday = null;
		if (code != PurchaseCode.QUERY_OK) {
			/**
			 * 查询不到商品购买的相关信息
			 */
			result = "查询结果：" + Purchase.getReason(code);
		} else {
			/**
			 * 查询到商品的相关信息。
			 * 此时你可以获得商品的paycode，orderid，以及商品的有效期leftday（仅租赁类型商品可以返回）
			 */
			leftday = (String) arg1.get(OnPurchaseListener.LEFTDAY);
			if (leftday != null && leftday.trim().length() != 0) {
				result = result + ",剩余时间 ： " + leftday;
			}
			orderID = (String) arg1.get(OnPurchaseListener.ORDERID);
			if (orderID != null && orderID.trim().length() != 0) {
				result = result + ",OrderID ： " + orderID;
			}
			paycode = (String) arg1.get(OnPurchaseListener.PAYCODE);
			if (paycode != null && paycode.trim().length() != 0) {
				result = result + ",Paycode:" + paycode;
			}
		}
		System.out.println(result);
		//context.dismissProgressDialog();
	}

	

	@Override
	public void onUnsubscribeFinish(String code) {
		// TODO Auto-generated method stub
		String result = "退订结果：" + Purchase.getReason(code);
		System.out.println(result);
		//context.dismissProgressDialog();
	}

}
