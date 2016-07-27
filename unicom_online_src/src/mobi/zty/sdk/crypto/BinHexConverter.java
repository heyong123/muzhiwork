package mobi.zty.sdk.crypto;

/**
 * 二进制十六进制转换类
 *
 * @author Chicken
 * @version 1.0.0
 */
public class BinHexConverter {

    private static final char hexTable[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final byte binTable[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, 0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * 把十六进制字符串转换成二进制数组
     *
     * @param hexContent 十六进制字符�?
     * @return 二进制数�?
     */
    public static byte[] hex2Bin(String hexContent) {


        if (hexContent.length() % 2 == 0) {

            byte result[] = new byte[hexContent.length() >> 1];
            for (int i = 0; i < hexContent.length(); i += 2) {
                result[i >> 1] = (byte) (binTable[hexContent.charAt(i)] << 4 | binTable[hexContent.charAt(i + 1)]);
            }
            return result;

        } else {
            throw new IllegalArgumentException("Wrong hexContent length.");
        }


    }

    /**
     * 把二进制数组转换成十六进制字符串
     *
     * @param binContent 二进制数�?
     * @return 十六进制字符�?
     */

    public static String bin2Hex(byte[] binContent) {

        if (binContent != null) {
            char result[] = new char[binContent.length << 1];
            for (int i = 0; i < binContent.length; i++) {
                result[i << 1] = hexTable[0xf & binContent[i] >> 4];
                result[(i << 1) + 1] = hexTable[binContent[i] & 0xf];
            }
            return new String(result);
        } else {
            throw new IllegalArgumentException("NULL binContent");
        }

    }

}
