package mobi.zty.pay.sdk.factory;
import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.pay.sdk.egame.EgamePayInstance;

public class PaymentFactoy {
	public static PaymentInterf producePay(int billTypy){
		try {
			switch (billTypy) {
			case PayConfig.TIANYI_MOBLE:
				return EgamePayInstance.getInstance();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
		return null;
	};
}
