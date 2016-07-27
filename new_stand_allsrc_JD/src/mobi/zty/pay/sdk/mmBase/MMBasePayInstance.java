package mobi.zty.pay.sdk.mmBase;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.pay.sdk.ShopInfo;
import mobi.zty.sdk.util.Util_G;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import cn.cmgame.billing.api.BillingResult;
import cn.cmgame.billing.api.GameInterface;

public class MMBasePayInstance extends PaymentInterf {

	private static MMBasePayInstance instance;
	private Handler callBHandler = null;
	private ShopInfo shopInfo = null;
	private boolean initSCC = false;
	public static MMBasePayInstance getInstance(){
		if(instance==null){
			instance = scyMMBasepay();
		}
		return instance;
	}
	private static synchronized MMBasePayInstance scyMMBasepay(){
		if(instance==null){
			instance =  new MMBasePayInstance();
		}
		return instance;
	}

	@Override
	public void init(Context context, Object... parameters) {
		String appId = (String)parameters[0];
		shopInfo = PayConfig.getShopInfo(appId,PayConfig.JD_PAY);
		callBHandler = (Handler) parameters[1];
		if (shopInfo == null) {
			Util_G.debugE("AllPay", "appId error！");
			PayResultInfo info = new PayResultInfo();
			info.resutCode = PayConfig.JD_INIT_FAIL;
			info.retMsg = "gameId不存在";
			Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
			message.obj = info;
			message.sendToTarget();
			return;
		}
		GameInterface.initializeApp((Activity)context);
		initSCC = true;
	}

	@Override 
	public void pay(Context context, Object... parameters) {
		int mPayIndex = (Integer) parameters[0];
		if (shopInfo==null) {
			PayResultInfo info = new PayResultInfo();
			info.resutCode = PayConfig.JD_INIT_FAIL;
			info.retMsg = "gameId不存在";
			Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
			message.obj = info;
			message.sendToTarget();
		}else if(!shopInfo.payCodeMap.containsKey(mPayIndex)){
			PayResultInfo info = new PayResultInfo();
			info.resutCode = PayConfig.MM_CODE_ERRO;
			info.retMsg = "传入的支付索引有误!";
			Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
			message.obj = info;
			message.sendToTarget();
		}else{
			Util_G.debugE("AllPay", "GameInterface.doBilling start");
			 // 计费结果的监听处理，合作方通常需要在收到SDK返回的onResult时，告知用户的购买结果
		    GameInterface.IPayCallback payCallback = new GameInterface.IPayCallback() {
		      @Override
		      public void onResult(int resultCode, String billingIndex, Object obj) {
		        String result = "";
		        switch (resultCode) {
		          case BillingResult.SUCCESS:{
		            result = "购买道具：[" + billingIndex + "] 成功！";
		            PayResultInfo info = new PayResultInfo();
					info.resutCode = PayConfig.BIIL_SUCC;
					info.retMsg = result;
					Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
					message.obj = info;
					message.sendToTarget();
		            break;
		          }
		          case BillingResult.FAILED:{
		            result = "购买道具：[" + billingIndex + "] 失败！";
		            PayResultInfo info = new PayResultInfo();
					info.resutCode = PayConfig.JD_PAY_FAIL;
					info.retMsg = result;
					Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
					message.obj = info;
					message.sendToTarget();
		            break;
		          }
					default: {
						result = "购买道具：[" + billingIndex + "] 取消！";

						PayResultInfo info = new PayResultInfo();
						info.resutCode = PayConfig.BIIL_CANCER;
						info.retMsg = result;
						Message message = callBHandler
								.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
						message.obj = info;
						message.sendToTarget();
						break;
					}
					}
		        Util_G.debugE("AllPay", "GameInterface.doBilling result->>"+result);
		      }
		    };
			GameInterface.doBilling(context, true, true, shopInfo.payCodeMap.get(mPayIndex), null, payCallback);
		}
		
		
	}

}
