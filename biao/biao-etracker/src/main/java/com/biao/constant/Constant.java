package com.biao.constant;

import com.biao.entity.CoinAddress;
import com.biao.wallet.ethereum.Token;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constant {
    public static BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
    public static BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    public static HashMap<String, CoinAddress> ETH_ADDRESS_MAP = new HashMap<>();
    public static HashMap<String, String> TOKEN_ADDRESS_MAP = new HashMap<>();
    public static HashMap<String, Token> TOKEN_MAP = new HashMap<>();
    public static List<Token> TOKEN_LIST = new ArrayList<>();
}
