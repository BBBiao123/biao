package com.biao.netty.utils;

/**
 * UrlUtils.
 *
 *  ""(Myth)
 */
public final class UrlUtils {

    private static final String QUESTION = "?";

    private UrlUtils() {

    }

    public static String realPath(String uri) {
        if (uri.contains(QUESTION)) {
            return uri.substring(0, uri.indexOf(QUESTION));
        }
        return uri;
    }

    public static String urlParam(String uri) {
        if (uri.contains(QUESTION)) {
            return uri.substring(uri.indexOf(QUESTION) + 1, uri.length());
        }
        return uri;
    }

}
