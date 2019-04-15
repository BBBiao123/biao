package com.biao.service;

public interface BaiduAccessTokenService {

    /**
     * 获取access_token
     *
     * @param apikey
     * @param apiSecret
     * @return
     */
    String getAccessToken(String apikey, String apiSecret);

    /**
     * 刷新access_token
     *
     * @param apikey
     * @param apiSecret
     * @return
     */
    String refushAccessToken(String apikey, String apiSecret);

    /**
     * 根据结果判定是否刷新access_token,存在包含的错误码自动刷新token
     *
     * @param responseJson
     */
    void parseResultRefush(String responseJson, String apikey, String apiSecret);
}
