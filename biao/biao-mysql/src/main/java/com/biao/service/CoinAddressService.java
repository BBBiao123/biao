package com.biao.service;

import com.biao.entity.CoinAddress;

public interface CoinAddressService {

    CoinAddress findByCoinId(String coinId, String userId);

}
