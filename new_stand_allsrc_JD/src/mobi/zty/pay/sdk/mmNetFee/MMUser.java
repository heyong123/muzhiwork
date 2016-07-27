package mobi.zty.pay.sdk.mmNetFee;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Random;

import mobi.zty.sdk.util.DeviceInfoUtil;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.Rsa;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

public class MMUser {
	private String mac = "";
	private String imsi = "";
	private String deviceId = "";
	private long event1_sendTime = 1422511917327L;// System.currentTimeMillis();//1422511917327L;
	private long event1_logTime = 1422511917195L;// 比sendTime少100毫秒
	private long act_sendTime = 1422511918591L;// System.currentTimeMillis();//1422511917327L;
	private long act_logTime = 1422511917320L;
	private long sys_sendTime = 1422511917320L;
	private long event2_sendTime = 1422511919015L;
	private long event2_logTime = 1422511917992L;
	long playTime = 0;

	String appKey = "300008844659";// "300008813481";//"300008702230";
	String appid = "300008844659";// appid和appkey一样
	public String packageName = "";
	public String MainActivity = "";
	private String versionName = "";
	private int versionCode = 1;
	String sdkVersion = "3.1.1";
	String protocolVersion = "3.1.0";
	String cpuRatioCur = "1728000";
	String menoryRatio = "1893832";
	String cpuRatioMax = "2265600";

	private String model = "";// 手机型号lg:MI 3
	private String deviceDetail = "";
	private String manufacturer = "";
	private String OsVersion = "";// 系统版本号
	private String phoneOs = "";
	private String resolution = "";// 手机 宽*高
	private int screenHeight = 480;
	private int screenWidth = 480;
	private String MMversionName = "1.2.3";
	private String channel = "";
	private String accessPoint = "";// 访问的是wifi还是啥
	private String countryCode = "CN";
	private int screenDensity = 0;
	private String languageCode = "zh";
	private String carrierName = "China+Mobile";
	String logJsonAry = "";
	String cid = "7f9c9fae657f9ebe4a";
	public String sid = "";

	public MMUser(String imei, String imsi, String mac, String strChannelID,
			String appkey) {
		this.mac = mac;
		if(mac==null||((mac.trim()).equals("")))
		this.mac = String.format("%c%c:%c%c:%c%c:%c%c:%c%c:%c%c",getChar(),getChar(),getChar(),getChar(),getChar()
			,getChar(),getChar(),getChar(),getChar(),getChar(),getChar(),getChar());
		 
		this.imsi = imsi;
		this.deviceId = imei;
		this.appKey = appkey;
		this.appid = appkey;
		this.channel = strChannelID;
		event1_sendTime = System.currentTimeMillis();
		event1_logTime = event1_sendTime - genRan(10, 20);

		act_sendTime = event1_sendTime + genRan(400, 500);
		act_logTime = event1_sendTime - genRan(2, 10);

		sys_sendTime = event1_sendTime + genRan(30, 40);

		event2_sendTime = System.currentTimeMillis() + genRan(1000, 1400);
		event2_logTime = event1_sendTime + genRan(300, 400);

		/*
		 * event1_sendTime = 1422511917327L; event1_logTime = 1422511917195L;
		 * 
		 * act_sendTime = 1422511918591L; act_logTime = 1422511917320L;
		 * 
		 * sys_sendTime = 1422511917406L;
		 * 
		 * event2_sendTime = 1422511919015L; event2_logTime = 1422511917992L;
		 */

		cid = String
				.valueOf(Rsa.getMD5(replace(deviceId)).toCharArray(), 7, 18);
		int i1 = new Random().nextInt(10);
		sid = String.valueOf(
				Rsa.getMD5(
						System.currentTimeMillis() + appKey + replace(deviceId)/*
																				 * g
																				 * .
																				 * a
																				 * (
																				 * paramContext
																				 * ,
																				 * deviceId
																				 * )
																				 */
								+ mac + i1).toCharArray(), 8, 16);// "f9585dbed037d81c";
	}

	// 常规启动初始化,
	public void doact(String sid) {
		event1_sendTime = System.currentTimeMillis();
		event1_logTime = event1_sendTime - genRan(10, 20);
		this.sid = sid;

		String logJson1 = "_pay_init";
		String logJson2_2 = appid + "@@" + packageName + "@@" + versionName
				+ "@@" + "0000000000@@99999999" + "@@" + imsi + "@@" + deviceId
				+ "@@" + model + "@@" + OsVersion + "@@" + resolution
				+ "@@SMS@@@@" + "@@" + MMversionName + "@@"
				+ event1_logTime / 1000 + "@@" + event1_logTime / 1000 + "@@"
				+ "1";
		String logJson2 = channel + "@@" + imsi + "@@" + manufacturer + "@@"
				+ model + "@@" + phoneOs + "@@" + mac + "@@" + accessPoint
				+ "@@" + resolution + "@@" + URLEncoder.encode(logJson2_2);
		logJson2 = URLEncoder.encode(logJson2);
		String logJson3 = "0";
		String logJson4 = event1_logTime + "\n";// 1422511917195

		logJsonAry = logJson1 + "|" + logJson2 + "|" + logJson3 + "|"
				+ logJson4;
		posteventlog(event1_sendTime, logJsonAry);

	}

	// 在线时长,
	public void doacttime(String sid) {
		playTime = genRan(60516, 65324);
		act_sendTime = event1_sendTime + playTime + 231;
		act_logTime = act_sendTime - playTime;
		postactlog();
	}

	public static char getChar() {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
				'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
				'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z' };
		int index = genRan(0, hexDigits.length - 1);
		return hexDigits[index];
	}

	public static String replace(String paramString) {
		if (paramString == null) {
			return "";
		}
		if (paramString.length() > 30) {
			paramString = paramString.substring(0, 29);
		}
		return paramString.replace("\\", "").replace("|", "");
	}

	public void initData(Context context) {
		this.packageName = context.getPackageName();
		this.versionName = Helper.getBaseVersion(context);
		this.versionCode = Helper.getVersionCode(context);
		this.accessPoint = Helper.getAccessPoint(context);
		this.carrierName = Helper.getCarrierName(context);
		this.screenDensity = Helper.getScreenDensity(context);
		
		this.model = Build.MODEL;
		this.deviceDetail = Build.MODEL;
		this.manufacturer = Build.MANUFACTURER;
		this.OsVersion = Build.VERSION.RELEASE;
		this.phoneOs = "android+" + OsVersion;
		this.MainActivity = Helper.getMainActivityName(context);
		this.screenWidth = DeviceInfoUtil.getDeviceInfo(context)
				.getScreenWidth();
		this.screenHeight = DeviceInfoUtil.getDeviceInfo(context).getScreenHeight();
		this.resolution = this.screenWidth+ "*"+ this.screenHeight;
		this.countryCode = Locale.getDefault().getCountry();
		this.languageCode = Locale.getDefault().getLanguage();
	}

	public void initUser() {
		posteventlog1();

		/*
		 * try { Thread.sleep(act_sendTime-event1_sendTime); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		postactlog();

		/*
		 * try { Thread.sleep(sys_sendTime-event1_sendTime); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		postsyslog();

		/*
		 * try { Thread.sleep(event2_sendTime-event1_sendTime); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		posteventlog2();

		doacttime(sid);

	}

	// 初始化第一步
	public void posteventlog1() {
		// sid = "";
		// cid = "7f9c9fae657f9ebe4a";
		// long event1_sendTime = 1422511917327L;//
		// System.currentTimeMillis();//1422511917327L;
		// long event1_logTime = 1422511917195L;//比sendTime少100毫秒

		String logJson1 = "_pay_init";
		String logJson2_2 = appid + "@@" + packageName + "@@" + versionName
				+ "@@" + "0000000000@@99999999" + "@@" + imsi + "@@" + deviceId
				+ "@@" + model + "@@" + OsVersion + "@@" + resolution
				+ "@@SMS@@@@" + "@@" + MMversionName + "@@"
				+ event1_logTime / 1000 + "@@" + event1_logTime / 1000 + "@@"
				+ "1";
		String logJson2 = channel + "@@" + imsi + "@@" + manufacturer + "@@"
				+ model + "@@" + phoneOs + "@@" + mac + "@@" + accessPoint
				+ "@@" + resolution + "@@" + URLEncoder.encode(logJson2_2);
		logJson2 = URLEncoder.encode(logJson2);
		String logJson3 = "0";
		String logJson4 = event1_logTime + "\n";// 1422511917195

		logJsonAry = logJson1 + "|" + logJson2 + "|" + logJson3 + "|"
				+ logJson4;
		posteventlog(event1_sendTime, logJsonAry);
	}

	// 第二步激活数据
	public void postactlog() {
		try {
			String str = "";
			String strUrl = "http://da.mmarket.com/mmsdk/mmsdk?func=mmsdk:postactlog&appkey="
					+ appKey + "&channel=" + channel + "&code=105";

			// long act_sendTime =1422511918591L;//
			// System.currentTimeMillis();//1422511917327L;
			// long act_logTime = 1422511917320L;

//			// String logJsonAry = "";
//			String MainActivity = "mobi.shoumeng.game.demo.MainActivity";

			int i1 = new Random().nextInt(10);
			// String paramContext="";
			// sid = String.valueOf(Rsa.getMD5(System.currentTimeMillis() + appKey +
			// replace(deviceId)/*g.a(paramContext,deviceId)*/ + mac +
			// i1).toCharArray(), 8, 16);//"f9585dbed037d81c";

			// sid = "f9585dbed037d81c";

			JSONObject logJson = new JSONObject();
			int flowConsumpRev = 0;
			int flowConsumpSnd = 0;
			logJson.put("flowConsumpRev", flowConsumpRev);
			logJson.put("flowConsumpSnd", flowConsumpSnd);
			logJson.put("sid", sid);
			logJson.put("logs", "onResume|" + MainActivity + "|" + act_logTime
					+ "|" + playTime + "|\n");
			logJsonAry = "[" + logJson.toString() + "]";

			JSONObject reqjson = new JSONObject();
			reqjson.put("sid", sid);
			reqjson.put("packageName", packageName);
			reqjson.put("sendTime", act_sendTime);
			reqjson.put("versionCode", 1);
			reqjson.put("mac", mac);
			reqjson.put("pid", 1);
			reqjson.put("versionName", versionName);
			reqjson.put("cid", cid);
			reqjson.put("phoneOs", URLEncoder.encode(phoneOs,"UTF-8"));
			reqjson.put("sdkVersion", sdkVersion);
			reqjson.put("cpuRatioCur", cpuRatioCur);
			reqjson.put("logJsonAry", logJsonAry);
			reqjson.put("protocolVersion", protocolVersion);
			reqjson.put("menoryRatio", menoryRatio);
			reqjson.put("cpuRatioMax", cpuRatioMax);
			//
			
			reqjson.put("manufacturer", URLEncoder.encode(replace(manufacturer),"UTF-8"));
			reqjson.put("accessPoint", accessPoint);
			reqjson.put("deviceDetail", URLEncoder.encode(replace(deviceDetail),"UTF-8"));
			reqjson.put("channel", channel);
			reqjson.put("deviceId", deviceId);
			reqjson.put("appKey", appKey);

			System.out.println("strUrl=" + strUrl);

			str = reqjson.toString();
			System.out.println("str=" + str);

			d td = d.a();
			byte[] bytes = td.a(str);
			System.out.println("strBytes=="+byteToStr2 (bytes));

			try {
				String rsq = URLPostUTF8(strUrl, bytes);
				System.out.println("rsq" + rsq);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 public static String  byteToStr2(byte[] bts)
	    {
	    	String str = "";
	    	for(byte bt:bts)
	    	{
	    		String t = String.format("%d,", bt);
	    		str += t;
	    	}
	    	return str;
	    }
	//第三步系统消息
    public  void postsyslog()
    {
    	try {
			String str = "";
			
			String strUrl = "http://da.mmarket.com/mmsdk/mmsdk?func=mmsdk:postsyslog&appkey="+appKey+"&channel="+channel+"&code=105";
			//"http://da.mmarket.com/mmsdk/mmsdk?func=mmsdk:postsyslog&appkey=300008702230&channel=0000000000&code=105";
			
			JSONObject reqjson = new JSONObject();
			reqjson.put("screenHeight",screenHeight);
			reqjson.put("packageName",packageName);
			reqjson.put("sendTime",sys_sendTime);
			reqjson.put("countryCode",countryCode);
			reqjson.put("versionCode",versionCode);//
			reqjson.put("pid",1);
			reqjson.put("mac",mac);
			reqjson.put("versionName",versionName);
			reqjson.put("phoneOS",URLEncoder.encode(phoneOs,"UTF-8"));
			reqjson.put("cid",cid);
			reqjson.put("screenDensity",screenDensity);
			reqjson.put("sdkVersion",sdkVersion);
			reqjson.put("languageCode",languageCode);
			reqjson.put("protocolVersion",protocolVersion);
			reqjson.put("screenWidth",screenWidth);
			reqjson.put("manufacturer",URLEncoder.encode(replace(manufacturer),"UTF-8"));
			reqjson.put("carrierName",URLEncoder.encode(carrierName,"UTF-8"));
			reqjson.put("accessPoint",accessPoint);
			reqjson.put("imsi",imsi);
			reqjson.put("deviceDetail",URLEncoder.encode(replace(deviceDetail),"UTF-8"));
			reqjson.put("channel",URLEncoder.encode(channel,"UTF-8"));
			reqjson.put("deviceId",deviceId);
			reqjson.put("appKey",appKey);
			//{"screenHeight":1920,"packageName":"com.pansen.zumalocal","sendTime":1422511917406,"countryCode":"CN","versionCode":1,"pid":1,"mac":"d4:97:0b:69:c7:07","versionName":"1.0","phoneOS":"android+4.4.4","cid":"7f9c9fae657f9ebe4a","screenDensity":480,"sdkVersion":"3.1.1","languageCode":"zh","protocolVersion":"3.1.0","screenWidth":1080,"manufacturer":"Xiaomi","carrierName":"China+Mobile","accessPoint":"wifi","imsi":"460029140953268","deviceDetail":"MI+3C","channel":"0000000000","deviceId":"865903029745048","appKey":"300008702230"}
			//{"screenHeight":1920,"packageName":"com.pansen.zumalocal","sendTime":1422511917406,"countryCode":"CN","versionCode":1,"pid":1,"mac":"d4:97:0b:69:c7:07","versionName":"1.0","phoneOs":"android+4.4.4","cid":"7f9c9fae657f9ebe4a","screenDensity":480,"sdkVersion":"3.1.1","languageCode":"zh","protocolVersion":"3.1.0","screenWidth":1080,"manufacturer":"Xiaomi","carrierName":"China+Mobile","accessPoint":"wifi","imsi":"460029140953268","deviceDetail":"MI+3C","channel":"0000000000","deviceId":"865903029745048","appKey":"300008702230"}

			System.out.println("strUrl="+strUrl);
			
			str = reqjson.toString();
			System.out.println("str="+str);
			int index = 0;
			byte last=0;
			byte next=0;
			byte cur=0;
			
//			System.out.println(byteToStr (str.getBytes()));//////
			d td= d.a();
			byte [] bytes =td.a(str);
			System.out.println("strBytes=="+byteToStr2 (bytes));
			
			try {
				String rsq = URLPostUTF8(strUrl, bytes);
				System.out.println("rsq"+rsq);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    //初始化第四步
    public  void posteventlog2()
    {
    	//sid = "f9585dbed037d81c";
    	//cid = "7f9c9fae657f9ebe4a";
    	//long sendTime =  1422511919015L;// System.currentTimeMillis();//1422511917327L;
    	//long beginTime = 1422511917992L;
    	
    	//long event2_sendTime =  1422511919015L;// System.currentTimeMillis();//1422511917327L;
    	//long event2_logTime = 1422511917992L;//比sendTime少100毫秒
    	
    	
    	String logJson1="#applist";
    	logJson1 = URLEncoder.encode(logJson1);
    	//String logJson2_2 = "";
    	String logJson2 = URLEncoder.encode("微信")+"@@com.tencent.mm@@6.0.2.57_r966533";;//"微信@@com.tencent.mm@@6.0.2.57_r966533@@##新浪体育@@cn.com.sina.sports@@2.8.1.2@@##WPS Office@@cn.wps.moffice_eng@@6.5@@##小米商城@@com.xiaomi.shop@@3.0.20150109@@##自选股@@com.tencent.portfolio@@3.7.1@@##手机管家@@com.tencent.qqpimsecure@@5.3.0.2960mini@@##米聊@@com.xiaomi.channel@@1.0.1167@@##携程旅行@@ctrip.android.view@@6.1.2@@##语音设置@@com.iflytek.speechcloud@@1.0.10051@@##山水手环@@com.ble@@2.0.1@@##平安口袋银行@@com.pingan.pabank.activity@@2.2.1@@##QQ空间@@com.qzone@@5.0.2.188@@##百度地图@@com.baidu.BaiduMap@@7.8.0@@##应用宝@@com.tencent.android.qqdownloader@@5.0.0@@##Demo@@com.pansen.zumalocal@@1.0@@##炒股公开课@@com.hexin.openclass@@3.0.1@@##QQ浏览器@@com.tencent.mtt@@5.6.1.1301@@##高德地图@@com.autonavi.minimap@@7.1.3.572@@##碰碰贴@@com.xiaomi.tag@@2.0.4@@##smsTest@@com.example.smstest@@1.0@@##58同城@@com.wuba@@5.8.2.0@@##财经证券新闻@@com.eastmoney.android.tokyo@@1.3@@##百度输入法小米版@@com.baidu.input_mi@@4.1.4.29@@##多看阅读@@com.duokan.reader@@3.3.5@@##星星祖玛@@com.pansen.zumalocal.lenovo@@1.5.0@@##QQ@@com.tencent.mobileqq@@5.3.1@@##豌豆荚连接服务@@com.wandoujia.phoenix2.usbproxy@@3.52.1@@##乐商店@@com.lenovo.leos.appstore@@6.15.10.88@@##微博@@com.sina.weibo@@5.0.0@@##豌豆荚@@com.wandoujia.phoenix2@@4.18.1@@##后台@@com.zty.ompic25@@1.0.1@@##酷狗音乐@@com.kugou.android@@7.0.9@@##招商银行@@cmb.pb@@3.0.2@@##";//channel+"@@"+imsi+"@@"+manufacturer+"@@"+model+"@@"+phoneOs+"@@"+mac+"@@"+accessPoint+"@@"+resolution+"@@"+URLEncoder.encode(logJson2_2);
    	logJson2 = URLEncoder.encode(logJson2);
    	logJson2="%25E5%25BE%25AE%25E4%25BF%25A1%40%40com.tencent.mm%40%406.0.2.57_r966533%40%40%23%23%25E6%2596%25B0%25E6%25B5%25AA%25E4%25BD%2593%25E8%2582%25B2%40%40cn.com.sina.sports%40%402.8.1.2%40%40%23%23WPS%2BOffice%40%40cn.wps.moffice_eng%40%406.5%40%40%23%23%25E5%25B0%258F%25E7%25B1%25B3%25E5%2595%2586%25E5%259F%258E%40%40com.xiaomi.shop%40%403.0.20150109%40%40%23%23%25E8%2587%25AA%25E9%2580%2589%25E8%2582%25A1%40%40com.tencent.portfolio%40%403.7.1%40%40%23%23%25E6%2589%258B%25E6%259C%25BA%25E7%25AE%25A1%25E5%25AE%25B6%40%40com.tencent.qqpimsecure%40%405.3.0.2960mini%40%40%23%23%25E7%25B1%25B3%25E8%2581%258A%40%40com.xiaomi.channel%40%401.0.1167%40%40%23%23%25E6%2590%25BA%25E7%25A8%258B%25E6%2597%2585%25E8%25A1%258C%40%40ctrip.android.view%40%406.1.2%40%40%23%23%25E8%25AF%25AD%25E9%259F%25B3%25E8%25AE%25BE%25E7%25BD%25AE%40%40com.iflytek.speechcloud%40%401.0.10051%40%40%23%23%25E5%25B1%25B1%25E6%25B0%25B4%25E6%2589%258B%25E7%258E%25AF%40%40com.ble%40%402.0.1%40%40%23%23%25E5%25B9%25B3%25E5%25AE%2589%25E5%258F%25A3%25E8%25A2%258B%25E9%2593%25B6%25E8%25A1%258C%40%40com.pingan.pabank.activity%40%402.2.1%40%40%23%23QQ%25E7%25A9%25BA%25E9%2597%25B4%40%40com.qzone%40%405.0.2.188%40%40%23%23%25E7%2599%25BE%25E5%25BA%25A6%25E5%259C%25B0%25E5%259B%25BE%40%40com.baidu.BaiduMap%40%407.8.0%40%40%23%23%25E5%25BA%2594%25E7%2594%25A8%25E5%25AE%259D%40%40com.tencent.android.qqdownloader%40%405.0.0%40%40%23%23Demo%40%40com.pansen.zumalocal%40%401.0%40%40%23%23%25E7%2582%2592%25E8%2582%25A1%25E5%2585%25AC%25E5%25BC%2580%25E8%25AF%25BE%40%40com.hexin.openclass%40%403.0.1%40%40%23%23QQ%25E6%25B5%258F%25E8%25A7%2588%25E5%2599%25A8%40%40com.tencent.mtt%40%405.6.1.1301%40%40%23%23%25E9%25AB%2598%25E5%25BE%25B7%25E5%259C%25B0%25E5%259B%25BE%40%40com.autonavi.minimap%40%407.1.3.572%40%40%23%23%25E7%25A2%25B0%25E7%25A2%25B0%25E8%25B4%25B4%40%40com.xiaomi.tag%40%402.0.4%40%40%23%23smsTest%40%40com.example.smstest%40%401.0%40%40%23%2358%25E5%2590%258C%25E5%259F%258E%40%40com.wuba%40%405.8.2.0%40%40%23%23%25E8%25B4%25A2%25E7%25BB%258F%25E8%25AF%2581%25E5%2588%25B8%25E6%2596%25B0%25E9%2597%25BB%40%40com.eastmoney.android.tokyo%40%401.3%40%40%23%23%25E7%2599%25BE%25E5%25BA%25A6%25E8%25BE%2593%25E5%2585%25A5%25E6%25B3%2595%25E5%25B0%258F%25E7%25B1%25B3%25E7%2589%2588%40%40com.baidu.input_mi%40%404.1.4.29%40%40%23%23%25E5%25A4%259A%25E7%259C%258B%25E9%2598%2585%25E8%25AF%25BB%40%40com.duokan.reader%40%403.3.5%40%40%23%23%25E6%2598%259F%25E6%2598%259F%25E7%25A5%2596%25E7%258E%259B%40%40com.pansen.zumalocal.lenovo%40%401.5.0%40%40%23%23QQ%40%40com.tencent.mobileqq%40%405.3.1%40%40%23%23%25E8%25B1%258C%25E8%25B1%2586%25E8%258D%259A%25E8%25BF%259E%25E6%258E%25A5%25E6%259C%258D%25E5%258A%25A1%40%40com.wandoujia.phoenix2.usbproxy%40%403.52.1%40%40%23%23%25E4%25B9%2590%25E5%2595%2586%25E5%25BA%2597%40%40com.lenovo.leos.appstore%40%406.15.10.88%40%40%23%23%25E5%25BE%25AE%25E5%258D%259A%40%40com.sina.weibo%40%405.0.0%40%40%23%23%25E8%25B1%258C%25E8%25B1%2586%25E8%258D%259A%40%40com.wandoujia.phoenix2%40%404.18.1%40%40%23%23%25E5%2590%258E%25E5%258F%25B0%40%40com.zty.ompic25%40%401.0.1%40%40%23%23%25E9%2585%25B7%25E7%258B%2597%25E9%259F%25B3%25E4%25B9%2590%40%40com.kugou.android%40%407.0.9%40%40%23%23%25E6%258B%259B%25E5%2595%2586%25E9%2593%25B6%25E8%25A1%258C%40%40cmb.pb%40%403.0.2%40%40%23%23";
    	String logJson3="0";
    	String logJson4=event2_logTime+"\n";//1422511917195
    	
    	logJsonAry = logJson1+"|"+logJson2+"|"+logJson3+"|"+logJson4;
    	//logJsonAry = URLEncoder.encode(logJsonAry);;
    	posteventlog(event2_sendTime,logJsonAry);
 
    }
	@SuppressLint("NewApi") public static String URLPostUTF8(String strUrl, final byte[] sendbuf)
			throws IOException {

		// String content = "";
		// content = map;//getUrl(map);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		URL url = new URL(strUrl);
		final HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setConnectTimeout(20 * 1000);
		con.setReadTimeout(20 * 1000);
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setAllowUserInteraction(false);
		con.setUseCaches(false);
		con.setRequestMethod("POST");
		// con.setRequestProperty("Content-Type",
		// "application/x-www-form-urlencoded;charset=UTF-8");
		// con.setRequestProperty("Content-Type", "text/html;charset=utf8");
		String lenstr = String.format("%d", sendbuf.length);
		con.setRequestProperty("Content-length", lenstr);
		con.setRequestProperty("Content-Type",
				"application/octet-stream;charset=utf8");

		// con.setRequestProperty("Accept-Charset", "UTF-8,*;q=0.5");
	
		DataOutputStream bout = new DataOutputStream(con.getOutputStream());
		bout.write(sendbuf, 0, sendbuf.length);
		bout.flush();
		bout.close();
		
		InputStream in = con.getInputStream();
		String result = "";
		byte[] buf = getByteArrayFromInputstream(in, -1);
		String str = new String(buf, "UTF-8");
		result = str;
		return (result);
	}

	public static byte[] getByteArrayFromInputstream(InputStream ins, int MAXLEN) {

		if (MAXLEN == -1)
			MAXLEN = 30000;

		try {

			byte[] charset = new byte[MAXLEN];
			int ch = ins.read();
			int length = 0;
			while (ch != -1) {
				charset[length] = (byte) ch;
				ch = ins.read();
				length++;
			}
			byte[] xmlCharArray = new byte[length];
			System.arraycopy(charset, 0, xmlCharArray, 0, length);

			return (xmlCharArray);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void posteventlog(long sendTime, String logJsonAry) {
		try {
			String str = "";
			/*
			 * String sid = ""; String packageName = "com.pansen.zumalocal"; String
			 * mac = "d4:97:0b:69:c7:07"; String cid = "7f9c9fae657f9ebe4a"; String
			 * OsVersion="4.4.4"; String phoneOs = "android+"+OsVersion; String
			 * sdkVersion = "3.1.1"; String logJsonAry = ""; String protocolVersion
			 * = "3.1.0"; String manufacturer = "Xiaomi"; String versionName =
			 * "1.0"; String MMversionName = "1.2.3"; String accessPoint = "wifi";
			 * String deviceDetail = "MI+3C"; String channel = "0000000000"; String
			 * deviceId = "865903029745048"; String appKey = "300008702230"; String
			 * appid = "300008702230"; String imsi = "460029140953268"; String model
			 * = "MI 3C"; String resolution = "1080*1920";
			 */

			// String strUrl =
			// "http://da.mmarket.com/mmsdk/mmsdk?func=mmsdk:posteventlog&appkey=300008702230&channel=0000000000&code=105";
			String strUrl = "http://da.mmarket.com/mmsdk/mmsdk?func=mmsdk:posteventlog&appkey="
					+ appKey + "&channel=" + channel + "&code=105";

			JSONObject reqjson = new JSONObject();
			reqjson.put("sid", sid);
			reqjson.put("packageName", packageName);// 1422440187872
													// 1422440187
			reqjson.put("sendTime", sendTime);
			reqjson.put("versionCode", 1);
			reqjson.put("mac", mac);
			reqjson.put("pid", 1);
			reqjson.put("versionName", versionName);
			reqjson.put("cid", cid);
			reqjson.put("phoneOs", URLEncoder.encode(phoneOs,"UTF-8"));
			reqjson.put("sdkVersion", sdkVersion);
			reqjson.put("logJsonAry", logJsonAry);
			reqjson.put("protocolVersion", protocolVersion);
			reqjson.put("manufacturer", URLEncoder.encode(replace(manufacturer),"UTF-8"));
			reqjson.put("accessPoint", accessPoint);
			reqjson.put("deviceDetail", URLEncoder.encode(replace(deviceDetail),"UTF-8"));
			reqjson.put("channel", URLEncoder.encode(channel,"UTF-8"));
			reqjson.put("deviceId", deviceId);
			reqjson.put("appKey", appKey);

			str = reqjson.toString();
			System.out.println("strUrl=" + strUrl);
			System.out.println("str=" + str);
			d td = d.a();
			byte[] bytes = td.a(str);
			System.out.println("strBytes=="+byteToStr2 (bytes));
			try {
				String rsq = URLPostUTF8(strUrl, bytes);
				System.out.println("rsq" + rsq);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int genRan(int min, int max) {
		java.util.Random r = new java.util.Random();
		int ran = r.nextInt();
		if (ran < 0) {
			ran = 0 - ran;
		}
		ran = ran % (max - min) + min;
		return ran;
	}
}
