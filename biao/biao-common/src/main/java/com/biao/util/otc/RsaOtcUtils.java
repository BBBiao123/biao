package com.biao.util.otc;

import com.biao.util.RsaUtils;
import com.biao.vo.otc.OtcAppropriationRequestVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RsaOtcUtils {

    private static final Logger logger = LoggerFactory.getLogger(RsaOtcUtils.class);

    public static final String DEFAULT_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAL6MOAZVla6SJdJs3RRJ2BCbFICD3/R1xPiqFJ6wAvbgDo9M4ykhdpZ8BlfDgbEvhD7u47QsVcCuINPRrtNuQeYsqFuZPfgMAIKRDzZ1h141g3O7HrH4EEDri4V4abtQHwjQyEvWHPbsvnObnmyF0J4r3B8LMzCTTkG2XzxKUYpXAgMBAAECgYBjDheNy239i0IYKrme82hb8ZJt4KBKv/I+ZoRDnjJcTkujWQ/58MhqIF1XwG2qQ45W/O7oWefWO28bTjS4+udMLyzep6zoMweJ37yqpz/cCn0b7Vc0FW6duTmqtbFRzC2RNsWUt3IIdzWEjqZoZLk1uTXQ02gzwR9XGkOIYM81sQJBAPI2L+I1vDTDyloVgo3NaB/ELr/oWh7d9eL0vmU3vpZoNlVbL9+VxT5F74Y//bLz0zv3/u7+7VbWi0Yz5HjQyAkCQQDJZR60MAWt3b3F7aO8PuCKCu8XTocu0KkRJQlHkZnrARqw0N0Tp6X9zi/J0HHpOFeICpUoF2JuvJX1GPsqipdfAkAXW4Jiniv4Kqlo5ooggp538cNQXmfScjU0HcX+nNGUT5htws1rElZjGvtuRt2AVGMadV/wTEU3CpmDu51cyUWBAkBkldiL7iAoAkOyA0pvzoee6m8XmOTzgMuwAGtuD05sjRSjku2Xz3ecF8rOZQk/jiBJld+BMoy6+f17eIqZaLbJAkATqxH6fdTBfDtQtcMlTLPz/v79Jr0R1s9Cdpi69KbuLOGBYptxAzTtUwv6N07jomXxbamuKdWaEMZsoNkxxjtK";
    public static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+jDgGVZWukiXSbN0USdgQmxSAg9/0dcT4qhSesAL24A6PTOMpIXaWfAZXw4GxL4Q+7uO0LFXAriDT0a7TbkHmLKhbmT34DACCkQ82dYdeNYNzux6x+BBA64uFeGm7UB8I0MhL1hz27L5zm55shdCeK9wfCzMwk05Btl88SlGKVwIDAQAB";

    public static final String PARAM_CONNECTOR = "&";
    public static final String KEY_VALUE_CONNECTOR = "=";

    private static final String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String decryptParam(String secretKey) {
        return RsaUtils.decryptByPrivateKey(secretKey, DEFAULT_PRIVATE_KEY);
    }

    public static Map<String, String> getParamMap(String params) {
        Map<String, String> keyValueMap = new HashMap<>();
        if (StringUtils.isBlank(params)) {
            return keyValueMap;
        }
        String[] keyValues = params.split(PARAM_CONNECTOR);
        if (keyValues.length < 1) {
            return keyValueMap;
        }
        String[] keyValueArr = null;
        for (String keyValue : keyValues) {
            keyValueArr = keyValue.split(KEY_VALUE_CONNECTOR);
            if (Objects.isNull(keyValueArr) || keyValueArr.length != 2) {
                return keyValueMap;
            }
            keyValueMap.put(keyValueArr[0], keyValueArr[1]);
        }
        return keyValueMap;
    }

    public static Map<String, String> getParamMapBySecretKey(String secretKey) {
        try {
            String secretValue = decryptParam(secretKey);
            return getParamMap(secretValue);
        } catch (Exception e) {
            logger.error("RSA私钥解密失败", e);
        }
        return new HashMap<String, String>();
    }

    public static <T> T getParamObject(Map<String, String> keyValueMap, Class<T> clazz) {
        T paramObject = null;
        try {
            paramObject = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                f.set(paramObject, keyValueMap.get(f.getName()));
                f.setAccessible(false);
            }
        } catch (Exception e) {
            logger.error("类型转换错误", e);
        }
        return paramObject;
    }

    public static <T> T getParamObject(String secretKey, Class<T> clazz) {

        T paramObject = null;
        try {
            String secretValue = decryptParam(secretKey);
            Map<String, String> keyValueMap = getParamMap(secretValue);
            paramObject = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                f.set(paramObject, keyValueMap.get(f.getName()));
                f.setAccessible(false);
            }
//            for (String filedName : filedNames) {
//                clazz.getFields();
//                Field f = clazz.getDeclaredField(filedName);
//                f.setAccessible(true);
//                f.set(paramObject, keyValueMap.get(filedName));
//                f.setAccessible(false);
////                Method m = clazz.getDeclaredMethod(filedName);
////                m.invoke(paramObject, keyValueMap.get(filedName));
//            }
        } catch (Exception e) {
            logger.error("类型转换错误", e);
        }
        return paramObject;
    }


    public static void main(String[] args) {
        String kk = "iIqLQhn4M6SzRvRYgaqk2SM0CnaSzNiABvWzlYZySGQUg9pETwd/0qD99qVpxRxeQoyAQ8bGzExSoMcgovpUyKNbXhKt2O3F0KCbMjHfHO2A/FrHk56cQn+ptzPh/d9k/a5C2NeajUw1UeuOzbaibHPu0rDqrF6gMsKggYTAUf4=";
        OtcAppropriationRequestVO requestVO = RsaOtcUtils.getParamObject(kk, OtcAppropriationRequestVO.class);
        System.out.println(requestVO.getBatchNo());
    }

}
