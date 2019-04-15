package com.biao.util;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

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

    public static boolean checkBtcAddress(String input) {
        try {
            NetworkParameters networkParameters = null;
            networkParameters = MainNetParams.get();
            Address address = Address.fromBase58(networkParameters, input);
            if (address != null)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean checkBtcTxHash(String txHash) {
        if (txHash == null || txHash.length() != 64) {
            return false;
        }
        return true;
    }
}
