package com.iap.demo.weiying;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Iterator;

import mobi.zty.sdk.util.Helper;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MMPayUtil {
	
	 //static String str1;//"2ETI2R";//时间    
	 static String REPLACE_STR = "[a-zA-Z`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#�?…�?&*（）—�?+|{}【�?‘；：�?“�?。，、？]";
	 static String str2 = "1LQWK";//"1LQWK";//"1NJCHR";//程序ID     
	 //static String str3 ;//"AMU5H0917";//计费代码
	 static String str4="0";//"0";//渠道ID
	 static String str6 = "A3247E0A5B4FCC46";//"A3247E0A5B4FCC46";//APPKEY
	 static String str7;//"4J2EGWX8DW";//lsl,Imsi
	 static String str8 ;//"8IMBNSD73D";//lsl,IMEI
 	    
	 static String str9;//key,"ac9c536b357e1df7cb4f06a631c005068ba73dcb0eb65c689aff21893a8244f87d7c985c91244eb6ba4b9b31a881c475c3d422b30a408483a87936e4661a074847204e5a8699578c4fec6ba4688ebb9b16507f608dd4bdf4dac826ec560026297c2b60859ca912b0ec513290edaf0a5330c94709ec7dc55bbb4c7c08fe6c23b3bf052913adeb2cadc9a9739020fb0d23a508db23f5dcc963e50045ae5a1f3d5f02b3f77975f4a99c31fc3f341d1e09acd21640ec6f0aeacf2c6b1d1e632b09ab314294749485e18dbbed65a5e4c8a6a41bd284103054242ddbd6acc675162f7f2d0c7ea038445807d280a97518e5ff090c54855e72e399dc5dff8e262a3afff7";//lsl,用户数据
	 //static String str10;//"test";//lsl,用户数据
	 //static String str5 = "";
     
	 public static String getStr1()
	 {
		 return str2;
	 }
	
	 public static String getStr3()
	 {
		 return str2;
	 }
	 public static String getStr4()
	 {
		 return str4;
	 }
	 public static String getStr5()
	 {
		 return str4;
	 }
	 public static String getStr6()
	 {
		 return str6;
	 }
	 public static String getStr7()
	 {
		 return str7;
	 }
	 public static String getStr8()
	 {
		 return str8;
	 }
	 public static String getStr9()
	 {
		 return str9;
	 }
	
	 
	 private static String a(byte[] paramArrayOfByte)
	  {
		  RSAPublicKey fd;
		  String str = null;
	    try
	    {
	      str = ((RSAPublicKey)((X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(
	    		  new ByteArrayInputStream(paramArrayOfByte))).getPublicKey()).getModulus().toString(16);
	      if ((str != null) && (str.contains("modulus:")))
	      {
	        str = str.substring(9 + str.indexOf("modulus: "), str.indexOf("\n", str.indexOf("modulus:")));
	        str.trim();
	        //d.c("CommUtil", "public key:" + str);
	      }
	      return str;
	    }
	    catch (CertificateException localCertificateException)
	    {
	      while (true)
	      {
	        localCertificateException.printStackTrace();
	        
	      }
	    }
	  }

	  private static byte[] a(Context context)
	  {
		  Context localContext = context;//c.getContext();
	    String str = localContext.getApplicationInfo().packageName;
	    //d.c("CommUtil", "package name:" + str);
	    Iterator localIterator = ((Activity)localContext).getPackageManager().getInstalledPackages(64).iterator();
	    PackageInfo localPackageInfo = null;
	    do
	    {
	      if (!localIterator.hasNext())
	        break;
	      localPackageInfo = (PackageInfo)localIterator.next();
	    }
	    while (!localPackageInfo.packageName.equals(str));
	    for (byte[] arrayOfByte = localPackageInfo.signatures[0].toByteArray(); ; arrayOfByte = null)
	      return arrayOfByte;
	  }

	  public static void adfdf(Context context)//init
	  {
		  str9 = getab(context);
		  str8 = getaf(context);
		  str7 = getAD(context);
	  }
	  public static String getab(Context context)//getKey
	  {
	    return a(a(context));
	  }
	  
	  public static String getaf(Context context)//getIMEI
	  {
	     String ret = ((TelephonyManager)context.getSystemService("phone")).getDeviceId();	 
	     return ret;
      }
	  
	 public static String getAD(Context context)//getIMSI
	 {
		 //"460029140953268"
	   String ret = ((TelephonyManager)context.getSystemService("phone")).getSubscriberId();
	   if ((ret == null) || (ret.equals("")))
		   ret = "10086";
	   //Util_G.debug_e(Util_G.busylog, "imsi:"+ret); 
	   return ret;
	 }
	public static native String getstrcontent(String str1,String str2,String str3
			,String str4,String str5,String str6,String str7,
			String str8,String str9,String str10);
	public static native String getstrcontent(String paycode,String userdata);
	//public native String getstr(String request);
	
/*public static String getOrderContent(String programId,String AppKey
		,String key,String channle,String payCode
		,String IMEI,String IMSI,
		String userData)
 {
      String ret = "";
      
      String str1 = SMSPurchase.getTime();//"2ETI2R";//时间
      
      String str2 = SMSPurchase.encode(MMPayUtil.parseLong(programId));//"1LQWK";//"1NJCHR";//程序ID
      
      String str3 = channle;//"0";//渠道ID
      String str4 = SMSPurchase.encode(MMPayUtil.parseLong(payCode));//"AMU5H0917";//计费代码

      String str6 = AppKey;//"A3247E0A5B4FCC46";//APPKEY
      String str7 =SMSPurchase.encode(MMPayUtil.parseLong(IMEI));//"8IMBNSD73D";//lsl,IMEI
      String str8 =SMSPurchase.encode(MMPayUtil.parseLong(IMSI));//"4J2EGWX8DW";//lsl,Imsi
  	    
      String str9 =key;//"ac9c536b357e1df7cb4f06a631c005068ba73dcb0eb65c689aff21893a8244f87d7c985c91244eb6ba4b9b31a881c475c3d422b30a408483a87936e4661a074847204e5a8699578c4fec6ba4688ebb9b16507f608dd4bdf4dac826ec560026297c2b60859ca912b0ec513290edaf0a5330c94709ec7dc55bbb4c7c08fe6c23b3bf052913adeb2cadc9a9739020fb0d23a508db23f5dcc963e50045ae5a1f3d5f02b3f77975f4a99c31fc3f341d1e09acd21640ec6f0aeacf2c6b1d1e632b09ab314294749485e18dbbed65a5e4c8a6a41bd284103054242ddbd6acc675162f7f2d0c7ea038445807d280a97518e5ff090c54855e72e399dc5dff8e262a3afff7";//lsl,用户数据
      String str10 =userData;//"test";//lsl,用户数据
      String str5 = "";//MMPayUtil.gen_order_id(str1,str2,str3,str4,str7,str8,str6);//"F940D3A6F910E27E";//lsl,计费代码

      //ret = MMPayUtil.getOrderContent(str1, str2, str3, str4, str5, str6, str7, str8, str9, str10);
      ret = MMPayUtil.getstrcontent(str1, str2, str3, str4, str5, str6, str7, str8, str9, str10);
      //MMPayUtil tPay = new MMPayUtil();
      //Util_G.debug_e(Util_G.busylog, tPay.getstr("hello"));
      return ret;
 }

 public static String getOrderContent(String str1,String str2,String str3
		,String str4,String str5,String str6,String str7,
		String str8,String str9,String str10)
 {
    str5 =MMPayUtil.gen_order_id(str1,str2,str3,str4,str7,str8,str6);//"F940D3A6F910E27E";//lsl,计费代码

    Util_G.debug_e(Util_G.busylog, "str1:"+str1+"str2:"+str2+"str3:"+str3);
    Util_G.debug_e(Util_G.busylog,"str4:"+str4+"str5:"+str5+"str6:"+str6);
    Util_G.debug_e(Util_G.busylog,"str7:"+str7+"str8:"+str8+"str9:"+str9
    		+"str10:"+str10); 	
    String str11 = IdentifyApp.SMSOrderContent(str1, str2, str3, str4, str5,str6, str7, str8, str9, str10);
    Util_G.debug_e(Util_G.busylog, "content:"+str11);
    return str11;
 }
*/	
public static long parseLong(String str)
 {
	Log.e("parseLong", "parseLong-->"+str);
	Log.e("parseLong", "REPLACE_STR-->"+REPLACE_STR);
	str.replaceAll(REPLACE_STR, "");
	Log.e("parseLong", "parseLong-->"+str);
	return Long.parseLong(str);
	 
}

//getOrderContent
public static String getstefte(String str1,String str2,String str3
		,String str4,String str5,String str6,String str7,
		String str8,String str9,String str10)
 {  	
//    String str11 = IdentifyApp.SMSOrderContent(str1, 
//    		str2, str3, str4, str5,str6, 
//    		str7, str8, str9, str10);
	String str11 = "";
    return str11;
 }

public static String  getProgramId(String appid)
{
//	if (Helper.isDebugEnv()) {
	  	return "3043167";
//	}else{
//		return IdentifyApp.decryptPapaya(appid);
//	}
}

 public static String gen_order_id(String str1, String str2, String str3, String str4, String str5,String str6,String str7)
 {
//    String str11 = IdentifyApp.generateTransactionID(str1, str2, str3, str4, str5,str6,str7); //mm1.23
//	 String str11 = IdentifyApp.generateTransactionID(str1, str2, str3, str4);
    return "str11";
 }
 
 
 public static String dsfjtie(String str1, String str2, String str3, String str4, String str5,String str6,String str7)
 {
    /*String str11 = IdentifyApp.generateTransactionID(str1, str2, str3, str4, str5,str6,str7);*///mm1.23
//	 String str11 = IdentifyApp.generateTransactionID(str1, str2, str3, str4);
    return "str11";
 }
 
  public static void sendTextMessage(Context context,String num,String con)
  {
		Util_G.debug_e(Util_G.busylog, "num:"+num+"con:"+con); 	
		SmsManager smsManager = SmsManager.getDefault();
	    smsManager.sendTextMessage(num, null, con, null, null);	
   }
  
  
}
