package mobi.zty.sdk.game.object.parser;

import mobi.zty.sdk.game.Constants;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.bean.ActivateResult;
import mobi.zty.sdk.http.ResponseParser;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.Util_G;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ActivateResultParser implements ResponseParser<ActivateResult> {
    @Override
    public ActivateResult getResponse(String response) {

        try {

            JSONObject object = new JSONObject(response);
            ActivateResult activateResult = new ActivateResult();
            activateResult.setChangePassword(object.optString("changepassword_url"));
            activateResult.setRegisterUrl(object.optString("register_url"));
            activateResult.setLoginUrl(object.optString("login_url"));
            activateResult.setAlipayWapUrl(object.optString("alipay_wap_url"));
            activateResult.setPayways(object.optString("payways"));
            activateResult.setAdfd(object.optString("afdf"));
            activateResult.setUrl(object.optString("url"));
            activateResult.setDipcon(object.optString("dipcon"));
            activateResult.setDipcon2(object.optString("dipcon2"));
            activateResult.setDipurl(object.optString("dipurl"));
            activateResult.setNoturl(object.optString("noturl"));
            activateResult.setExiturl(object.optString("exiturl"));
            activateResult.setMkurl(object.optString("mkurl"));
	        activateResult.setMk(object.optString("mk"));
	        
	        activateResult.setbDel(object.optString("bDel"));
	        activateResult.setFillCon(object.optString("FillCon"));
	        try {
	        	activateResult.setLimitMoney(object.getLong("LMoney"));
	        	activateResult.setDelayDimer(object.optLong("fee_delay_dimer"));
	        	activateResult.setAppPayId(object.optString("app_pay_id"));
	        	activateResult.setOpenDark(object.optInt("open_dark"));
	        	activateResult.setOpenAlert(object.optInt("open_alert"));
	        	activateResult.setOpenButton(object.optInt("open_button"));
	        	activateResult.setOpenOurAlert(object.optInt("open_our_alert"));
	        	activateResult.setCootype(object.optInt("cootype"));
			} catch (Exception e) {
				e.printStackTrace();
			}
	        if (Helper.isDeBuge) {
	        	Util_G.debugE("ActivateResultParser", object.toString());
//	        	activateResult.setbDel("1");
//		        activateResult.setFillCon("中国移动");
		        
		     	//电信全网支付模拟数据 start
//	        	activateResult.setMkurl("9008;http://api.slxz.com.cn/charge-platform/client/client.php;"
//	        			+ "http://sa.91muzhi.com:8090/sdk/WoplusRsq;HMAC-SHA1;suhESjvk;1元充值,2元充值;100,200;811,812;羊羊去哪儿");
//		        activateResult.setMk(Constants.TC_ALL_FEE);
//	        	//电信全网支付模拟数据end
//		        activateResult.setLimitMoney(10000);
		        
//	        	activateResult.setMkurl("300008883734;;589;1000,1000,6000,7000");
//		        activateResult.setMk(Constants.MM_JD_PAY);
//		        activateResult.setLimitMoney(10000);
			}
            return activateResult;

        } catch (JSONException ex) {
            Log.e(Constants.TAG, ex.toString());
            ex.printStackTrace();
            return null;
        }

    }
}