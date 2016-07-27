package mobi.zty.pay.sdk.factory;
import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.pay.sdk.egame.EgamePayInstance;
import mobi.zty.pay.sdk.mm.MMPayInstance;
import mobi.zty.pay.sdk.qihu360.Qihu360PayInstance;
import mobi.zty.pay.sdk.uincon.UniconPayInstance;

public class PaymentFactoy {
	public static PaymentInterf producePay(int billTypy){
		try {
			switch (billTypy) {
			case PayConfig.UNICOM_MOBLE:
				return UniconPayInstance.getInstance();
			case PayConfig.TIANYI_MOBLE:
				return EgamePayInstance.getInstance();
			case PayConfig.CMCC_MOBLE:
				return MMPayInstance.getInstance();
//			case PayConfig.JD_PAY:
//				return MMBasePayInstance.getInstance();
			case PayConfig.QIHU_PAYTYPE:
				return Qihu360PayInstance.getInstance();
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
