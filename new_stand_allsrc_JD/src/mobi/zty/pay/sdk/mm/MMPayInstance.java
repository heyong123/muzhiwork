package mobi.zty.pay.sdk.mm;

import mm.purchasesdk.Purchase;
import mm.purchasesdk.core.ui.PurchaseSkin;
import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.pay.sdk.ShopInfo;
import mobi.zty.sdk.util.Util_G;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MMPayInstance extends PaymentInterf{

	private IAPListener mListener;
	public static Purchase purchase;
	private static MMPayInstance instance;
	private Handler callBHandler = null;
	private ShopInfo shopInfo = null;
	private Context context;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			initMMsdk();
		};
	};
	public static MMPayInstance getInstance(){
		if(instance==null){
			instance = scyMMpay();
		}
		return instance;
	}
	private static synchronized MMPayInstance scyMMpay(){
		if(instance==null){
			instance =  new MMPayInstance();
		}
		return instance;
	}

	@Override
	public void init(Context context,  Object... parameters) {
		String appId = (String)parameters[0];
		shopInfo = PayConfig.getShopInfo(appId,PayConfig.CMCC_MOBLE);
		callBHandler = (Handler) parameters[1];
		this.context = context;
		if (shopInfo == null) {
			PayResultInfo info = new PayResultInfo();
			info.resutCode = PayConfig.MM_INIT_FAIL;
			info.retMsg = "gameId不存在";
			Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
			message.obj = info;
			message.sendToTarget();
			return;
		}
		handler.sendEmptyMessageDelayed(0, 1000);
		initMMsdk();
	}
	private void initMMsdk() {
		/**
		 * IAP组件初始化.包括下面3步。
		 */
		/**
		 * step1.实例化PurchaseListener。实例化传入的参数与您实现PurchaseListener接口的对象有关。 例如，此Demo代码中使用IAPListener继承PurchaseListener，其构造函数需要Context实例。
		 */
		mListener = new IAPListener(context, callBHandler);
		/**
		 * step2.获取Purchase实例。
		 */
		purchase = Purchase.getInstance();
		/**
		 * step3.向Purhase传入应用信息。APPID，APPKEY。 需要传入参数APPID，APPKEY。 APPID，见开发者文档 APPKEY，见开发者文档
		 */
		try {
			purchase.setAppInfo(shopInfo.AppId, shopInfo.AppKey,PurchaseSkin.SKIN_SYSTEM_ONE);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/**
		 * step4. IAP组件初始化开始， 参数PurchaseListener，初始化函数需传入step1时实例化的 PurchaseListener。
		 */
		try {
			purchase.init(context, mListener);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void pay(Context context, Object... parameters) {
		int mPayIndex = (Integer) parameters[0];
		String imei = (String) parameters[1];
		int amount = (Integer) parameters[2];
		if (mListener == null||shopInfo == null) {
			Log.e("CNCCMMInit", "请初始化移动MM支付接口");
			PayResultInfo info = new PayResultInfo();
			info.resutCode = PayConfig.MM_INIT_FAIL;
			info.retMsg = Purchase.getReason("-1");
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
		} else
			try {
				Util_G.debugE("ALLPAY", "当前支付的MMcode==》"+shopInfo.payCodeMap.get(mPayIndex));
				Util_G.debugE("ALLPAY", "当前支付的MMKey==》"+shopInfo.AppKey);
				purchase.order(context, shopInfo.payCodeMap.get(mPayIndex),1,imei,false,mListener);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
}
