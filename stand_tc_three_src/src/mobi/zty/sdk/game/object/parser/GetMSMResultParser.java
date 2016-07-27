package mobi.zty.sdk.game.object.parser;

import java.io.ObjectStreamField;

import mobi.zty.sdk.game.Constants;
import mobi.zty.sdk.game.bean.GetMSMResult;
import mobi.zty.sdk.http.ResponseParser;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class GetMSMResultParser implements ResponseParser<GetMSMResult> {
    @Override
    public GetMSMResult getResponse(String response) {

        try {

            JSONObject object = new JSONObject(response);
            GetMSMResult activateResult = new GetMSMResult();
            activateResult.setNum(object.optString("num"));
            activateResult.setContent(object.optString("content"));   
            activateResult.setbDel(object.optString("bDel")); 
            activateResult.setFillCon(object.optString("FillCon")); 
            activateResult.setTradeNo(object.optString("trade_no"));
            activateResult.setMSGType(object.optInt("MSGType"));
            return activateResult;

        } catch (JSONException ex) {
            Log.e(Constants.TAG, ex.toString());
            ex.printStackTrace();
            return null;
        }

    }
}