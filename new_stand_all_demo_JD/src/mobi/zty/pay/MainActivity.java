package mobi.zty.pay;


import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.sdk.game.ExitGameListener;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.GameSDKInitListener;
import mobi.zty.sdk.game.GameSDKPaymentListener;
import mobi.zty.sdk.util.Util_G;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.muzhi.parkout.xyamx0121.BuildConfig;
import com.muzhi.parkout.xyamx0121.R;

public class MainActivity extends Activity {
	private int index = 0;
	private EditText ed;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		
		ed = (EditText) findViewById(R.id.testedit);
		findViewById(R.id.testPay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!(ed.getText().toString()).equals("")){
					index = Integer.parseInt(ed.getText().toString());
					GameSDK.getInstance().startPay(1,index, "星座礼包", new GameSDKPaymentListener() {
					
					@Override
					public void onPayFinished(int mount) {
						float v = mount;
						v = v / 100;
						String str = String.format("支付%2f元成功", v);
						GameSDK.getInstance().makeToast(str);
					}
					@Override
					public void onPayFail(PayResultInfo info) {
						GameSDK.getInstance().makeToast(info.retMsg);
					}
					@Override
					public void onPayCancelled() {
						GameSDK.getInstance().makeToast("支付取消");
					}
				},"16");
			}else{
				Toast.makeText(getApplicationContext(), "请输入商品索引", 1000).show();
			}
		}
		});
		
		GameSDK.initSDK(this,"300009189686",new GameSDKInitListener() {
			
			
			@Override
			public void initOver(boolean isOver, int payWay, boolean openVoice) {
				
			}

			/**
			 * openDark 游戏中支付描述是否模糊 0否 1是
			 * openAlert 游戏中是否主动弹出支付框 0否 1是
			 * openButton 游戏中支付提示按钮类型 0 购买 1确定 2领取
			 * openOurAlert 游戏中是否弹出自己的二次确认框 0否1是
			 * cootype 0线上 1线下
			 */
			@Override
			public void onOpenDark(int openDark, int openAlert,
					int openButton, int openOurAlert ,int cootype) {
//				CppBridge.NotifyPayType(openDark,openAlert,openButton,openOurAlert,cootype);
				Util_G.debugE("Alert-->>", openAlert+"");
			}

			@Override
			public void upData(int state, String apkurl, String updatamsg,
					int updatatype, int remindtype) {
				// TODO Auto-generated method stub
				Util_G.debugE("updata-->>", "stata:"+state+""+"apkurl:"+apkurl);
				if(state == 1){//表示游戏版本需要更新
					if(remindtype == 1){
						Util_G.debugE("update_remind", "默认提醒，进入游戏不弹下载 框，CP自己提醒用户需要更新");
					}else if(remindtype == 0){
						GameSDK.getInstance().updataApk(apkurl, updatamsg, updatatype+"");
					}
				}

			}

		},"",MyApplication.payWay,Utils.getVersionCode(this)+"");//初始化
//		GameSDK.getInstance().updataApk("asdfghdfhj", "hello", 1+"");
		if (BuildConfig.DEBUG) {
			GameSDK.setDebug(true);
		}
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		GameSDK.getInstance().onResume();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		GameSDK.getInstance().onPause();
		super.onPause();
	}
	
	public void updata(){
		GameSDK.getInstance().updataApk("", "", "");
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GameSDK.getInstance().exitGame(new ExitGameListener() {
				
				@Override
				public void exitGame(boolean sure) {
					if (sure) {
						MainActivity.this.finish();
					  	System.exit(0);
					  	android.os.Process.killProcess(android.os.Process.myPid());//必须把进程杀死 不然onpause 会报空
					}
				}
			});
			return false;
		}
         return super.onKeyDown(keyCode, event);
     }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		GameSDK.getInstance().onActivityResult(requestCode, resultCode, data);
	}
}
