package com.biao.service.impl.balance;

import com.biao.entity.balance.BalancePlatJackpotVolumeDetail;
import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.mapper.balance.BalancePlatJackpotVolumeDetailDao;
import com.biao.mapper.balance.BalanceUserCoinVolumeDao;
import com.biao.service.balance.BalancePlatJackpotVolumeService;
import com.biao.service.balance.BalanceUserCoinVolumeService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *余币宝对应service
 */
@Service
public class BalancePlatJackpotVolumeServiceImpl implements BalancePlatJackpotVolumeService {

    @Autowired(required = false)
    private BalancePlatJackpotVolumeDetailDao balanceUserCoinVolumeDao;


    @Override
    public String save(BalancePlatJackpotVolumeDetail balanceUserCoinVolume) {
        String id = SnowFlake.createSnowFlake().nextIdString();

        balanceUserCoinVolume.setId(id);
        balanceUserCoinVolumeDao.insert(balanceUserCoinVolume);
        return id;
    }
    @Override
    public void updateById(BalancePlatJackpotVolumeDetail balanceUserCoinVolume) {
        balanceUserCoinVolumeDao.updateById(balanceUserCoinVolume);
    }

    @Override
    public List<BalancePlatJackpotVolumeDetail> findAll(String userId) {
        return balanceUserCoinVolumeDao.findByUserIdAndCoin(userId);
    }


}
