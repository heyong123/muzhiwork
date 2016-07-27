package mobi.zty.pay.test;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class Utils {
	/**
	 * 
	 * @description:获取系统版本号
	 * @return
	 * @return int
	 * @throws
	 */
	public static int getVersionCode(Context context) {
		int verCode = 0;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
		}
		return verCode;
	}

}
