package mobi.zty.pay;


import java.util.Date;

import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.GameSDKInitListener;
import mobi.zty.sdk.game.GameSDKPaymentListener;
import mobi.zty.sdk.util.Util_G;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.afdsfgh.jkiuuju.hsdfg.test.BuildConfig;
import com.afdsfgh.jkiuuju.hsdfg.test.R;

public class MainActivity extends Activity {
	EditText editText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
//		DnPayServer.getInstance().init(getApplicationContext(), );
//		if (android.os.Build.VERSION.SDK_INT > 9) {
//		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		    StrictMode.setThreadPolicy(policy);
//		}

		Log.d("MainActivity", android.os.Build.VERSION.RELEASE);
		editText = (EditText) findViewById(R.id.testEdit);
		findViewById(R.id.testPay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {	

				int payIndex = Integer.valueOf(editText.getText().toString().trim());
				
				GameSDK.getInstance().startPay(1, payIndex, "测试计费点",

						new GameSDKPaymentListener() {
							@Override
							public void onPayFinished(int mount) {
								float v = mount;
								v = v / 100;
								String str = String.format("支付%2f元成功",v);
								Log.e("ALLPAY", "cp收到支付结果回调"+str);
								GameSDK.getInstance().makeToast(str);
							}

							@Override
							public void onPayFail(PayResultInfo info) {
								GameSDK.getInstance().makeToast(info.retMsg);
							}

							@Override
							public void onPayCancelled() {
								GameSDK.getInstance().makeToast("操作取消");
							}
						});
			}
		}); 
//		// 开启debug模式，上线前需关闭
//		TP_PAY.setDebugMode(true);      
//		// 错误码提示,上线可开启
//		TP_PAY.setToastSwitchOn(true);
		// 初始化
		// 后面要依次填上下文， gameID ,packetID,初始化监听接口
		
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {// 测试模式，这段代码不要加入到你们的游戏中
			GameSDK.setDebug(true);
		}
		//5735275
		GameSDK.initSDK(this, "5262", "N613", new GameSDKInitListener() {

			/**
			 * openDark 模糊描述 0关闭1打开 
			 * openAlert 是否主动弹出计费框 0否 1是
			 *  openButton 0购买1确认2领取
			 * openOurAlert 是否另外弹出游戏的二次确认框 0否 1确认 
			 * cootype 是否是线下 0线上,1线下
			 * deceptive 坑人模式1：开启，0：关闭
			 */
			@Override
			public void onOpenDark(int openDark, int openAlert, int openButton,
					int openOurAlert, int cootype,int deceptive) {
				Util_G.debugE("allpay-->>", "oppenDark="+openDark+",openAlert="+openAlert+",openButton="+
						openButton+",openOurAlert="+openOurAlert+",coottype="+cootype);
			}
		}
		, "", "", Utils.getVersionCode(this) + "");// 初始化
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			GameSDK.getInstance().exitGame();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	//注意：如果需要支持微信支付 一定要在此方法中进行 调用
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		GameSDK.getInstance().onActivityResult(requestCode, resultCode, data);//
		super.onActivityResult(requestCode, resultCode, data);
	}

}
