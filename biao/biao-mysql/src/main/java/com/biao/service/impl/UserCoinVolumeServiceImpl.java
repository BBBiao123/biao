package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.Coin;
import com.biao.entity.UserCoinVolume;
import com.biao.enums.OrderEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.CoinDao;
import com.biao.mapper.UserCoinVolumeDao;
import com.biao.pojo.UserCoinVolumeOpDTO;
import com.biao.redis.RedisCacheManager;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.SnowFlake;
import com.biao.util.TradeCompute;
import com.biao.vo.redis.RedisUserCoinVolume;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;

/**
 *
 */
//@Service
public class UserCoinVolumeServiceImpl implements UserCoinVolumeExService {

    @Autowired(required = false)
    private UserCoinVolumeDao userCoinVolumeDao;

    @Autowired(required = false)
    private CoinDao coinDao;

    @Autowired
    private RedissonClient rsclient;

    @Autowired
    RedisCacheManager redisCacheManager;

    private Logger logger = LoggerFactory.getLogger(UserCoinVolumeServiceImpl.class);

    @Override
    public void updateById(UserCoinVolume userCoinVolume) {

    }

    @Override
    public List<UserCoinVolume> findAll(String userId) {
        return userCoinVolumeDao.findByUserId(userId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addLockVolume(String userId, String symbol, BigDecimal blockVolume, boolean isSubtractVolume) {
        String key = getKey(userId, symbol);
        Lock lock = rsclient.getLock(key);
        lock.lock();
        try {
            UserCoinVolume volume = this.findByUserIdAndCoinSymbol(userId, symbol);
            if(volume == null) {
                logger.warn("addLockVolume{}用户没有查询到资产{}相应的资产为！", userId, symbol);
                return 0L;
            }
            //得到资产交易可能使用的最大所需资产数
            boolean expect = blockVolume.compareTo(volume.getVolume()) > 0;
            if (expect) {
                logger.warn("{}用户查询到资产{}相应的资产为{}超出范围！", userId, symbol, volume.getVolume());
                return 0L;
            }
            BigDecimal decimal = blockVolume.setScale(16, BigDecimal.ROUND_HALF_UP);
            BigDecimal newBlockVole = TradeCompute.add(volume.getLockVolume(), decimal);
            BigDecimal newVolume = volume.getVolume();
            if (isSubtractVolume) {
                newVolume = TradeCompute.subtract(volume.getVolume(), decimal);
            }
            RedisUserCoinVolume volume1 = new RedisUserCoinVolume();
            volume1.setId(volume.getId());
            volume1.setCoinId(volume.getCoinId());
            volume1.setUserId(userId);
            volume1.setCoinSymbol(symbol);
            volume1.setLockVolume(newBlockVole);
            volume1.setFlag(volume.getFlag());
            volume1.setFlagMark(volume.getFlagMark());
            volume1.setVolume(newVolume);
            return redisCacheManager.updateUserCoinVolume(volume1) ? 1L : 0L;
        } catch (Exception ex) {
            logger.error("addLockVolume 同步BB资产到Redis失败了,你要管一下哦.{},blockVolume->{},coinSymbol->{},isSubtractVolume->{}",
                    userId,
                    blockVolume,
                    symbol,
                    isSubtractVolume,
                    ex);
            return 0L;
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long subtractLockVolume(String userId, String symbol, BigDecimal blockVolume, boolean isAddVolume) {
        return subtractLockVolume(userId, symbol, blockVolume, BigDecimal.ZERO, isAddVolume, true);
    }

    @Override
    public long subtractLockVolume(String userId, String symbol, BigDecimal blockVolume, boolean isAddVolume, boolean force) {
        return subtractLockVolume(userId, symbol, blockVolume, BigDecimal.ZERO, isAddVolume, force);
    }

    @Override
    public long volumeActionByRlock(UserCoinVolumeOpDTO dto, boolean forceLock) {
        return 0;
    }

    private long subtractLockVolume(String userId,
                                    String symbol,
                                    BigDecimal blockVolume,
                                    BigDecimal refundVolume,
                                    boolean isAddVolume,
                                    boolean force) {
        String key = getKey(userId, symbol);
        RLock lock = rsclient.getLock(key);
        if (force) {
            while (!lock.tryLock()) {
            }
        } else {
            //如果没有拿到锁直接返回.
            if (!lock.tryLock()) {
                return 0L;
            }
        }
        try {
            UserCoinVolume volume = this.findByUserIdAndCoinSymbol(userId, symbol);
            if(volume == null) {
                logger.warn("subtractLockVolume{} 用户没有查询到{}相应的资产为！", userId, symbol);
                return 0L;
            }
            BigDecimal decimal = blockVolume.setScale(12, BigDecimal.ROUND_HALF_UP);
            if (volume.getLockVolume().compareTo(decimal) < 0) {
                logger.warn("用户{}资产更改后lock_volume小于0:原{},需要减少的{}",userId, volume.getLockVolume(), decimal);
                return 0L;
            }
            BigDecimal newBlockVole = TradeCompute.subtract(volume.getLockVolume(), decimal);
            if (newBlockVole.compareTo(new BigDecimal(0.000000001)) < 0) {
                newBlockVole = BigDecimal.ZERO;
            }
            BigDecimal newVolume;
            if (isAddVolume) {
                newVolume = TradeCompute.add(volume.getVolume(), decimal);
            } else {
                newVolume = TradeCompute.add(volume.getVolume(), refundVolume);
            }
            RedisUserCoinVolume volume1 = new RedisUserCoinVolume();
            volume1.setId(volume.getId());
            volume1.setCoinId(volume.getCoinId());
            volume1.setUserId(userId);
            volume1.setCoinSymbol(symbol);
            volume1.setLockVolume(newBlockVole);
            volume1.setFlag(volume.getFlag());
            volume1.setFlagMark(volume.getFlagMark());
            volume1.setVolume(newVolume);
            return redisCacheManager.updateUserCoinVolume(volume1) ? 1L : 0L;
        } catch (Exception ex) {
            logger.error("subtractLockVolume 同步BB资产到Redis失败了,你要管一下哦.{},blockVolume->{},refundVolume->{},coinSymbol->{},isAddVolume->{}",
                    userId,
                    blockVolume,
                    refundVolume,
                    symbol,
                    isAddVolume,
                    ex);
            return 0L;
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateSpent(OrderEnum.OrderStatus status, BigDecimal spent, BigDecimal refundVolume, String userId, String coinSymbol) {
        return subtractLockVolume(userId, coinSymbol, spent, refundVolume, false, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateIncome(OrderEnum.OrderStatus status, BigDecimal income, String userId, String coinSymbol) {
        return updateIncome(status, income, userId, coinSymbol, true);
    }

    @Override
    public long updateIncome(OrderEnum.OrderStatus status, BigDecimal income, String userId, String coinSymbol, boolean force) {
        String key = getKey(userId, coinSymbol);
        RLock lock = rsclient.getLock(key);
//        while (!lock.tryLock()) {
//        }
        if (force) {
            while (!lock.tryLock()) {
            }
        } else {
            //如果没有拿到锁直接返回.
            if (!lock.tryLock()) {
                return 0L;
            }
        }
        try {
            UserCoinVolume volume = this.findByUserIdAndCoinSymbol(userId, coinSymbol);
            if (volume == null || StringUtils.isEmpty(volume.getUserId())) {
                String id = SnowFlake.createSnowFlake().nextIdString();
                Coin coin = coinDao.findByName(coinSymbol);
                RedisUserCoinVolume su = new RedisUserCoinVolume();
                su.setId(id);
                su.setCoinId(coin.getId());
                su.setCoinSymbol(coinSymbol);
                su.setLockVolume(BigDecimal.ZERO);
                su.setVolume(income);
                su.setUserId(userId);
                su.setFlag((short) 0);
                return redisCacheManager.insertUserCoinVolume(su) ? 1L : 0L;
            } else {
                BigDecimal newVolume = TradeCompute.add(volume.getVolume(), income);
                RedisUserCoinVolume volume1 = new RedisUserCoinVolume();
                volume1.setId(volume.getId());
                volume1.setCoinId(volume.getCoinId());
                volume1.setUserId(userId);
                volume1.setCoinSymbol(volume.getCoinSymbol());
                volume1.setLockVolume(volume.getLockVolume());
                volume1.setVolume(newVolume);
                volume1.setFlag(volume.getFlag());
                volume1.setFlagMark(volume.getFlagMark());
                return redisCacheManager.updateUserCoinVolume(volume1) ? 1L : 0L;
            }
        }catch (Exception ex){
            logger.error("updateIncome 同步BB资产到Redis失败了,你要管一下哦.{},income->{},coinSymbol->{}",userId, income, coinSymbol, ex);
            return 0L;
        }finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateIncomeException(OrderEnum.OrderStatus status, BigDecimal income, String userId, String coinSymbol) {
        long resultStatus = updateIncome(status, income, userId, coinSymbol, false);
        if (resultStatus != 1L) {
            throw new PlatException(Constants.UPDATE_ERROR, "常规账户加资产更新失败");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateOutcome(OrderEnum.OrderStatus status, BigDecimal outCome, String userId, String coinSymbol) {
        return this.updateOutcome(status, outCome, userId, coinSymbol, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateOutcome(OrderEnum.OrderStatus status, BigDecimal outCome, String userId, String coinSymbol, boolean force) {
        String key = getKey(userId, coinSymbol);
        RLock lock = rsclient.getLock(key);
//        while (!lock.tryLock()) {
//        }
        if (force) {
            while (!lock.tryLock()) {
            }
        } else {
            //如果没有拿到锁直接返回.
            if (!lock.tryLock()) {
                return 0L;
            }
        }
        try {
            UserCoinVolume volume = this.findByUserIdAndCoinSymbol(userId, coinSymbol);
            if (volume == null || StringUtils.isEmpty(volume.getUserId()) || volume.getVolume().compareTo(outCome) < 0) {
                return 0L;
            } else {
                BigDecimal newVolume = TradeCompute.subtract(volume.getVolume(), outCome);
                RedisUserCoinVolume volume1 = new RedisUserCoinVolume();
                volume1.setId(volume.getId());
                volume1.setCoinId(volume.getCoinId());
                volume1.setUserId(userId);
                volume1.setCoinSymbol(volume.getCoinSymbol());
                volume1.setLockVolume(volume.getLockVolume());
                volume1.setVolume(newVolume);
                volume1.setFlag(volume.getFlag());
                volume1.setFlagMark(volume.getFlagMark());
                return redisCacheManager.updateUserCoinVolume(volume1) ? 1L : 0L;
            }
        } catch (Exception ex){
            logger.error("updateOutcom 同步BB资产到Redis失败了,你要管一下哦.{},outCome->{},coinSymbol->{}",userId, outCome, coinSymbol, ex);
            return 0L;
        }finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOutcomeException(OrderEnum.OrderStatus status, BigDecimal outCome, String userId, String coinSymbol) {
        long resultStatus = updateOutcome(status, outCome, userId, coinSymbol, false);
        if (resultStatus != 1L) {
            throw new PlatException(Constants.UPDATE_ERROR, "常规账户减资产更新失败");
        }
    }

    @Override
    public UserCoinVolume findByUserIdAndCoinId(String userId, String coinId) {
        return userCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public UserCoinVolume findByUserIdAndCoinSymbol(String userId, String coinSymbol) {
        RedisUserCoinVolume redisUserCoinVolume = redisCacheManager.acquireUserCoinVolume(userId, coinSymbol);
        if (Objects.isNull(redisUserCoinVolume) || StringUtils.isBlank(redisUserCoinVolume.getUserId())) {
            return null;
        }
        return UserCoinVolume.transformToRedis(redisUserCoinVolume);
    }

    private String getKey(String userId, String symbol) {
        return "lock_user_coin_" + userId + symbol;
    }

}
