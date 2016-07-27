package mobi.zty.pay.test;

import mobi.zty.mm.testpay.BuildConfig;
import mobi.zty.mm.testpay.R;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.sdk.game.ExitGameListener;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.GameSDKInitListener;
import mobi.zty.sdk.game.GameSDKPaymentListener;
import mobi.zty.sdk.util.Util_G;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;

import com.chinaMobile.MobileAgent;

/**
 * 电信支付不能在这里面测试  因为要改包名 所以最好把当前的 工程单做依赖库来进行测试
 * @author Administrator
 *
 */
public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_pay_layout);
		findViewById(R.id.testPay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 第一个参数是 money单位分  第二个参数是 计费点索引（如30000899288201传0） 第三个参数为监听器
				GameSDK.getInstance().startPay(1,3, "游戏激活", new GameSDKPaymentListener() {
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
		  
		 GameSDK.initSDK(this,"300009370275",new GameSDKInitListener() {//
			
			/**
			 * isOver 服务端响应配置信息成功，正常情况下可以支付了
			 * payWay 当前的支付类型
			 * openVoice 只有payWay是8MM基地的支付才有用，一般不需要理会
			 */
			@Override
			public void initOver(boolean isOver,int payWay,boolean openVoice) {
				
			}
			/**
			 * @param openDark 1是打开支付模糊描述 0正常状态不变
			 * @param openAlert 1:主动支付弹框打开 0主动弹框关闭
			 * @param openButton 0：按钮购买按钮 1：确认按钮 2：领取按钮
			 * @param openOurAlert 0：关闭二次确认框 1：打开二次确认框
			 * @param cootype 0:线上弹框逻辑 1线下
			 */
			@Override
			public void onOpenDark(int openDark,int openAlert,int openButton, int openOurAlert,int cootype) {
				Util_G.debugE("allpay-->>", "oppenDark="+openDark+",openAlert="+openAlert+",openButton="+
						openButton+",openOurAlert="+openOurAlert+",coottype="+cootype);
			}
			@Override
			public void upData(int state, String apkurl, String updatamsg,
					int updatatype, int remindtype) {
				// TODO Auto-generated method stub
				
			}

		});//这个0代表没有网络的情况下 默认走 移动MM白包
		 if (BuildConfig.DEBUG) {
				GameSDK.setDebug(true); 
			}
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
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
        	GameSDK.getInstance().exitGame(new ExitGameListener() {
    			
    			@Override
    			public void exitGame(boolean sure) {
    				if(sure){
    					TestActivity.this.finish();
    					System.exit(0);
    				  	android.os.Process.killProcess(android.os.Process.myPid());
    				}
    			}
    		});
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }

	
	/**
	 * 读取manifest.xml中application标签下的配置项，如果不存在，则返回空字符串
	 * 
	 * @param key
	 *            键名
	 * @return 返回字符串
	 */
	public static String getConfigString(Context context, String key) {
		String val = "";
		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			val =  appInfo.metaData.getString(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val==null?"":val.trim();
	}
	
}
