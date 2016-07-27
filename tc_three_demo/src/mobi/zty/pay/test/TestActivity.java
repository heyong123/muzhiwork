package mobi.zty.pay.test;

import com.centurysoft.backkom.three.BuildConfig;
import com.centurysoft.backkom.three.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import cn.egame.terminal.paysdk.EgameExitListener;
import cn.egame.terminal.paysdk.EgamePay;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.GameSDKInitListener;
import mobi.zty.sdk.game.GameSDKPaymentListener;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.test_pay_layout);
		findViewById(R.id.testPay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//支付接口
				//pram1：金额单位分、pram2：索引（参照计费文件）、pram3：道具名称
				GameSDK.getInstance().startPay(200,10, "22钻石", new GameSDKPaymentListener() {
					
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
				});
			}
		});
		
		//初始化
		//pram1:上下文 、pram2：游戏计费唯一标识
		GameSDK.initSDK(this,"300009370275", new GameSDKInitListener() {
			
			@Override
			public void initOver(boolean arg0, int arg1, boolean arg2) {
				
			}

			@Override
			public void onOpenDark(int arg0, int arg1, int arg2, int arg3,
					int arg4) {
			}
		});//初始化sdk
		
		if (BuildConfig.DEBUG) {
			GameSDK.setDebug(true);
		}
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {//退出游戏
			// 电信爱游戏特殊处理
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Dialog dDialog = new AlertDialog.Builder(TestActivity.this)
							.setPositiveButton("继续退出", new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									eixtDX();
								}
							})
							.setNegativeButton("更多游戏", new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									 EgamePay.moreGame(TestActivity.this);
								}
							}).setCancelable(false).create();
					if (!dDialog.isShowing() && dDialog != null) {
						dDialog.show();
					} else {
						eixtDX();
					}
				}
			});
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private  void eixtDX() {
		
		 EgamePay.exit(TestActivity.this, new EgameExitListener() {
			
			@Override
			public void exit() {
				// 退出游戏操作
				TestActivity.this.finish();
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			@Override
			public void cancel() {
			}
		});
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
}
