package com.biao.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtils {

    //私钥： AES固定格式为128/192/256 bits.即：16/24/32bytes。DES固定格式为128bits，即8bytes。
    private static final String SECRET_KEY = "1234567890asdfghjklzxcvbnmzxcvbn";

    public static String encrypt(String text) {
        try {
            Key keySpec = getSecretKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] secretText = cipher.doFinal(text.getBytes(Charset.defaultCharset()));
            return Base64.getEncoder().encodeToString(secretText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String secretText) {
        try {
            byte[] byteValue = Base64.getDecoder().decode(secretText);
            Key key = getSecretKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] ret = cipher.doFinal(byteValue);
            return new String(ret, Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private static SecretKeySpec getSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128, new SecureRandom(SECRET_KEY.getBytes(Charset.defaultCharset())));
        SecretKey secretKey = kg.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    public static void main(String[] args) {
        System.out.println(decrypt(""));
    }
}
