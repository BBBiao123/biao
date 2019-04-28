package com.biao.service.impl.balance;

import com.biao.constant.Constants;
import com.biao.entity.Coin;
import com.biao.entity.UserCoinVolume;
import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.enums.OrderEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.CoinDao;
import com.biao.mapper.UserCoinVolumeDao;
import com.biao.mapper.balance.BalanceUserCoinVolumeDao;
import com.biao.pojo.UserCoinVolumeOpDTO;
import com.biao.redis.RedisCacheManager;
import com.biao.service.UserCoinVolumeExService;
import com.biao.service.balance.BalanceUserCoinVolumeService;
import com.biao.util.SnowFlake;
import com.biao.util.TradeCompute;
import com.biao.vo.redis.RedisUserCoinVolume;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;

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


}
