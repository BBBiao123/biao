package com.bbex.constant;

import com.bbex.entity.CoinAddress;

import java.util.HashMap;

public class Constant {
    public static HashMap<String, CoinAddress> BTC_ADDRESS_MAP = new HashMap<>();
    public static HashMap<String, CoinAddress> LTC_ADDRESS_MAP = new HashMap<>();
    public static HashMap<String, CoinAddress> BBC_ADDRESS_MAP = new HashMap<>();
    public static HashMap<String, CoinAddress> USDT_ADDRESS_MAP = new HashMap<>();
    public static Integer ADDRESS_TYPE_BTC = 2;
    public static Integer ADDRESS_TYPE_LTC = 3;
    public static Integer ADDRESS_TYPE_BBC = 4;
}
