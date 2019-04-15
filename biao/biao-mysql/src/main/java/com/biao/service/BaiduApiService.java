package com.biao.service;

import java.util.Map;

public interface BaiduApiService {

    public static final String MAPKEY_NAME = "姓名";
    public static final String MAPKEY_BITCH = "出生";
    public static final String MAPKEY_IDCARD = "公民身份号码";
    public static final String MAPKEY_SEX = "性别";
    public static final String MAPKEY_GROUP = "民族";
    public static final String MAPKEY_ADDRESS = "住址";

    public Map<String, String> parseImgByte(byte[] imgData, String apikey, String apiSecret);
}
