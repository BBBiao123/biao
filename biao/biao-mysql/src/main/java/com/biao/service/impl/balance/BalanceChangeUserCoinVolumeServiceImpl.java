package com.biao.service.impl.balance;

import com.biao.entity.balance.BalanceChangeUserCoinVolume;
import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.mapper.balance.BalanceChangeUserCoinVolumeDao;
import com.biao.service.balance.BalanceChangeUserCoinVolumeService;
import com.biao.service.balance.BalanceUserCoinVolumeService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 *余币宝对应service
 */
@Service
public class BalanceChangeUserCoinVolumeServiceImpl implements BalanceChangeUserCoinVolumeService {

    @Autowired(required = false)
    private BalanceChangeUserCoinVolumeDao balanceUserCoinVolumeDao;


    @Override
    public String save(BalanceChangeUserCoinVolume balanceUserCoinVolume) {
        String id = SnowFlake.createSnowFlake().nextIdString();
        balanceUserCoinVolume.setId(id);
        balanceUserCoinVolume.setCreateDate(LocalDateTime.now());
        balanceUserCoinVolumeDao.insert(balanceUserCoinVolume);
        return id;
    }
    @Override
    public void updateById(BalanceChangeUserCoinVolume balanceUserCoinVolume) {
        balanceUserCoinVolumeDao.updateById(balanceUserCoinVolume);
    }

    @Override
    public List<BalanceChangeUserCoinVolume> findAll() {
        return balanceUserCoinVolumeDao.findAll();
    }


}
