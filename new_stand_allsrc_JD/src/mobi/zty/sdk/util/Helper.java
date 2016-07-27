package mobi.zty.sdk.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import mobi.zty.pay.sdk.PayConfig;

public class Helper {

	public static boolean isDeBuge = false;//默认 为false
	public static boolean isAppOnForeground(Context ctx) {
		ActivityManager activityManager = (ActivityManager) ctx
				.getApplicationContext()//
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = ctx.getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null) {
			return false;
		}

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	public static String getBaseVersion(Context context) {

		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return "0.0.0";
	}

	public static int getVersionCode(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			int version = info.versionCode;
			return version;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return 1;
	}
	
	/**
	 * 获取当前版本名
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String versionName = info.versionName;
			return versionName;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	public static int getScreenDensity(Context paramContext) {
		try {
			DisplayMetrics localDisplayMetrics = new DisplayMetrics();
			((WindowManager) paramContext.getSystemService("window"))
					.getDefaultDisplay().getMetrics(localDisplayMetrics);
			int density = localDisplayMetrics.densityDpi;
			return density;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return 0;
	}

	/**
	 * context 杩欎釜涓�瀹氶挜鍖欎富activity涓殑涓婁笅鏂� (this)
	 * 
	 * @param context
	 * @return
	 */
	public static String getMainActivityName(Context context) {
		String classname = context.getClass().getName();
		return classname;
	}
	public static String getCarrierName(Context paramContext) {
		String str = "";
		try {
			str = ((TelephonyManager) paramContext.getSystemService("phone"))
					.getNetworkOperatorName();
			if (TextUtils.isEmpty(str)) {
				str = "";
			}
		} catch (Exception localException) {
			str = "";
		}
		return str;
	}

	public static String getAccessPoint(Context context) {
		String type = "unknown";
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();// 鑾峰彇缃戠粶鐨勮繛鎺ユ儏鍐�
			if (networkInfo != null) {
				if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					type = "wifi";
					return type;
				}
				if (networkInfo.getExtraInfo() != null) {
					return networkInfo.getExtraInfo().trim();
				}
			}

		}
		return type;
	}

	public static boolean isDebugEnv() {
		return isDeBuge;
	}
	public static void setDebug(Boolean var){
		isDeBuge = var;
	}

	public static String getChannel(Activity activity, String name)//
	{

		String ret = "";
		InputStream is = activity.getClass().getResourceAsStream("/mmiap.xml");
		if (Helper.isDebugEnv() && is == null) {
			return "2200166013";
		}
		if (is != null) {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = null;

			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Document doc = null;

			try {
				doc = builder.parse(is);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			doc.normalize();
			ret = doc.getElementsByTagName(name).item(0).getFirstChild()
					.getNodeValue();
		}
		return ret;
	}

	public static String getVesrionSdk() {
		return Build.VERSION.SDK;
	}

	public static String getMODEL() {
		return Build.MODEL;
	}

	/**
	 * 获取运营商类型 
	 * @param imsi
	 * @return 1移动卡5联通10电信15支付宝
	 */
	public static int getSIMType(Context context)// mmpay_tcpay
	{
	    String simOp = getIMSIStart(context);
		int cardType = 0;
		if ((simOp != null) ) {
			if ((simOp.indexOf("46000") >= 0) || (simOp.indexOf("46002") >= 0) || 
					(simOp.indexOf("46007") >= 0)||(simOp.indexOf("46020") >= 0) ) {
				cardType = PayConfig.CMCC_MOBLE;
			} else if (simOp.equals("46001")||(simOp.indexOf("46006") >= 0) ) {
				cardType = PayConfig.UNICOM_MOBLE;

			} else if (simOp.equals("46003") || simOp.equals("46005")
					|| simOp.equals("46011"))// "46003", "46005", "46011"
			{
				cardType = PayConfig.TIANYI_MOBLE;
			} else {
				cardType = PayConfig.BILL_TYPE_TAOBAO;
			}
		}else{
			cardType = PayConfig.BILL_TYPE_TAOBAO;
		}
		return cardType;

	}

	public static String getReleaseVersion() {
		return Build.VERSION.RELEASE;
	}

	public static String getIMEI(Context context)// getIMEI
	{
		String ret = ((TelephonyManager) context.getSystemService("phone"))
				.getDeviceId();
		return ret;
	}

	public static String getIMSI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSubscriberId();
	}
	
	/**
	 * 获取imsi卡的 类型标识
	 * @param context
	 * @return
	 */
	public static String getIMSIStart(Context context){
		TelephonyManager telmanager = (TelephonyManager)context.getSystemService("phone");
	    String simOp = telmanager.getSimOperator();
	    return simOp;
	}
	
	
	
	public static  boolean isNetworkConnected(Context context) {  
	  if (context != null) {  
		  ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                 .getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
		        if (mNetworkInfo != null) {  
	            return mNetworkInfo.isAvailable();  
	       }     
	  }  
	  return false;  
	}
	

	/**
	 * md5加密
	 * @param string
	 * @return
	 */
	public static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			char[] str = encodeHex(md, hexDigits);

			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将消息摘要转换成十六进制表示
	 * 
	 * @param data
	 * @param toDigits
	 * @return
	 */
	protected static char[] encodeHex(byte[] data, char[] toDigits) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}
	
	/**
	 * sha1加密
	 * @param value
	 * @param key
	 * @param signtype
	 * @return
	 */
	public static byte[] hmac_sha1(String value, String key,String signtype) {
		try {
			byte[] keyBytes = key.getBytes();
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, signtype);

			Mac mac = Mac.getInstance(signtype);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(value.getBytes());
			return rawHmac;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	  * 获取现在时间
	  * 
	  * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	  */
	public static String getNowTime() {
	  Date currentTime = new Date();
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  String dateString = formatter.format(currentTime);
	  return dateString;
	}
	
	public static String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext()
					.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
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
			val = appInfo.metaData.getString(key);
			if (val == null) {
				Util_G.debugE("Helper", "please set config value for " + key
						+ " in manifest.xml first");
				val = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}
	
	
	 /**
     * 字符串转json对象
     *
     * @param str   string
     * @param split split
     * @return JSONObject
     */
    public static JSONObject string2JSON(String str, String split) {

        JSONObject json = new JSONObject();
        try {
            String[] arrStr = str.split(split);
            for (String anArrStr : arrStr) {
                String[] arrKeyValue = anArrStr.split("=");
                json.put(arrKeyValue[0],
                        anArrStr.substring(arrKeyValue[0].length() + 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
    
    public static interface Callback {
		public void onResult(String info);
	}
    /**
	 * @param url
	 * @param imsi
	 * @return
	 */
	public synchronized static void requestPayResult(CharSequence url, String orderID,final Callback cb) {
		final String urlstr = url+ "?order=" + orderID;
		new Thread() {
			@Override
			public void run() {
				String result = null;
				HttpGet httpGet = new HttpGet(urlstr);
				BasicHttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,
						10 * 60 * 1000);
				HttpConnectionParams.setSoTimeout(httpParams, 10 * 60 * 1000);
				int step = 0;
				int ret =-1;
				while (ret<0&&step<40){
					try {
						Util_G.debugE("支付宝支付L", urlstr);
						HttpClient client = new DefaultHttpClient(httpParams);
						HttpResponse httpResp = client.execute(httpGet);
						if (httpResp.getStatusLine().getStatusCode() == 200) {
							result = EntityUtils.toString(httpResp.getEntity(),
									"UTF-8");
							if(!(result.contains("DOCTYPE HTML")||result.contains("doctype html"))){
								
								try {
									JSONObject resutJs = new JSONObject(result);
									ret = resutJs.getInt("ret");//1代表成 0代表失败-1代表后台还未收到结果
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} 
					//	what = httpResp.getStatusLine().getStatusCode();
						Log.e("httpResp", httpResp.getStatusLine()
								.getStatusCode() + "");

					} catch (ConnectTimeoutException e) {
						result = e.getMessage();

					} catch (ClientProtocolException e) {
						e.printStackTrace();
						result = e.getMessage();
					} catch (IOException e) {
						e.printStackTrace();
						result = e.getMessage();
					}
					try {
						Thread.sleep(2000);
						step++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (ret <0) {
					result = "{ret:0}";
				}
				Util_G.debugE("服务端通知的结果", result);
				cb.onResult(result);
				
			}
		}.start();
	}
}
