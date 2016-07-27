package mobi.zty.sdk.util;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.format.Time;

public class TimeSharedPreferences {
	private  SharedPreferences sp;
	private  void SavaUsersTime(Context context,long time){
		sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putLong("time", time);
		editor.commit();
		
		
	}

	public  boolean IsMutilLogin(Context context){
		sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
		long time = sp.getLong("time", 0);
		long nowtime = System.currentTimeMillis()-time;
		if(time==0){//需要提醒，返回true
			SavaUsersTime(context, System.currentTimeMillis());
			return true;
		}else {
			if(nowtime > 24*3600*1000){
				return true;
			}else{
				return false;
			}
		}
	}
}
