package mobi.zty.sdk.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Base64;

@TargetApi(Build.VERSION_CODES.FROYO)
public class AES implements Crypto {

    @Override
    public String encrypt(String key, String content) throws Exception {

        if (key == null || content == null) return null;

        byte[] keyBytes = BinHexConverter.hex2Bin(key);
        if (keyBytes.length != 16) throw new IllegalArgumentException("key length must be 16.");

        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
        return Base64.encodeToString(encrypted, Base64.NO_PADDING | Base64.NO_WRAP | Base64.NO_CLOSE);

    }

    @Override
    public String decrypt(String key, String content) throws Exception {

        if (key == null || content == null) return null;
        byte[] keyBytes = BinHexConverter.hex2Bin(key);
        if (keyBytes.length != 16) throw new IllegalArgumentException("key length must be 16.");

        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] encrypted = Base64.decode(content, Base64.NO_PADDING | Base64.NO_WRAP | Base64.NO_CLOSE);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original, "UTF-8");

    }

}
