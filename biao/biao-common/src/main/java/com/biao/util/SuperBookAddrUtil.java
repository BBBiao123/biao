package com.biao.util;

import java.util.UUID;

public class SuperBookAddrUtil {

    public static final String ADDR_PREFIX = "0x";

    public static String createAddr() {
        String addr1 = getUUID();
        String addr2 = getUUID();
        return ADDR_PREFIX + addr1 + addr2.substring(0, 8);
    }

    public static String createTxHash() {
        String addr1 = getUUID();
        String addr2 = getUUID();
        return ADDR_PREFIX  + addr1 + addr2;
    }

    private static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        System.out.println(createAddr());
        System.out.println(createTxHash());
        System.out.println("0x946d6cabf9827a9883c837d89ab1de83446bda9f0fec57c9303b20086936023b".length());
    }

}
