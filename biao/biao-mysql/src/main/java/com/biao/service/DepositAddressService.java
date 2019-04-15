package com.biao.service;

import com.biao.entity.DepositAddress;

public interface DepositAddressService {

    DepositAddress findById(String id);

    String save(DepositAddress depositAddress);

    void updateById(DepositAddress depositAddress);

    DepositAddress findByUserIdAndCoinId(String userId, String coinId);
}
