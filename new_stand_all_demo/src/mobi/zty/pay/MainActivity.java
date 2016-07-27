package mobi.zty.pay;

import com.mzyw.sgkptt.m4399.BuildConfig;
import com.mzyw.sgkptt.m4399.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.sdk.game.ExitGameListener;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.GameSDKInitListener;
import mobi.zty.sdk.game.GameSDKPaymentListener;
import mobi.zty.sdk.util.Util_G;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		findViewById(R.id.testPay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				sendSms(null, null);
				GameSDK.getInstance().startPay(1, 6, "每日签到", new GameSDKPaymentListener() {
					@Override
					public void onPayFinished(int mount) {
						float v = mount;
						v = v / 100;
						String str = String.format("支付%2f元成功", v);
						GameSDK.getInstance().makeToast(str);
					}
					@Override
					public void onPayFail(PayResultInfo info) {
						Util_G.debugE("mainActivity", "通知CP处理失败的结果");
						GameSDK.getInstance().makeToast(info.retMsg);
					}
					@Override
					public void onPayCancelled() {
						GameSDK.getInstance().makeToast("操作取消");
					}
					
				});
			}
		});
		
		GameSDK.initSDK(this,"300009296278",new GameSDKInitListener() {//300009296278
			@Override
			public void initOver(boolean arg0, int arg1, boolean arg2) {
				
			}

			@Override
			public void onOpenDark(int openDark, int openAlert, int openButton,
					int openOurAlert, int cootype) {
			}

			@Override
			public void upData(int state, String apkurl, String updatamsg,
					int updatatype, int remindtype) {
				// TODO Auto-generated method stub
				
			}
		},"",MyApplication.payWay);//初始化
		
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GameSDK.getInstance().exitGame(new ExitGameListener() {
				
				@Override
				public void exitGame(boolean sure) {
					//这里您直接退出游戏就好
					MainActivity.this.finish();
					System.exit(0);
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			});
		
//			// 电信爱游戏特殊处理
//			this.runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					Dialog dDialog = new AlertDialog.Builder(MainActivity.this)
//							.setPositiveButton("继续退出", new Dialog.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									eixtDX();
//								}
//							})
//							.setNegativeButton("更多游戏", new Dialog.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									CheckTool.more(MainActivity.this);
//								}
//							}).setCancelable(false).create();
//					if (!dDialog.isShowing() && dDialog != null) {
//						dDialog.show();
//					} else {
//						eixtDX();
//					}
//				}
//			});
		}
         return super.onKeyDown(keyCode, event);
     }

//	private  void eixtDX() {
//		CheckTool.exit(MainActivity.this, new ExitCallBack() {
//
//			@Override
//			public void exit() {
//				// 退出游戏操作
//				MainActivity.this.finish();
//				System.exit(0);
//				android.os.Process.killProcess(android.os.Process.myPid());
//			}
//
//			@Override
//			public void cancel() {
//				// 取消退出，返回游戏
//
//			}
//		});
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		GameSDK.getInstance().onActivityResult(requestCode, resultCode, data);;
	}

}
