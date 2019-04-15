package com.biao.util.otc;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <b>功能说明:MD5签名工具类 </b>
 *
 * ""Peter <a href="http://www.roncoo.net">广州市领课网络科技有限公司(www.roncoo.net)</a>
 */
public class MD5Util {

    private static final Logger LOG = LoggerFactory.getLogger(MD5Util.class);

    /**
     * 私有构造方法,将该工具类设为单例模式.
     */
    private MD5Util() {
    }

    private static final String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String encode(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = md5.digest(password.getBytes("utf-8"));
            String passwordMD5 = byteArrayToHexString(byteArray);
            return passwordMD5;
        } catch (Exception e) {
            LOG.error(e.toString());
        }
        return null;
    }

    public static String encode(String password, String enc) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = md5.digest(password.getBytes(enc));
            String passwordMD5 = byteArrayToHexString(byteArray);
            return passwordMD5;
        } catch (Exception e) {
            LOG.error(e.toString());
        }
        return password;
    }

    private static String byteArrayToHexString(byte[] byteArray) {
        StringBuffer sb = new StringBuffer();
        for (byte b : byteArray) {
            sb.append(byteToHexChar(b));
        }
        return sb.toString();
    }

    private static Object byteToHexChar(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hex[d1] + hex[d2];
    }

    /**
     * 获取参数签名
     *
     * @param paramMap  签名参数
     * @param paySecret 签名密钥
     * @return
     */
    public static String getSign(Map<String, String> paramMap, String paySecret) {
        SortedMap<String, String> smap = new TreeMap<String, String>(paramMap);
        if (smap.get("sign") != null) {
            smap.remove("sign");
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> m : smap.entrySet()) {
            Object value = m.getValue();
            if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                stringBuffer.append(m.getKey()).append("=").append(value).append("&");
            }
        }
        if (stringBuffer.length() < 1) { // 说明paramMap的value值都为空，则返回null,以示错误
            return null;
        }
        stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        String argPreSign = stringBuffer.append("&paySecret=").append(paySecret).toString();
        LOG.info("待签名数据:" + argPreSign);
        return MD5Util.encode(argPreSign).toUpperCase();
    }

    public static void main(String[] args) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("batchNo", "123435678989");
        paramMap.put("symbol", "UES");
        paramMap.put("volume", String.valueOf(1.2));
        paramMap.put("askUserId", "255063718591860736");
        paramMap.put("userId", "255063981146902528");
        String paySecret = "6798789780890";
        System.out.println(MD5Util.getSign(paramMap, paySecret));
    }
}
