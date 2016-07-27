package mobi.zty.pay.sdk.factory;
import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.pay.sdk.mmBase.MMBasePayInstance;
import mobi.zty.pay.sdk.wecaht.WeChatInstance;

public class PaymentFactoy {
	public static PaymentInterf producePay(int billTypy){
		try {
			switch (billTypy) {
			case PayConfig.JD_PAY:
				return MMBasePayInstance.getInstance();
			case PayConfig.WECHAT_PAY:
				return WeChatInstance.getInstance();
			default:
				break;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
		return null;
	};
}
