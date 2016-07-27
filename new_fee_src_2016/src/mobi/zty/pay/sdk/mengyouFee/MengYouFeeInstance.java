package mobi.zty.pay.sdk.mengyouFee;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;








import com.moe.pay.MoePay;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.sdk.game.bean.FeeInfo;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.Util_G;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 萌游支付管理类
 * @author Administrator
 *
 */
public class MengYouFeeInstance extends PaymentInterf{
	private static MengYouFeeInstance instance;
	private Handler callBHandler = null;
	private String code;
	private String extraInfo;
	public static MengYouFeeInstance getInstance(){
		if(instance==null){
			instance = scyMMpay();
		}
		return instance;
	}
	private static synchronized MengYouFeeInstance scyMMpay(){
		if(instance==null){
			instance =  new MengYouFeeInstance();
		}
		return instance;
	}
	@Override
	public void init(final Context context, Object... parameters) {
		if (callBHandler==null) {
			callBHandler = (Handler) parameters[0];
		}
		MoePay.onCreate(context);

	}
	

	@Override
	public void pay(final Context context, Object... parameters) {
		final FeeInfo feeinfo = (FeeInfo) parameters[0];
		
		if(feeinfo == null || feeinfo.sdkPayInfo == null){
			notiFyResult(PayConfig.SDK_INFO_NULL, "后台没给计费点", "0000000000");
			Helper.sendPayMessageToServer(PayConfig.ZHANGPAY_PAY, "后台没给计费点", "0000000000");
			return;
		}
		
		final String productName = feeinfo.feeName;
//		final String appName = feeinfo.sdkPayInfo.appName;
		final int point = feeinfo.consume;
		//String index = String.valueOf(feeinfo.);
		extraInfo = feeinfo.orderId;
		
		/**
		* paramContext调用计费的当前activity
		* paramString1 支付订单编号，由游戏方生成，保证游戏中唯一，长度32以内
		* paramString2计费点编号，数字
		* paramString3 计费点名称
		* paramInt 计费点价格（分）
		* paramString4  CP参数
		* paramHandler支付结果回调handler
		**/
		MoePay.pay(context,extraInfo,extraInfo, productName, point, "5301", mHandler);

	}
	
	private void notiFyResult(int resutCode,String retMsg,String orderId) {
		PayResultInfo info = new PayResultInfo();
		info.resutCode = resutCode;
		info.retMsg = retMsg;
		info.orderId = orderId;
		Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
		message.obj = info;
		message.sendToTarget();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle result = (Bundle) msg.obj;
			//订单号
			String app_orderid = result.getString(MoePay.APP_ORDERID);
			//支付金额
			//String pay_amount = result.getString(MoePay.PAY_AMOUNT);
			//支付状态 0成功 其他失败
			String code = result.getString(MoePay.RESULT_CODE);
			Util_G.debugE("code-->>", code);
			if(!"".equals(code) && code!=null && code.equals("0")){
				Helper.sendPayMessageToServer(PayConfig.MENGYOUSDK_PAY, "支付成功并发货", "0000000000");
				notiFyResult(PayConfig.BIIL_SUCC, "支付成功", app_orderid);
			}else {
				Helper.sendPayMessageToServer(PayConfig.MENGYOUSDK_PAY, "萌游代码返回失败", "0000000000");
				notiFyResult(PayConfig.SP_PAY_FAIL, "支付失败", app_orderid);
			}
		}
	};

}
