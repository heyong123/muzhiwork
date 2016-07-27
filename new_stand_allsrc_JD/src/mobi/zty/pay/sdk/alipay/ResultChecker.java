package mobi.zty.pay.sdk.alipay;

import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.Rsa;

import org.json.JSONObject;

/**
 * 对签名进行验签
 */
public class ResultChecker {

    public static final int RESULT_INVALID_PARAM = 0;
    public static final int RESULT_CHECK_SIGN_FAILED = 1;
    public static final int RESULT_CHECK_SIGN_SUCCEED = 2;

    String mContent;

    public ResultChecker(String content) {
        this.mContent = content;
    }

    /**
     * 对签名进行验签
     *
     * @return int
     */
    public int checkSign() {

        int retVal = RESULT_CHECK_SIGN_SUCCEED;

        try {
            JSONObject objContent = Helper.string2JSON(this.mContent, ";");
            String result = objContent.getString("result");
            result = result.substring(1, result.length() - 1);
            // 获取待签名数据
            int iSignContentEnd = result.indexOf("&sign_type=");
            String signContent = result.substring(0, iSignContentEnd);
            // 获取签名
            JSONObject objResult = Helper.string2JSON(result, "&");
            String signType = objResult.getString("sign_type");
            signType = signType.replace("\"", "");

            String sign = objResult.getString("sign");
            sign = sign.replace("\"", "");
            // 进行验签 返回验签结果
            if (signType.equalsIgnoreCase("RSA")) {
                if (!Rsa.doCheck(signContent, sign,
                        AlipayConstant.RSA_ALIPAY_PUBLIC))
                    retVal = RESULT_CHECK_SIGN_FAILED;
            }
        } catch (Exception e) {
            retVal = RESULT_INVALID_PARAM;
            e.printStackTrace();
        }

        return retVal;
    }

}
