package com.biao.util;


public class CheckUtil {
    public static boolean checkEthAddress(String address) {
        if (address == null || address.length() != 42 || !address.startsWith("0x")) {
            return false;
        }
        return true;
    }

    public static boolean checkEthTxHash(String txHash) {
        if (txHash == null || txHash.length() != 66 || !txHash.startsWith("0x")) {
            return false;
        }
        return true;
    }

    public static boolean checkBtcAddress(String address) {
        if (address == null || address.length() != 34) {
            return false;
        }
        return true;

    }

    public static boolean checkBtcTxHash(String txHash) {
        if (txHash == null || txHash.length() != 64) {
            return false;
        }
        return true;
    }
}
