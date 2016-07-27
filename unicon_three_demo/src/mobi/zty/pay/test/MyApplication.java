package mobi.zty.pay.test;

import android.app.ActivityManager;

import android.content.Context;

import android.util.Log;



import com.unicom.shield.UnicomApplicationWrapper;


public class MyApplication extends UnicomApplicationWrapper {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		/*
		 * 因为计费安全服务运行于独立进程，会导致本application oncreate方法调用两次
		 * 参考以下代码，将Application 必要的初始化代码放置于条件语句内。
		 * */
//		String processName = getCurProcessName(this);

		Log.i("MyApplication", "[unipay] call unipay sdk init"); 
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
