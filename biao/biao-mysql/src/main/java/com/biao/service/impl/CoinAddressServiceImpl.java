package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.CoinAddress;
import com.biao.entity.DepositAddress;
import com.biao.exception.PlatException;
import com.biao.mapper.CoinAddressDao;
import com.biao.mapper.DepositAddressDao;
import com.biao.service.CoinAddressService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class CoinAddressServiceImpl implements CoinAddressService {

    @Autowired
    private CoinAddressDao coinAddressDao;
    @Autowired
    private DepositAddressDao depositAddressDao;

    @Override
    @Transactional
    public CoinAddress findByCoinId(String coinId, String userId) {
        CoinAddress coinAddress = coinAddressDao.findByCoinId(coinId);
        if (null == coinAddress) return null;
        coinAddress.setUserId(userId);
        coinAddress.setStatus("1");
        Timestamp updateDate = Timestamp.valueOf(coinAddress.getUpdateDate());
        long count = coinAddressDao.updateUserIdAndStatusByIdAndUpdateDate(userId, 1, coinAddress.getId(), updateDate);
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        //Long l = coinAddressDao.updateById(coinAddress);
        //还要将该地址设置为用户该币种的默认充币地址
        DepositAddress depositAddress = new DepositAddress();
        String id = SnowFlake.createSnowFlake().nextIdString();
        depositAddress.setId(id);
        depositAddress.setUserId(userId);
        depositAddress.setCoinId(coinId);
        depositAddress.setCoinSymbol(coinAddress.getSymbol());
        depositAddress.setAddress(coinAddress.getAddress());
        depositAddress.setStatus(0);
        depositAddress.setCreateDate(LocalDateTime.now());
        depositAddress.setUpdateDate(LocalDateTime.now());
        depositAddressDao.insert(depositAddress);
        return coinAddress;
    }
}
