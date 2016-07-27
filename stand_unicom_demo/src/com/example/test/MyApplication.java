package com.example.test;

import android.app.Application;

import com.unicom.dcLoader.Utils;
import com.unicom.dcLoader.Utils.UnipayPayResultListener;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		Utils.getInstances().initSDK(this, new UnipayPayResultListener() {
			
			@Override
			public void PayResult(String arg0, int arg1, int arg2, String arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}	
}
