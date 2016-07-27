package mobi.zty.sdk.game.object.parser;

import mobi.zty.sdk.game.bean.AlipayOrderInfo;
import mobi.zty.sdk.http.ResponseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class AlipayOrderInfoParser implements ResponseParser<AlipayOrderInfo> {
    @Override
    public AlipayOrderInfo getResponse(String response) {

        try {

            JSONObject object = new JSONObject(response);
            AlipayOrderInfo orderInfo = new AlipayOrderInfo();
            orderInfo.setOrderInfo(object.getString("order_info"));
            orderInfo.setSign(object.getString("sign"));
            orderInfo.setSignType(object.getString("sign_type"));
            return orderInfo;

        } catch (JSONException ex) {
            return null;
        }

    }
}
