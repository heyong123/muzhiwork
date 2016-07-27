package mobi.zty.pay.sdk.uincon;

import com.unicom.dcLoader.Utils;
import com.unicom.dcLoader.Utils.UnipayPayResultListener;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.pay.sdk.ShopInfo;
import mobi.zty.sdk.util.Util_G;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class UniconPayInstance extends PaymentInterf {

	private static UniconPayInstance instance;
	private Handler callBHandler = null;
	private boolean isInitSCC = false;
	
	private ShopInfo shopInfo = null;
	public static UniconPayInstance getInstance(){
		if(instance==null){
			instance = scyUinconPay();
		}
		return instance;
	}
	private static synchronized UniconPayInstance scyUinconPay(){
		if(instance==null){
			instance =  new UniconPayInstance();
		}
		return instance;
	}
	@Override
	public void init(Context context, Object... parameters) {
		String appId = (String)parameters[0];
		shopInfo = PayConfig.getShopInfo(appId,PayConfig.UNICOM_MOBLE);
		callBHandler = (Handler) parameters[1];
		if (shopInfo == null) {
			PayResultInfo info = new PayResultInfo();
			info.resutCode = PayConfig.UNICON_INIT_FAIL;
			info.retMsg = "gameId不存在-->>" +appId;
			Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
			message.obj = info;
			message.sendToTarget();
			Util_G.debug_e("Alppay", info.retMsg);
			return;
		}
		isInitSCC = true;
	}

	@Override
	public void pay(Context context, Object... parameters) {
		int mPayIndex = (Integer) parameters[0];
		final PayResultInfo info = new PayResultInfo();
		final Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
		if (!isInitSCC) {
			info.resutCode = PayConfig.UNICON_INIT_FAIL;
			info.retMsg = "未初始化成功！";
			message.obj = info;
			message.sendToTarget();
		}else if (shopInfo == null) {
			info.resutCode = PayConfig.UNICON_INIT_FAIL;
			info.retMsg = "初始化失败!";
			message.obj = info;
			message.sendToTarget();
		}else if(!shopInfo.payCodeMap.containsKey(mPayIndex)){
			info.resutCode = PayConfig.UNICON_CODE_ERRO;
			info.retMsg = "传入的支付索引有误!";
			message.obj = info;
			message.sendToTarget();
		}else{
			Log.e("shopInfo", "shopInfo-->"+shopInfo.payCodeMap.get(mPayIndex));
			Utils.getInstances().pay(context, shopInfo.payCodeMap.get(mPayIndex),
					new UnipayPayResultListener() {
						@Override
						public void PayResult(String paycode, int flag, int flag2,
								String error) {
							// TODO Auto-generated methodstub
							switch (flag) {
							case 1:// success
								// 此处放置支付请求已提交的相关处理代码
								info.resutCode = PayConfig.BIIL_SUCC;
								info.retMsg = "1";
								message.obj = info;
								message.sendToTarget();
								break;

							case 2:// fail
								// 此处放置支付请求失败的相关处理代码
								info.resutCode = PayConfig.UNICON_PAY_FAIL;
								info.retMsg = "失败code:" + error;
								message.obj = info;
								message.sendToTarget();
								break;
							case 3:// cancel
									// 此处放置支付请求被取消的相关处理代码
								info.resutCode = PayConfig.BIIL_CANCER;
								info.retMsg = "你取消了支付!";
								message.obj = info;
								message.sendToTarget();
								break;
							default:
								break;
							}
						}

					});
		}
		
	}

}
