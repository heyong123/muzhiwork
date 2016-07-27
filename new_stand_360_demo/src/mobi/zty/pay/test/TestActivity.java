package mobi.zty.pay.test;

import com.centurysoft.backkom.qihu.BuildConfig;
import com.centurysoft.backkom.qihu.R;

import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.sdk.game.ExitGameListener;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.GameSDKInitListener;
import mobi.zty.sdk.game.GameSDKPaymentListener;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.Util_G;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


public class TestActivity extends Activity {
	
	private Button btn_login,btn_pay,btn_exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_test_layout);
		btn_login = (Button) findViewById(R.id.test_login);
		btn_pay = (Button) findViewById(R.id.test_Pay);
		btn_exit = (Button) findViewById(R.id.test_exit);
		
		/**
		 * 计算appsecret:99e09c529e98377f1cbbcbae6b5b4232
		 */
//		String appsecret = "81815467fda0465a3d8e1de4c7c12d93";
//		String appkey = "1aaa5d41e4ad80372191ee0876893a14";
//		String key = Helper.md5(appsecret + "#" + appkey );
//		Util_G.debugE("TestActivity","-->privatekey:"+ key);
//		Toast.makeText(TestActivity.this, key,Toast.LENGTH_LONG).show();
		//调用初始化
		GameSDK.initSDK(TestActivity.this, "300009370275", "f013", new GameSDKInitListener() {
			
			@Override
			public void onOpenDark(int openDark, int openAlert, int openButton,
					int openOurAlert, int cootype) {
				// TODO Auto-generated method stub
				GameSDK.getInstance().makeToast("初始化成功");
			}
			
			@Override
			public void initOver(boolean isOver, int payWay, boolean openVoice) {
				// TODO Auto-generated method stub
				
			}
		},"",MyApplication.payWay,Utils.getVersionCode(this)+"");
		if (BuildConfig.DEBUG) {
			GameSDK.setDebug(true);
		}
		super.onCreate(savedInstanceState);
		
		//登录

		btn_login.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//第二个参数为是否是横屏登录
//				GameSDK.getInstance().doSdkLogin(TestActivity.this,false);
			}
		});
				
				     
		//支付
		btn_pay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//金额（单位分）、payIndex（支付索引计费文件里面有）、道具名称
				GameSDK.getInstance().startPay(100,8, "10钻石", new GameSDKPaymentListener() {
							
					@Override
					public void onPayFinished(int mount) {
						float v = mount;
						v = v / 100;
						String str = String.format("支付%1f元成功", v);
						GameSDK.getInstance().makeToast(str);
					}
					@Override
					public void onPayCancelled() {
						GameSDK.getInstance().makeToast("支付取消");
						}
					@Override
					public void onPayFail(PayResultInfo info) {
						// TODO Auto-generated method stub
						GameSDK.getInstance().makeToast("支付失败");
					}
				});
			}
		});
		
		btn_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GameSDK.getInstance().exitGame(new ExitGameListener() {
					
					@Override
					public void exitGame(boolean sure) {
						// TODO Auto-generated method stub
						if(sure){
							Toast.makeText(TestActivity.this, "成功退出", Toast.LENGTH_LONG).show();
							finish();
						}
					}
				});
			}
		});
	}
	
	@Override
	protected void onResume() {
//		GameSDK.getInstance().onResume();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
//		GameSDK.getInstance().ondestroy();
//		GameSDK.getInstance().ondestroy();
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
//			doSdkQuit(false);
			GameSDK.getInstance().exitGame(new ExitGameListener() {
				
				@Override
				public void exitGame(boolean sure) {
					// TODO Auto-generated method stub
					if(sure){
						finish();
						Toast.makeText(TestActivity.this, "成功退出", Toast.LENGTH_LONG).show();
					}
				}
			});
			return true;

		default:
			return super.onKeyDown(keyCode, event);
		}
		
	}
}
