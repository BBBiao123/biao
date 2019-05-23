package com.biao.service.impl.balance;

import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.mapper.balance.BalanceUserCoinVolumeDao;
import com.biao.service.balance.BalanceUserCoinVolumeService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *余币宝对应service
 */
@Service
public class BalanceUserCoinVolumeServiceImpl implements BalanceUserCoinVolumeService {

    @Autowired(required = false)
    private BalanceUserCoinVolumeDao balanceUserCoinVolumeDao;


    @Override
    public String save(BalanceUserCoinVolume balanceUserCoinVolume) {
        String id = SnowFlake.createSnowFlake().nextIdString();

        balanceUserCoinVolume.setId(id);
        balanceUserCoinVolumeDao.insert(balanceUserCoinVolume);
        return id;
    }
    @Override
    public void updateById(BalanceUserCoinVolume balanceUserCoinVolume) {
        balanceUserCoinVolumeDao.updateById(balanceUserCoinVolume);
    }

    @Override
    public List<BalanceUserCoinVolume> findAll(String userId) {
        return balanceUserCoinVolumeDao.findByUserId(userId);
    }

    @Override
    public List<BalanceUserCoinVolume> findByRank() {
        return balanceUserCoinVolumeDao.findByRank();
    }

    @Override
    public List<BalanceUserCoinVolume>  findByUserIdAndCoin(String userId,String coinSymbol){
        return  balanceUserCoinVolumeDao.findByUserIdAndCoin(userId,coinSymbol);
    }

    @Override
    public int findByCountNum(){
        return balanceUserCoinVolumeDao.findByCountNum();
    }

}
