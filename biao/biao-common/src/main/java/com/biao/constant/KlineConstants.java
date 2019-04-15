package com.biao.constant;

/**
 *  ""(Myth)
 */
public final class KlineConstants {

    public static final String KINE_SUFFIX = "kline";

    public static String buildKlineKey(String coinMain, String coinOther) {
        return String.join("_", coinMain, coinOther, KINE_SUFFIX);

    }
}
