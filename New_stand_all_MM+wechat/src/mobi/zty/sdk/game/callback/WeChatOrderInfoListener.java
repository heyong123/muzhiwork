package mobi.zty.sdk.game.callback;

import java.util.Map;

import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.switfpass.pay.utils.XmlUtils;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.wecaht.WeChatInstance;
import mobi.zty.sdk.game.SendOder;
import mobi.zty.sdk.game.bean.WeiXinOrderInfo;
import mobi.zty.sdk.http.HttpCallback;
import mobi.zty.sdk.util.StringUtil;
import mobi.zty.sdk.util.Util_G;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


public class WeChatOrderInfoListener implements HttpCallback<WeiXinOrderInfo> {

	private Context mContext;
	private Handler callBackHandler;
	private Map<String, String> retMap;
	private Handler handler = new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
//			String result = "appId=1408709961320306&consumerId=456123&consumerName=test&mhtCharset=UTF-8&mhtCurrencyType=156&mhtOrderAmt=1&mhtOrderDetail=Buy PPTV Vip Privilege&mhtOrderName=PPTV_VIP&mhtOrderNo=20150821181629&mhtOrderStartTime=20150821181624&mhtOrderTimeOut=3600&mhtOrderType=01&mhtReserved=test&notifyUrl=http://localhost:10802/&payChannelType=13&mhtSignature=c98a96bd8fedaaea66afd05c17f211e3&mhtSignType=MD5";
//			IpaynowPlugin.pay((Activity) mContext,result);
			Util_G.debugE("WECHAT_PAY", "wechat支付请求信息："+result);
			
			retMap = XmlUtils.parse(result);
			if (retMap.get("status").equalsIgnoreCase("0")){
	        	// 成功
	            RequestMsg Rmsg = new RequestMsg();
	            
	            int amount = Integer.valueOf(SendOder.getInstance().amount);
	            Rmsg.setMoney(Double.parseDouble(amount+""));
	            Rmsg.setTokenId(retMap.get("token_id"));
	            Util_G.debugE("tokenid", retMap.get("token_id"));
	            Rmsg.setOutTradeNo(retMap.get("out_trade_no"));
	            // 微信wap支付
	            Rmsg.setTradeType(MainApplication.PAY_WX_WAP);
	            PayPlugin.unifiedH5Pay((Activity)mContext, Rmsg);
	            
	        }else{
	           Util_G.debugE("WECHAT_PAY", "微信支付失败");
	        }
		};
	};
	public WeChatOrderInfoListener(Handler handler ,Context context){
		mContext = context;
		this.callBackHandler = handler;
	}
	
	@Override
	public void onSuccess(WeiXinOrderInfo object) {
		String tn = object.getOrderInfo();
		if (StringUtil.isEmpty(tn)) {
			callBackHandler.sendEmptyMessage(PayConfig.WECAHT_FAIL);
		} else {
			goto_url(tn);
		}
		
	}

	@Override
	public void onFailure(int errorCode, String errorMessage) {
		callBackHandler.sendEmptyMessage(PayConfig.WECAHT_FAIL);
	}

	public void goto_url(String result) {
		Message message = handler.obtainMessage(1);
		message.obj = result;
		message.sendToTarget();
	}
}
