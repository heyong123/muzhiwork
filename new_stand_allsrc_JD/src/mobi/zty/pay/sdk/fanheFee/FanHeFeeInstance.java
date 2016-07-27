package mobi.zty.pay.sdk.fanheFee;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.sdk.util.Helper;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tg52.action.PayMentAction;
import com.tg52.action.PayMentCallBack;


/**
 * 联通支付 饭盒支付的管理类
 * @author Administrator
 *
 */
public class FanHeFeeInstance extends PaymentInterf{
	private static FanHeFeeInstance instance;
	private Handler callBHandler = null;
	public static FanHeFeeInstance getInstance(){
		if(instance==null){
			instance = scyMMpay();
		}
		return instance;
	}
	private static synchronized FanHeFeeInstance scyMMpay(){
		if(instance==null){
			instance =  new FanHeFeeInstance();
		}
		return instance;
	}
	@Override
	public void init(Context context, Object... parameters) {
		callBHandler = (Handler) parameters[0];
		PayMentAction.getInstance().init((Activity)context);
	}

	@Override
	public void pay(final Context context, Object... parameters) {
		
		String cpfee = (String) parameters[0];//计费代码
		cpfee = getPayCode(context, cpfee);
		final String extentsP = (String) parameters[1];//透传参数
		final String cpGoods = (String) parameters[2];//渠道商品名称
		callBHandler.sendEmptyMessageDelayed(PayConfig.NATIVE_FEE_FAIL, 28000);
		PayMentAction.getInstance().payment(cpfee, extentsP, cpGoods, (Activity) context, new PayMentCallBack() {
			
			@Override
			public void payResult(int resultCode, String msg) {
				callBHandler.removeMessages(PayConfig.NATIVE_FEE_FAIL);
				if (resultCode == 501) {//支付成功
					PayResultInfo info = new PayResultInfo();
					info.resutCode = PayConfig.BIIL_SUCC;
					info.retMsg = msg;
					Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
					message.obj = info;
					message.sendToTarget();
				}else{
					PayResultInfo info = new PayResultInfo();
					info.resutCode = PayConfig.FANHE_PAY_FAIL;
					info.retMsg = msg;
					Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
					message.obj = info;
					message.sendToTarget();
				}
				
			}
		});
	}
	private String getPayCode(final Context context, String cpfee) {
		String cpcode = Helper.getConfigString(context, "cpcode");
		cpfee= cpcode+cpfee;
		return cpfee;
	}
}
