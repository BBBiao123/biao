package com.biao.service.impl;

import com.biao.entity.DepositAddress;
import com.biao.mapper.DepositAddressDao;
import com.biao.service.DepositAddressService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DepositAddressServiceImpl implements DepositAddressService {

    @Autowired
    private DepositAddressDao depositAddressDao;

    @Override
    public String save(DepositAddress withdrawAddress) {
        String id = SnowFlake.createSnowFlake().nextIdString();
        withdrawAddress.setStatus(0);
        withdrawAddress.setCreateDate(LocalDateTime.now());
        withdrawAddress.setUpdateDate(LocalDateTime.now());
        withdrawAddress.setId(id);
        depositAddressDao.insert(withdrawAddress);
        return id;
    }

    @Override
    public void updateById(DepositAddress depositAddress) {
    }

    @Override
    public DepositAddress findByUserIdAndCoinId(String userId, String coinId) {
        return depositAddressDao.findByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public DepositAddress findById(String id) {
        return depositAddressDao.findById(id);
    }


}
