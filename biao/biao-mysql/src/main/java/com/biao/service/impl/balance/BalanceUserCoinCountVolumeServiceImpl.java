package com.biao.service.impl.balance;

import com.biao.entity.balance.BalanceUserCoinCountVolume;
import com.biao.mapper.balance.BalanceUserCoinCountVolumeDao;
import com.biao.service.balance.BalanceUserCoinCountVolumeService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *余币宝对应service
 */
@Service
public class BalanceUserCoinCountVolumeServiceImpl implements BalanceUserCoinCountVolumeService {

    @Autowired(required = false)
    private BalanceUserCoinCountVolumeDao balanceUserCoinVolumeDao;


    @Override
    public String save(BalanceUserCoinCountVolume balanceUserCoinVolume) {
        String id = SnowFlake.createSnowFlake().nextIdString();
        balanceUserCoinVolume.setId(id);
        balanceUserCoinVolumeDao.insert(balanceUserCoinVolume);
        return id;
    }
    @Override
    public void updateById(BalanceUserCoinCountVolume balanceUserCoinVolume) {
        balanceUserCoinVolumeDao.updateById(balanceUserCoinVolume);
    }

    @Override
    public List<BalanceUserCoinCountVolume> findAll(String userId) {
        return balanceUserCoinVolumeDao.findByUserId(userId);
    }

    @Override
    public List<BalanceUserCoinCountVolume> findByRank() {
        return balanceUserCoinVolumeDao.findByRank();
    }

    @Override
    public List<BalanceUserCoinCountVolume>  findByUserIdAndCoin(String userId,String coinSymbol){
        return  balanceUserCoinVolumeDao.findByUserIdAndCoin(userId,coinSymbol);
    }

    @Override
    public int findByCountNum(){
        return balanceUserCoinVolumeDao.findByCountNum();
    }

}
