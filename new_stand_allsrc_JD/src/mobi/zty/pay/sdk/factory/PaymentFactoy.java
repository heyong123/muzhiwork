package mobi.zty.pay.sdk.factory;
import android.util.Log;
import mobi.zty.pay.sdk.PayConfig;
import mobi.zty.pay.sdk.PaymentInterf;
import mobi.zty.pay.sdk.alipay.TaoBaoInstance;
import mobi.zty.pay.sdk.ananFee.AnAnFeeInstance;
import mobi.zty.pay.sdk.egame.EgamePayInstance;
import mobi.zty.pay.sdk.fanheFee.FanHeFeeInstance;
import mobi.zty.pay.sdk.leyouFee.LeYouFeeInstance;
import mobi.zty.pay.sdk.mm.MMPayInstance;
import mobi.zty.pay.sdk.mmBase.MMBasePayInstance;
import mobi.zty.pay.sdk.mmFee.MMFeeInstance;
import mobi.zty.pay.sdk.mmNetFee.MMNetFeeInstance;
import mobi.zty.pay.sdk.tianyiFee.TianYiFeeInstance;
import mobi.zty.pay.sdk.uincon.UniconPayInstance;
import mobi.zty.pay.sdk.unicomFee.UnicomFeeInstance;
import mobi.zty.pay.sdk.wecaht.WeChatInstance;
import mobi.zty.sdk.util.Util_G;

public class PaymentFactoy {
	public static PaymentInterf producePay(int billTypy){
		try {
			switch (billTypy) {
			case PayConfig.CMCC_MOBLE:
				return MMPayInstance.getInstance();
			case PayConfig.CMCC_NETIVE_FEE:
				return MMFeeInstance.getInstance();
			case PayConfig.UNICOM_MOBLE:
				return UniconPayInstance.getInstance();
			case PayConfig.TIANYI_MOBLE:
				return EgamePayInstance.getInstance();
			case PayConfig.CMCC_LEYOU_FEE:
				return LeYouFeeInstance.getInstance();
			case PayConfig.CMCC_ANAN_FEE:
				return AnAnFeeInstance.getInstance();
			case PayConfig.MM_NET_FEE:
				return MMNetFeeInstance.getInstance();
			case PayConfig.UNICOM_TEN_FEE:
				return UnicomFeeInstance.getInstance();
			case PayConfig.TIANYI_FEE:
				return TianYiFeeInstance.getInstance();
			case PayConfig.JD_PAY:
				return MMBasePayInstance.getInstance();
			case PayConfig.FANHE_PAY:
				return FanHeFeeInstance.getInstance();
			case PayConfig.BILL_TYPE_TAOBAO:
				return TaoBaoInstance.getInstance();
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
