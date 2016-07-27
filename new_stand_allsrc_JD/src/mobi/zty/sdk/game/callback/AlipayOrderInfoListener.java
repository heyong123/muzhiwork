package mobi.zty.sdk.game.callback;

import java.net.URLEncoder;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.sdk.game.Constants;
import mobi.zty.sdk.game.bean.AlipayOrderInfo;
import mobi.zty.sdk.http.HttpCallback;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

public class AlipayOrderInfoListener implements HttpCallback<AlipayOrderInfo> {
	private Handler mhandler;
	private Activity macticity;

	public AlipayOrderInfoListener(Handler handler,Activity acticity){
		mhandler = handler;
		macticity = acticity;
	}
	
	@Override
	public void onSuccess(AlipayOrderInfo object) {
		final String info = object.getOrderInfo() + "&sign=" + "\""
				+ URLEncoder.encode(object.getSign()) + "\""
				+ "&sign_type=\"" + object.getSignType() + "\"";

		Log.i(Constants.TAG, info);
		
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(macticity);
				// 调用支付接口
				String result = alipay.pay(info);//支付返回结果 是阻塞等待的

				Message msg = new Message();
				msg.what = PayConfig.ALIPAY_RQF_PAY;
				msg.obj = result;
				mhandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
		
	}

	@Override
	public void onFailure(int errorCode, String errorMessage) {
		mhandler.sendEmptyMessage(2);
	}

}
