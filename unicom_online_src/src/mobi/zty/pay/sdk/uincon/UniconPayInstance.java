package mobi.zty.pay.sdk.uincon;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.pay.sdk.ShopInfo;
import mobi.zty.sdk.util.Util_G;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.unicom.dcLoader.Utils;
import com.unicom.dcLoader.Utils.UnipayPayResultListener;
import com.unipay.account.AccountAPI.BusyException;
import com.unipay.account.AccountAPI.OnInitResultListener;
import com.unipay.account.AccountAPI;
import com.unipay.account.UnipayAccountPlatform;

public class UniconPayInstance extends PaymentInterf {

	private static UniconPayInstance instance;
	private Handler callBHandler = null;
	private boolean isInitSCC = false;
	
	private ShopInfo shopInfo = null;
	private String CLIENT_ID = "90810002096120160405122239736100";//客户端id
	private String CLIENT_KEY = "08fc9b86c3ea08926b82";//客户端密钥
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
		}else{
			try {
				UnipayAccountPlatform.init(context, CLIENT_ID, CLIENT_KEY, new OnInitResultListener() {
					
					@Override
					public void onResult(int code, String message) {
						// TODO Auto-generated method stub
						if (code == AccountAPI.CODE_SUCCESS) {
							Util_G.debugE("UniconPayInstance-->>", "初始化成功");	
						} else {
							Util_G.debugE("UniconPayInstance-->>", "初始化失败");
						}
					}
				});
			} catch (BusyException e) {
				return;
			}
			isInitSCC = true;
		}
		
	}

	@Override
	public void pay(Context context, Object... parameters) {
		int mPayIndex = (Integer) parameters[0];
		String orderId = (String) parameters[1];
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
			Log.e("shopInfo", "shopInfo"
					+ "-->"+shopInfo.payCodeMap.get(mPayIndex));
			Utils.getInstances().payOnlineWithWostre(context, shopInfo.payCodeMap.get(mPayIndex), "0", orderId,
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
