package mobi.zty.pay;


import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.sdk.util.Helper;

import com.unicom.dcLoader.Utils;
//import com.unicom.dcLoader.Utils.UnipayPayResultListener;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyApplication extends Application {

	public static String payWay = "0";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		/*
		 * 因为计费安全服务运行于独立进程，会导致本application oncreate方法调用两次
		 * 参考以下代码，将Application 必要的初始化代码放置于条件语句内。
		 * */
		String processName = getCurProcessName(this);
		if(processName.equals(this.getPackageName())){
			switch (getSIMType(getApplicationContext())) {
			case 1:{//移动卡
				payWay = "0";
//				System.loadLibrary("megjb");//加载游戏基地的so库
				com.secneo.mmb.Helper.install(this);
			}
				
				break;
			case 5:{//联通卡
				payWay = "5";
				com.unicom.dcLoader.Utils.getInstances().initSDK(this, new com.unicom.dcLoader.Utils.UnipayPayResultListener() {
					
					@Override
					public void PayResult(String arg0, int arg1, int arg2, String arg3) {
						// TODO Auto-generated method stub
						
					}
				});

			}
				
				break;
			case 10:{//电信卡
				payWay = "10";
			}
				break;
			default:
				break;
			}
			
		}
		Log.i("MyApplication", "[unipay] call unipay sdk init");
	}

	/**
	 * 获取运营商类型 
	 * @param imsi
	 * @return 1移动 5联通10电信15支付宝
	 */
	public static int getSIMType(Context context)// mmpay_tcpay
	{
		TelephonyManager telmanager = (TelephonyManager)context.getSystemService("phone");
	    String simOp = telmanager.getSimOperator();
		int cardType = 0;
		if ((simOp != null) ) {
			if ((simOp.indexOf("46000") >= 0) || (simOp.indexOf("46002") >= 0) || 
					(simOp.indexOf("46007") >= 0)||(simOp.indexOf("46020") >= 0) ) {
				cardType = 1;
			} else if (simOp.equals("46001")||(simOp.indexOf("46006") >= 0) ) {
				cardType = 5;

			} else if (simOp.equals("46003") || simOp.equals("46005")
					|| simOp.equals("46011"))// "46003", "46005", "46011"
			{
				cardType = 10;
			} else {
				cardType = 15;
			}
		}else{
			cardType = 15;
		}
		return cardType;

	}
	/**
	 * 获取调用onCreate的进程的processName。
	 * @param context
	 * @return
	 */
	String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return "";
	}
}
