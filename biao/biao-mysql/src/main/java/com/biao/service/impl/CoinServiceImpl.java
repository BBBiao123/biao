package com.biao.service.impl;

import com.biao.entity.Coin;
import com.biao.mapper.CoinDao;
import com.biao.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinDao coinDao;

    @Override
    public Coin findById(String id) {
        return coinDao.findById(id);
    }

    /**
     * 根据币种名称获取
     *
     * @param name 币种名称
     * @return coin
     */
    @Override
    public Coin findByName(String name) {
        return coinDao.findByName(name);
    }

    @Override
    public List<Coin> findAll() {
        return coinDao.findAll();
    }


}
