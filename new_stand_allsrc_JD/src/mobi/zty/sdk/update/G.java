package mobi.zty.sdk.update;

import java.io.File;

import android.os.Environment;

public class G {
	/** 应用缓存目录 */
	public static final String CachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
			+ "Android/data/com.example.addemo/file/";
}
