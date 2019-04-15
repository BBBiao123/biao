package com.biao.redis;

import com.biao.constant.RedisConstants;
import com.biao.constant.RedisKeyConstant;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.current.threadpool.BbexThreadPool;
import com.biao.entity.*;
import com.biao.entity.register.UserRegisterLottery;
import com.biao.entity.register.UserRegisterLotteryLimit;
import com.biao.entity.register.UserRegisterLotteryRule;
import com.biao.mapper.MainCnbDao;
import com.biao.mapper.MkAutoTradeUserDao;
import com.biao.mapper.UserCoinVolumeDao;
import com.biao.mapper.register.UserRegisterLotteryDao;
import com.biao.mapper.register.UserRegisterLotteryLimitDao;
import com.biao.mapper.register.UserRegisterLotteryRuleDao;
import com.biao.service.CoinService;
import com.biao.service.ExPairService;
import com.biao.util.SnowFlake;
import com.biao.vo.redis.MainCnbVO;
import com.biao.vo.redis.RedisExPairVO;
import com.biao.vo.redis.RedisMkAutoTradeUserVO;
import com.biao.vo.redis.RedisUserCoinVolume;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * redis缓存.
 *
 *  ""
 */
@Component
@SuppressWarnings("unchecked")
public class RedisCacheManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheManager.class);

    private final RedisTemplate redisTemplate;

    private final ExPairService exPairService;

    private final UserCoinVolumeDao userCoinVolumeDao;

    private final MkAutoTradeUserDao mkAutoTradeUserDao;

    private final MainCnbDao mainCnbDao;

    private final UserRegisterLotteryRuleDao registerLotteryRuleDao;

    private final UserRegisterLotteryLimitDao userRegisterLotteryLimitDao;

    private final UserRegisterLotteryDao userRegisterLotteryDao;

    private static final String USER_VOLUME_TYPE = "userVolume";

    private static final String EX_PAIR_TYPE = "exPair";

    @Autowired
    private CoinService coinService;

    @Autowired(required = false)
    public RedisCacheManager(final RedisTemplate redisTemplate,
                             final ExPairService exPairService,
                             final UserCoinVolumeDao userCoinVolumeDao,
                             final MkAutoTradeUserDao mkAutoTradeUserDao,
                             final MainCnbDao mainCnbDao, UserRegisterLotteryRuleDao registerLotteryRuleDao, UserRegisterLotteryDao userRegisterLotteryDao, UserRegisterLotteryLimitDao userRegisterLotteryLimitDao) {
        this.redisTemplate = redisTemplate;
        this.exPairService = exPairService;
        this.userCoinVolumeDao = userCoinVolumeDao;
        this.mkAutoTradeUserDao = mkAutoTradeUserDao;
        this.mainCnbDao = mainCnbDao;
        this.registerLotteryRuleDao = registerLotteryRuleDao;
        this.userRegisterLotteryDao = userRegisterLotteryDao;
        this.userRegisterLotteryLimitDao = userRegisterLotteryLimitDao;
    }


    /**
     * 获取redis缓存.
     *
     * @param key      redis key
     * @param supplier 异步执行函数
     * @return Object
     */
    @SuppressWarnings("unchecked")
    private Object cacheObject(final String key, final Supplier<Object> supplier, final String type) {
        //获取读锁
        Object object = null;
        try {
            //根据key获取redis集群中的对象数组
            object = redisTemplate.boundValueOps(key).get();
            if (Objects.isNull(object)) {
               /* if (EX_PAIR_TYPE.equals(type)) {
                    List<RedisExPairVO> vos = Lists.newArrayList();
                    redisTemplate.boundValueOps(key).set(vos);
                }*/
                object = supplier.get();
                if (Objects.nonNull(object)) {
                    redisTemplate.boundValueOps(key).set(object);
                }

            }
        } catch (Exception e) {
            try {
                LOGGER.error("读取缓存key:{},Exception:{}", key, e);
                object = supplier.get();
                redisTemplate.boundValueOps(key).set(object);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return object;
    }

    private Object cacheHashObject(final String key, final String hashKey, final Supplier<Object> supplier,
                                   final Supplier<Object> empty,
                                   final String type) {
        //获取读锁
        Object object = null;
        try {
            //根据key获取redis集群中的对象数组
            object = redisTemplate.opsForHash().get(key, hashKey);
            if (Objects.isNull(object)) {
                synchronized (this) {
                    object = supplier.get();
                    if (Objects.nonNull(object)) {
                        redisTemplate.opsForHash().put(key, hashKey, object);
                    } else {
                        if (type.equals(USER_VOLUME_TYPE) && empty != null) {
                            redisTemplate.opsForHash().put(key, hashKey, empty.get());
                        }
                    }
                }
            }
        } catch (Exception e) {
            try {
                LOGGER.error("读取缓存key:{},Exception:{}", key, e);
                object = supplier.get();
                redisTemplate.opsForHash().put(key, hashKey, object);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return object;
    }

    private RedisUserCoinVolume buildEmptyCoinVolume(String userId, String coinSymbol) {
        return new RedisUserCoinVolume();
    }

    public void cleanUserCoinVolume(String userId, String coinSymbol) {
        redisTemplate.opsForHash().delete(RedisKeyConstant.buildUserCoinVolumeKey(userId), coinSymbol);
    }

    public Boolean insertUserCoinVolume(final RedisUserCoinVolume redisUserCoinVolume) {
        if (redisUserCoinVolume.getVolume().compareTo(BigDecimal.ZERO) < 0
                || redisUserCoinVolume.getLockVolume().compareTo(BigDecimal.ZERO) < 0) {
            LOGGER.error("特别警告:{}插入资产错误！！！", redisUserCoinVolume.getUserId());
        }
        redisTemplate.opsForHash().put(
                RedisKeyConstant.buildUserCoinVolumeKey(redisUserCoinVolume.getUserId()),
                redisUserCoinVolume.getCoinSymbol(), redisUserCoinVolume);
        try {
            userCoinVolumeDao.insert(UserCoinVolume.transformToRedis(redisUserCoinVolume));
        } catch (Exception ex) {
            LOGGER.warn("BB资产同步到数据库失败了，不要管它了!:{}", redisUserCoinVolume, ex);
        }
        return Boolean.TRUE;
    }

    public Boolean updateUserCoinVolume(final RedisUserCoinVolume redisUserCoinVolume) {
        if (redisUserCoinVolume.getVolume().compareTo(BigDecimal.ZERO) < 0
                || redisUserCoinVolume.getLockVolume().compareTo(BigDecimal.ZERO) < 0) {
            LOGGER.error("特别警告:{}更新资产错误！！！{}", redisUserCoinVolume.getUserId(), redisUserCoinVolume);
            return Boolean.FALSE;
        }
        redisTemplate.opsForHash().put(
                RedisKeyConstant.buildUserCoinVolumeKey(redisUserCoinVolume.getUserId()),
                redisUserCoinVolume.getCoinSymbol(), redisUserCoinVolume);

        try {
            long l = userCoinVolumeDao.updateVolumeAndLockVolume(redisUserCoinVolume.getUserId(),
                    redisUserCoinVolume.getCoinSymbol(),
                    redisUserCoinVolume.getVolume(),
                    redisUserCoinVolume.getLockVolume());
            //如果修改失败，则需要新增一条数据.
            if (l <= 0) {
                if (StringUtils.isBlank(redisUserCoinVolume.getCoinId())) {
                    Coin coin = coinService.findByName(redisUserCoinVolume.getCoinSymbol());
                    redisUserCoinVolume.setCoinId(coin.getId());
                }
                //如果主键id为空，则设置一个新的.
                if (StringUtils.isBlank(redisUserCoinVolume.getId())) {
                    redisUserCoinVolume.setId(SnowFlake.createSnowFlake().nextIdString());
                }
                userCoinVolumeDao.insert(UserCoinVolume.transformToRedis(redisUserCoinVolume));
            }
        } catch (Exception ex) {
            LOGGER.warn("BB资产同步到数据库失败了，不要管它了!:{}", redisUserCoinVolume, ex);
        }
        return Boolean.TRUE;
    }

    public RedisUserCoinVolume acquireUserCoinVolume(final String userId, final String coinSymbol) {
        return (RedisUserCoinVolume) this.cacheHashObject(RedisKeyConstant.buildUserCoinVolumeKey(userId), coinSymbol,
                () -> buildRedisUserCoinVolume(userCoinVolumeDao.findByUserIdAndCoinSymbol(userId, coinSymbol)), () -> buildEmptyCoinVolume(userId, coinSymbol), USER_VOLUME_TYPE);

    }

    public List<RedisExPairVO> acquireAllExpair() {
        return (List<RedisExPairVO>)
                this.cacheObject(RedisConstants.TRADE_EX_PAIR, () -> buildByExpair(exPairService.findByList()), EX_PAIR_TYPE);
    }


    public List<UserRegisterLotteryRule> acquireLotteryRule(String lotteryId) {
        return (List<UserRegisterLotteryRule>)
                this.cacheObject(RedisConstants.REGISTER_LOTTERY_RULE, () -> registerLotteryRuleDao.findByLotteryId(lotteryId), EX_PAIR_TYPE);
    }

    public List<UserRegisterLotteryLimit> acquireLotteryLimit(String lotteryId) {
        return (List<UserRegisterLotteryLimit>)
                this.cacheObject(RedisConstants.REGISTER_LOTTERY_LIMIT, () -> userRegisterLotteryLimitDao.findByLotteryId(lotteryId), EX_PAIR_TYPE);
    }


    public UserRegisterLottery acquireRegisterLottery() {
        return (UserRegisterLottery) this.cacheObject(RedisConstants.REGISTER_LOTTERY,
                userRegisterLotteryDao::findOne, EX_PAIR_TYPE);
    }

    /**
     * 根据主交易区，交易币种或者交易对信息
     * 当币种未匹配，交易对不存在会返回 null.
     *
     * @param coinMain  主交易区
     * @param coinOther 被交易币种
     * @return {@linkplain RedisExPairVO}
     */
    public RedisExPairVO acquireExPair(final String coinMain, final String coinOther) {
        List<RedisExPairVO> allExpair = (List<RedisExPairVO>)
                this.cacheObject(RedisConstants.TRADE_EX_PAIR, () -> buildByExpair(exPairService.findByList()), EX_PAIR_TYPE);
        if (CollectionUtils.isEmpty(allExpair)) {
            return null;
        }
        return allExpair.stream()
                .filter(e -> e.getPairOne().equals(coinMain)
                        && e.getPairOther().equals(coinOther)).findFirst()
                .orElse(null);
    }

    public void remove(final String key) {
        redisTemplate.delete(key);
    }

    private RedisUserCoinVolume buildRedisUserCoinVolume(final UserCoinVolume userCoinVolume) {
        if (Objects.isNull(userCoinVolume)) {
            return null;
        }
        RedisUserCoinVolume redisVO = new RedisUserCoinVolume();
        redisVO.setId(userCoinVolume.getId());
        redisVO.setCoinId(userCoinVolume.getCoinId());
        redisVO.setCoinSymbol(userCoinVolume.getCoinSymbol());
        redisVO.setLockVolume(userCoinVolume.getLockVolume());
        redisVO.setUserId(userCoinVolume.getUserId());
        redisVO.setVolume(userCoinVolume.getVolume());
        redisVO.setFlag(userCoinVolume.getFlag());
        redisVO.setFlagMark(userCoinVolume.getFlagMark());
        return redisVO;
    }

    private List<RedisExPairVO> buildByExpair(final List<ExPair> exPairs) {
        return exPairs.stream().map(ex -> {
            RedisExPairVO redisExPairVO = new RedisExPairVO();
            redisExPairVO.setPairOne(ex.getPairOne());
            redisExPairVO.setPairOther(ex.getPairOther());
            redisExPairVO.setStatus(ex.getStatus());
            redisExPairVO.setSort(ex.getSort());
            redisExPairVO.setType(Integer.valueOf(ex.getType()));
            redisExPairVO.setFree(ex.getFee());
            redisExPairVO.setMaxVolume(ex.getMaxVolume());
            redisExPairVO.setMinVolume(ex.getMinVolume());
            redisExPairVO.setPricePrecision(ex.getPricePrecision());
            redisExPairVO.setVolumePrecision(ex.getVolumePrecision());
            redisExPairVO.setVolumePercent(ex.getVolumePercent());
            return redisExPairVO;
        }).collect(Collectors.toList());
    }

    private List<RedisMkAutoTradeUserVO> buildByMkAutoTradeUser(final List<MkAutoTradeUser> mkAutoTradeUsers) {
        return mkAutoTradeUsers.stream().map(mkAutoTradeUser -> {
            RedisMkAutoTradeUserVO redisMkAutoTradeUserVO = new RedisMkAutoTradeUserVO();
            redisMkAutoTradeUserVO.setUserId(mkAutoTradeUser.getUserId());
            redisMkAutoTradeUserVO.setIdCard(mkAutoTradeUser.getIdCard());
            redisMkAutoTradeUserVO.setMail(mkAutoTradeUser.getMail());
            redisMkAutoTradeUserVO.setMobile(mkAutoTradeUser.getMobile());
            redisMkAutoTradeUserVO.setRealName(mkAutoTradeUser.getRealName());
            return redisMkAutoTradeUserVO;
        }).collect(Collectors.toList());
    }

    public List<RedisMkAutoTradeUserVO> acquireAllMkAutoTradeUser() {
        return (List<RedisMkAutoTradeUserVO>)
                this.cacheObject(RedisConstants.MK_AUTO_TRADE_USER, () -> buildByMkAutoTradeUser(mkAutoTradeUserDao.findByList()), "");
    }

    private List<MainCnbVO> buildByMainCnb(final List<MainCnb> mainCnbs) {
        return mainCnbs.stream().map(mainCnb -> {
            MainCnbVO mainCnbVO = new MainCnbVO();
            mainCnbVO.setMainCoinId(mainCnb.getCoinId());
            mainCnbVO.setMainCoinSymbol(mainCnb.getCoinSymbol());
            mainCnbVO.setCnbRate(mainCnb.getCnbRate());
            return mainCnbVO;
        }).collect(Collectors.toList());
    }

    public List<MainCnbVO> acquireAllMainCnb() {
        return (List<MainCnbVO>)
                this.cacheObject(RedisConstants.MAIN_COIN_RATE_CNB, () -> buildByMainCnb(mainCnbDao.findAll()), "");
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
