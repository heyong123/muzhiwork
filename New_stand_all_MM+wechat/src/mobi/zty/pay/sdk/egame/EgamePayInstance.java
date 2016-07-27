package mobi.zty.pay.sdk.egame;

import java.util.HashMap;
import java.util.Map;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.pay.sdk.ShopInfo;
import mobi.zty.sdk.util.Util_G;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.egame.terminal.paysdk.EgamePay;
import cn.egame.terminal.paysdk.EgamePayListener;

public class EgamePayInstance extends PaymentInterf {

	private static EgamePayInstance instance;
	private Handler callBHandler = null;
	private ShopInfo shopInfo = null;
	public static EgamePayInstance getInstance(){
		if(instance==null){
			instance = scyTCpay();
		}
		return instance;
	}
	private static synchronized EgamePayInstance scyTCpay(){
		if(instance==null){
			instance =  new EgamePayInstance();
		}
		return instance;
	}

	@Override
	public void init(Context context, Object... parameters) {
		String appId = (String)parameters[0];
		shopInfo = PayConfig.getShopInfo(appId,PayConfig.TIANYI_MOBLE);
		callBHandler = (Handler) parameters[1];
		if (shopInfo == null) {
			PayResultInfo info = new PayResultInfo();
			info.resutCode = PayConfig.TC_INIT_FAIL;
			info.retMsg = "gameId不存在-->>" +appId;
			Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
			message.obj = info;
			message.sendToTarget();
			Util_G.debug_e("Alppay", info.retMsg);
			return;
		}
		EgamePay.init((Activity) context);
	}

	@Override 
	public void pay(Context context, Object... parameters) {
		int mPayIndex = (Integer) parameters[0];
		String payOrder = (String) parameters[1];
		final PayResultInfo info = new PayResultInfo();
		final Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
		if (shopInfo == null) {
			Log.e("CNCCMMInit", "请初始化电信支付接口");
			info.resutCode = PayConfig.TC_INIT_FAIL;
			info.retMsg = "初始化失败!";
			message.obj = info;
			message.sendToTarget();
		}else if(!shopInfo.payCodeMap.containsKey(mPayIndex)){
			info.resutCode = PayConfig.TC_CODE_ERRO;
			info.retMsg = "传入的支付索引有误!";
			message.obj = info;
			message.sendToTarget();
		}else{
			HashMap<String, String> payParams=new HashMap<String, String>();
	    	//EgamePay.PAY_PARAMS_KEY_TOOLS_DESC;
		    //payParams.put(EgamePay.PAY_PARAMS_KEY_TOOLS_PRICE, String.format("%d", requestAmount));
			int money = (shopInfo.pricesMap.get(mPayIndex)/100);
			Log.e("EgamePayInstance", "EgamePayInstance-->"+shopInfo.payCodeMap.get(mPayIndex));
			Log.e("EgamePayInstance", "EgamePayInstance-->"+money);
			payParams.put(EgamePay.PAY_PARAMS_KEY_TOOLS_ALIAS, shopInfo.payCodeMap.get(mPayIndex));
			payParams.put(EgamePay.PAY_PARAMS_KEY_TOOLS_PRICE,money+"");
			payParams.put(EgamePay.PAY_PARAMS_KEY_CP_PARAMS, payOrder);
			//payParams.put(EgamePay.PAY_PARAMS_KEY_PRIORITY, "other");
			//sapay_ret("");/////////////////////////////
			
	    	EgamePay.pay((Activity) context, payParams,new EgamePayListener() {
				@Override
				public void paySuccess(Map<String, String> params) {
					info.resutCode = PayConfig.BIIL_SUCC;
					info.retMsg = "1";
					message.obj = info;
					message.sendToTarget();
				}
				
				@Override
				public void payFailed(Map<String, String> params, int errorInt) {
					info.resutCode = PayConfig.TC_PAY_FAIL;
					info.retMsg = "道具"+params.get(EgamePay.PAY_PARAMS_KEY_TOOLS_PRICE)+"支付失败：错误代码："+errorInt;
					message.obj = info;
					message.sendToTarget();
				}
				
				@Override
				public void payCancel(Map<String, String> params) {
					info.resutCode = PayConfig.BIIL_CANCER;
					info.retMsg = "你取消了支付!";
					message.obj = info;
					message.sendToTarget();
				}
			});      
		}
	}

}
