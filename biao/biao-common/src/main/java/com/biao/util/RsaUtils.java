package com.biao.util;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RsaUtils {

    public static final String DEFAULT_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIsAMkvRZgkmvGak2op6gBeFbw/QZYzLtkkb8j7wLAtnbB7zdWcdwXjNd+lavDfNjhEhiQVBPwSRbsVQMV5j1fOjGhBtyPIbfLAjM3pIj7sl3TtMaS01NPEVHoGPgiPMCVrOK0WVQoA/vsDKFPrlgMKFs1G6Xy7ozmqq3+iOMZvVAgMBAAECgYBeeK22dgVBZwBZSuX1hDiWw+ut9ddvD+0+Vy4XtKhav9nuV02N3d1mu08qXK7bxmsuHXrBBY1ND4MXu98x5ey8/1rg8v8Cmr8r1SrgWFnSx1bZFt9Xef1C4H3Cown3aBAqs9blvx8BzZXRH75pnuocR0jZMovhFjSarDmJYNuRKQJBAMgQ1lAsQ9Ex5idrw3Im7SAZL/M+L0K0r7idOZs3H1tKnt8365IvN12CvIDRrHCKMuh+Wu2gr290hLaz3dv/KXsCQQCx3MxIq/K7QKW0Pn4RvpJ+9REqkM+GE/4bXEjP0REYkjXhoWVy/t1btp2p1Ug9+URTqxrCRxenHRX8VQXdswbvAkEAiAUSgE2oCfl//ftBVUHr4+/b7uevYA3SjpSujr3KXtn7681sJuZdIQF6waZMM/NxqyoFDhw6Sm+Qr1rYkvMHMwJAYMU6HHv5V+onffbWTr86I3sguOELF+7+vVIrh24/d/fs+vL+KGAEfebQx1t0r8tUYpVgvL1DYDgSAzUbOPEz3wJAJ386RhWEIXvy4pXf8GR6GViuPBLwckPCKBMr/DSY7OW2wurvjRhVj/VtsaZnjo+elxNKz/qII+arGWlqTcnQRA==";
    public static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLADJL0WYJJrxmpNqKeoAXhW8P0GWMy7ZJG/I+8CwLZ2we83VnHcF4zXfpWrw3zY4RIYkFQT8EkW7FUDFeY9XzoxoQbcjyG3ywIzN6SI+7Jd07TGktNTTxFR6Bj4IjzAlazitFlUKAP77AyhT65YDChbNRul8u6M5qqt/ojjGb1QIDAQAB";

    public static final String PRIVATE_KEY_ADAPTATION = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALpxZUKjSucDzAfUm48TXRp16R4TKumYR6WKeBqwjaZk1AsKTZV4iZUvg6LTHDwwG8zu8K7jVoYTRFUj8GsshGKARzhRALlU7/qLHS5oajjMjo+T20H46AaFRMXFFKcHoYoapIoDjEkwHj9SlFGeesyfunvQ4rDQjCxXsNoMjPsnAgMBAAECgYA6hMD9q3clD0II6yhwPempFn023VlIvG0wfke4SNYM9fTAClnTomO8mY89mflwwsDXD+AnZF+R8ZYvCvq5CN376qHHnmKJtAD1+DTp/hJCjW8lg83FUVVgUVQDqFtkzlbRqzjVg7WETzcRKx/0GggqtOpE+E7IEGuqzhQN/p4kQQJBAPXVyxzINqEZAMk1Z6KzfqPanK1/M+rxgM2c1YE0vBXdKu41YNpe1gB5fDMUE+8uKbEfokTIonx9WVv4SRfrY/ECQQDCJu0D5/ai4YeR1KISP9EjCBeaB2mwJEAwa9C/vgCT4mPHH4bK52hTHtiNLg/PwN3PTiNqWmshaKn92dG2bYiXAkAjXngTkwaxFNzVFhbsBLEit/A00glRMx/Z9UeOiFzLNH/Zt9pQcLaPaOsimi/oCJc9AZUiGk+uY4Z4wfcaNuaxAkEAlr1oOicoS7A09NmVd/vKYZuPz4w/131dxPqAmc5oXmlgNKLaocDnM9n4Ii0II7JHxQOS5akMEmVTzn8GntAOQwJAd6oNUJBmi1a9qlsfek1wxnfc2oW3/XBUkh1MivhSy3dxhQaWJ4C1AQ0RuQBqyEx7KX1g8BDdi+hSEMj6lDzZQw==";

    public static final String PUBLIC_KEY_ADAPTATION = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6cWVCo0rnA8wH1JuPE10adekeEyrpmEelingasI2mZNQLCk2VeImVL4Oi0xw8MBvM7vCu41aGE0RVI/BrLIRigEc4UQC5VO/6ix0uaGo4zI6Pk9tB+OgGhUTFxRSnB6GKGqSKA4xJMB4/UpRRnnrMn7p70OKw0IwsV7DaDIz7JwIDAQAB";

    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    // 公钥
    private String PUBLIC_KEY;
    // 私钥
    private String PRIVATE_KEY;

    public String getPUBLIC_KEY() {
        return PUBLIC_KEY;
    }

    public void setPUBLIC_KEY(String pUBLIC_KEY) {
        PUBLIC_KEY = pUBLIC_KEY;
    }

    public String getPRIVATE_KEY() {
        return PRIVATE_KEY;
    }

    public void setPRIVATE_KEY(String pRIVATE_KEY) {
        PRIVATE_KEY = pRIVATE_KEY;
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public Map<String, String> initKey() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            // 随机生成密钥对
            KeyPairGenerator keyPairGen = KeyPairGenerator
                    .getInstance(KEY_ALGORITHM);
            // 按照指定字符串生成密钥对
            // SecureRandom secureRandom = new SecureRandom("我是字符串".getBytes());
            // keyPairGen.initialize(1024, secureRandom);

            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            PUBLIC_KEY = Coder.encryptBASE64(publicKey.getEncoded());
            // 私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            PRIVATE_KEY = Coder.encryptBASE64(privateKey.getEncoded());
            map.put("pubkey", PUBLIC_KEY);
            map.put("prikey", PRIVATE_KEY);
        } catch (Exception e) {
        }
        return map;
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String key) {
        try {
            // 取得公钥
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(
                    Coder.decryptBASE64(key));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedData = data.getBytes("utf-8");
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > 117) {
                    cache = cipher.doFinal(encryptedData, offSet, 117);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen
                            - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 117;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return Coder.encryptBASE64(decryptedData);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data, String key) {
        // 对密钥解密
        try {
            byte[] keyBytes = Coder.decryptBASE64(key);

            // 取得公钥
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            byte[] encryptedData = Coder.decryptBASE64(data);

            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > 128) {
                    cache = cipher.doFinal(encryptedData, offSet, 128);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen
                            - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 128;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();

            return new String(decryptedData, "utf-8");
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String key) {
        try {

            // 对密钥解密
            byte[] keyBytes = Coder.decryptBASE64(key);

            // 取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            byte[] encryptedData = data.getBytes("utf-8");
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > 117) {
                    cache = cipher.doFinal(encryptedData, offSet, 117);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen
                            - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 117;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return Coder.encryptBASE64(decryptedData);

        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String key) {
        try {
            // 对密钥解密
            byte[] keyBytes = Coder.decryptBASE64(key);

            // 取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encryptedData = Coder.decryptBASE64(data);

            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > 128) {
                    cache = cipher.doFinal(encryptedData, offSet, 128);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen
                            - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 128;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();

            return new String(decryptedData, "utf-8");

        } catch (Exception e) {
            System.out.println("aaa ccc:");
            e.printStackTrace();
            System.out.println("exception:" + e.getMessage());
        }
        return null;
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = Coder.decryptBASE64(privateKey);

        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);

        return Coder.encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {

        // 解密由base64编码的公钥
        byte[] keyBytes = Coder.decryptBASE64(publicKey);

        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(Coder.decryptBASE64(sign));
    }

    private RsaUtils() {

    }

    private static final RsaUtils rsa = new RsaUtils();

    public static RsaUtils getInstance() {
        return rsa;
    }

    public static void main(String args[]) {
        String encode = RsaUtils.encryptByPublicKey("www123456", DEFAULT_PUBLIC_KEY);
        System.out.println(encode);

        //String deycent = "YtU3QXdSPe1Ox1kbrpHAv8qcM18EcyThcR23+D31BQImk8+6f4SJSw5LyXpFUmOaPNoez/Yp4oAoek3zId9POBHlurXXEfHn/v9rARnBUkUweZ4foF98TMJ6dHZZAEUBk6XAQ965ws42OFHsdHWbwMBHsj0ImRMVpqvB/97/lQs=" ;
//        String deycent = "e989577f01dbe2299152c2262c40f93ab7a8e26d1a1274cc995b0fef";
//        System.out.println(RsaUtils.decryptByPrivateKey(deycent, RsaUtils.DEFAULT_PRIVATE_KEY));
    }
}
