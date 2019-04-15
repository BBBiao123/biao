package com.biao.service;

import com.biao.entity.Coin;

import java.util.List;

public interface CoinService {

    Coin findById(String id);

    /**
     * 根据币种名称获取
     *
     * @param name 币种名称
     * @return coin
     */
    Coin findByName(String name);

    List<Coin> findAll();

}
