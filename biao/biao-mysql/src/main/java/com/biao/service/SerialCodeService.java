package com.biao.service;

public interface SerialCodeService {

    /**
     * 字符串序列
     *
     * @return yyyyMMddHHmmssSSS+8位数字
     */
    String generateSerialCode();

    /**
     * 字符串序列
     *
     * @param prefix
     * @return prefix+yyyyMMddHHmmssSSS+8位数字
     */
    String generateSerialCode(String prefix);

}
