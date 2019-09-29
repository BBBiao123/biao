package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.constant.RedisKeyConstant;
import com.biao.entity.Coin;
import com.biao.entity.UserCoinVolume;
import com.biao.enums.OrderEnum;
import com.biao.enums.UserCoinVolumeEventEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.CoinDao;
import com.biao.mapper.UserCoinVolumeDao;
import com.biao.pojo.UserCoinVolumeOpDTO;
import com.biao.redis.RedisCacheManager;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.SnowFlake;
import com.biao.util.TradeCompute;
import com.biao.vo.redis.RedisUserCoinVolume;
import com.google.common.base.Joiner;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * UserCoinVolumeExServiceImpl.
 * <p>
 * 用户资产的扩展处理.
 * <p>
 * 19-1-2下午3:35
 *
 *  "" sixh
 */
@Component
public class UserCoinVolumeExServiceImpl implements UserCoinVolumeExService {

    private Logger logger = LoggerFactory.getLogger(UserCoinVolumeExServiceImpl.class);

    @Autowired(required = false)
    private UserCoinVolumeDao userCoinVolumeDao;

    @Autowired(required = false)
    private CoinDao coinDao;

    @Autowired
    private RedissonClient rsclient;

    /**
     * The Redis cache manager.
     */
    @Autowired
    RedisCacheManager redisCacheManager;


    @Override
    public UserCoinVolume findByUserIdAndCoinSymbol(String userId, String symbol) {
        RedisUserCoinVolume redisUserCoinVolume = redisCacheManager.acquireUserCoinVolume(userId, symbol);
        if (Objects.isNull(redisUserCoinVolume) || StringUtils.isBlank(redisUserCoinVolume.getUserId())) {
            return null;
        }
        return UserCoinVolume.transformToRedis(redisUserCoinVolume);
    }

    /**
     * 当面要操作Volume 减少 与lock增加的时候，可以调用这个方法进行操作.
     * 如果在确定没有加锁的情况下，请不要调用这个方法.
     *
     * @return 结果.
     */
    private UpdateRsp volumeAction(UserCoinVolumeOpDTO dto,
                                   UserCoinVolume volume) {
        String userId = dto.getUserId();
        String symbol = dto.getSymbol();
        boolean isAddLockVolume = UserCoinVolumeEventEnum.ADD_LOCKVOLUME.check(dto.getOpSign());
        boolean isAddVolume = UserCoinVolumeEventEnum.ADD_VOLUME.check(dto.getOpSign());
        boolean isSubtractVolume = UserCoinVolumeEventEnum.SUB_VOLUME.check(dto.getOpSign());
        boolean isSubtractLockVolume = UserCoinVolumeEventEnum.SUB_LOCKVOLUME.check(dto.getOpSign());
        //日志记录一下.
        if (volume == null) {
            logger.warn("没有找到用户的资产信息{}", dto);
            return new UpdateRsp().setSuccess(false).setMsg("没有找到用户相关的资产信息.");
        }
        BigDecimal newVolume = Optional.ofNullable(volume.getVolume()).orElse(BigDecimal.ZERO);
        newVolume=newVolume.setScale(16,BigDecimal.ROUND_HALF_UP);
        //如果需要相减volume.
        if (isSubtractVolume) {
            //得到资产交易可能使用的最大所需资产数
            boolean expect = dto.getOpVolume().compareTo(newVolume) > 0;
            if (expect) {
                logger.warn("{}用户查询到资产{}相应的资产为{}超出范围！", userId, symbol, newVolume);
                return new UpdateRsp().setSuccess(false).setMsg("用户Volume资产超出范围");
            }
            newVolume = TradeCompute.subtract(newVolume, dto.getOpVolume());
        }
        if (isAddVolume) {
            newVolume = TradeCompute.add(newVolume, dto.getOpVolume());
        }
        //lockVolume.
        BigDecimal newBlockVole = Optional.ofNullable(volume.getLockVolume()).orElse(BigDecimal.ZERO);
        if (isSubtractLockVolume) {
            if (newBlockVole.compareTo(dto.getOpLockVolume()) < 0) {
                logger.warn("用户{}资产更改后lock_volume小于0:原{},需要减少的{}", userId, newBlockVole, dto.getOpLockVolume());
                return new UpdateRsp().setSuccess(false).setMsg("用户LockVolume资产超出范围");
            }
            newBlockVole = TradeCompute.subtract(newBlockVole, dto.getOpLockVolume());
            if (newBlockVole.compareTo(new BigDecimal(0.000000001)) < 0) {
                newBlockVole = BigDecimal.ZERO;
            }
        }
        if (isAddLockVolume) {
            newBlockVole = TradeCompute.add(newBlockVole, dto.getOpLockVolume());
        }
        return new UpdateRsp().setData(
                Pair.of(UpdateResult.of(dto.getOpLockVolume(), newBlockVole),
                        UpdateResult.of(dto.getOpVolume(), newVolume)))
                .setSuccess(true);
    }

    /**
     * 当面要操作Volume 减少 与lock增加的时候，可以调用这个方法进行操作.
     *
     * @return 结果.
     */
    @Override
    public long volumeActionByRlock(UserCoinVolumeOpDTO dto,
                                    boolean forceLock) {
        //这时用这个方法是没有问题了，只是为了符合以前的调用实现进行处理;
        boolean isAddLockVolume = UserCoinVolumeEventEnum.ADD_LOCKVOLUME.check(dto.getOpSign());
        boolean isSubtractVolume = UserCoinVolumeEventEnum.SUB_VOLUME.check(dto.getOpSign());
        boolean isSubtractLockVolume = UserCoinVolumeEventEnum.SUB_LOCKVOLUME.check(dto.getOpSign());
        if (dto.getOpSign().equals(UserCoinVolumeEventEnum.ADD_VOLUME.getEvent())) {
            return addVolume(dto, forceLock);
        } else if (isSubtractLockVolume || isSubtractVolume || isAddLockVolume) {
            return volumeActionByRlockNonAddCheck(dto, forceLock);
        }
        return 0L;
    }

    public long volumeActionByRlockNonAddCheck(UserCoinVolumeOpDTO dto,
                                               boolean forceLock) {
        return updateVolumeByRlock(dto, volume -> volumeAction(dto, volume), forceLock);
    }

    /**
     * Build singleton key.
     *
     * @param userCode the user code
     * @param symbol   the symbol
     * @return the string
     */
    private String rlockKey(String userCode, String symbol) {
        return Joiner.on("_").join("user_volume_op", userCode, symbol);
    }

    /**
     * Compute volume calvin response.
     * 分布式锁更新 volume 与 lockVolume
     *
     * @param opDTO   the op dto
     * @param funPair 处理lockVolume 与 volume ; getLeft:lockVolume getRight:volume.
     * @return the calvin response
     */
    private long updateVolumeByRlock(UserCoinVolumeOpDTO opDTO,
                                     Function<UserCoinVolume,
                                             UpdateRsp> funPair,
                                     boolean forceLock) {
        String userId = opDTO.getUserId();
        String symbol = opDTO.getSymbol();
        String key = rlockKey(userId, symbol);
        RLock lock = rsclient.getLock(key);
        //日志信息封装
        try {
            if (forceLock) {
                while (!lock.tryLock()) {
                    logger.warn("用户{}修改资产没有获得锁,继续拿锁...", userId);
                }
            } else {
                if (!lock.tryLock()) {
                    logger.warn("用户{}修改资产没有获得锁,非强制锁返回0...{}", userId, opDTO);
                    return 0L;
                }
            }
            //如果查询资产发生了错误.
            UserCoinVolume coinVolume = this.findByUserIdAndCoinSymbol(userId, symbol);
            UpdateRsp response = funPair.apply(coinVolume);
            if (!response.getSuccess()) {
                logger.warn("变更{}:{}的资产发生了错误,{}！", userId, symbol, response.getMsg());
                return 0L;
            }
            Pair<UpdateResult, UpdateResult> apply = response.getData();
            UpdateResult left = apply.getLeft();
            UpdateResult right = apply.getRight();
            BigDecimal newLockVolume = left.getVolume();
            BigDecimal newVolume = right.getVolume();
            BigDecimal beforeLockVolume = Optional.ofNullable(coinVolume)
                    .map(UserCoinVolume::getLockVolume)
                    .orElse(BigDecimal.ZERO);
            BigDecimal beforeVolume = Optional.ofNullable(coinVolume)
                    .map(UserCoinVolume::getVolume)
                    .orElse(BigDecimal.ZERO);

            if (!response.getIsUpdate()) {
                return response.getSuccess() ? 1L : 0L;
            }
            if (coinVolume != null) {
                RedisUserCoinVolume volume1 = new RedisUserCoinVolume();
                volume1.setId(coinVolume.getId());
                volume1.setCoinId(coinVolume.getCoinId());
                volume1.setUserId(userId);
                volume1.setCoinSymbol(symbol);
                volume1.setLockVolume(newLockVolume);
                volume1.setFlag(coinVolume.getFlag());
                volume1.setFlagMark(coinVolume.getFlagMark());
                volume1.setVolume(newVolume);
                Boolean aBoolean = redisCacheManager.updateUserCoinVolume(volume1);
                if (aBoolean) {
                    logger.info("修改用户{}:{}bb资产成功,修改lockVolume{}->{},修改volume{}-->{}", userId, symbol, beforeLockVolume, newLockVolume, beforeVolume, newVolume);
                } else {
                    logger.error("修改用户{}:{}bb资产失败,修改lockVolume{}->{},修改volume{}-->{}", userId, symbol, beforeLockVolume, newLockVolume, beforeVolume, newVolume);
                }
                return aBoolean ? 1L : 0L;
            }
        } catch (Exception ex) {
            logger.error("用户BB资产操作失败:{}", opDTO, ex);
            return 0L;
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return 0L;
    }

    /**
     * 增加Volume值.
     *
     * @param opDTO opDto.
     * @return response.
     */
    private long addVolume(UserCoinVolumeOpDTO opDTO, boolean forceLock) {
        if (!opDTO.getOpSign().equals(UserCoinVolumeEventEnum.ADD_VOLUME.getEvent())) {
            return 0L;
        }
        String userId = opDTO.getUserId();
        String coinSymbol = opDTO.getSymbol();
        return updateVolumeByRlock(opDTO, volume -> {
            if (volume == null || StringUtils.isEmpty(volume.getUserId())) {
                String id = SnowFlake.createSnowFlake().nextIdString();
                Coin coin = coinDao.findByName(coinSymbol);
                RedisUserCoinVolume su = new RedisUserCoinVolume();
                su.setId(id);
                su.setCoinId(coin.getId());
                su.setCoinSymbol(coinSymbol);
                su.setLockVolume(BigDecimal.ZERO);
                su.setVolume(opDTO.getOpVolume());
                su.setUserId(userId);
                su.setFlag((short) 0);
                boolean flag = redisCacheManager.insertUserCoinVolume(su);
                if (flag) {
                    logger.info("新增记录用户{}:{}bb资产成功,lockVolume{},volume{}", opDTO.getUserId(), opDTO.getSymbol(), opDTO.getOpLockVolume(), opDTO.getOpVolume());
                } else {
                    logger.error("新增记录用户{}:{}bb资产失败,lockVolume{},volume{}", opDTO.getUserId(), opDTO.getSymbol(), opDTO.getOpLockVolume(), opDTO.getOpVolume());
                }
                return new UpdateRsp().setData(
                        Pair.of(
                                UpdateResult.of(BigDecimal.ZERO, BigDecimal.ZERO),
                                UpdateResult.of(opDTO.getOpVolume(), opDTO.getOpVolume())))
                        .setIsUpdate(false)
                        .setSuccess(flag);
            } else {
                return volumeAction(opDTO, volume);
            }
        }, forceLock);
    }

    @Override
    public void updateById(UserCoinVolume userCoinVolume) {

    }

    @Override
    public List<UserCoinVolume> findAll(String userId) {
        final Map<String, RedisUserCoinVolume> entries = redisCacheManager.getRedisTemplate().opsForHash().entries(RedisKeyConstant.buildUserCoinVolumeKey(userId));
        return entries.values().stream().filter(e -> StringUtils.isNotBlank(e.getUserId()))
                .map(UserCoinVolume::transformToRedis).collect(Collectors.toList());
    }

    @Override
    public long updateSpent(OrderEnum.OrderStatus status, BigDecimal spent, BigDecimal refundVolume, String userId, String coinSymbol) {
        int opSign = UserCoinVolumeEventEnum.parseEvent(
                UserCoinVolumeEventEnum.SUB_LOCKVOLUME,
                UserCoinVolumeEventEnum.ADD_VOLUME);
        UserCoinVolumeOpDTO opDTO = new UserCoinVolumeOpDTO();
        opDTO.setUserId(userId);
        opDTO.setSymbol(coinSymbol);
        opDTO.setOpLockVolume(spent);
        opDTO.setOpVolume(refundVolume);
        opDTO.setOpSign(opSign);
        return volumeActionByRlockNonAddCheck(opDTO, true);
    }

    @Override
    public long updateIncome(OrderEnum.OrderStatus status, BigDecimal income, String userId, String coinSymbol) {
        return updateIncome(status, income, userId, coinSymbol, true);
    }

    @Override
    public long updateIncome(OrderEnum.OrderStatus status, BigDecimal income, String userId, String coinSymbol, boolean force) {
        UserCoinVolumeOpDTO opDTO = new UserCoinVolumeOpDTO();
        opDTO.setUserId(userId);
        opDTO.setSymbol(coinSymbol);
        opDTO.setOpLockVolume(BigDecimal.ZERO);
        opDTO.setOpVolume(income);
        opDTO.setOpSign(UserCoinVolumeEventEnum.parseEvent(UserCoinVolumeEventEnum.ADD_VOLUME));
        return addVolume(opDTO, force);
    }

    @Override
    public void updateIncomeException(OrderEnum.OrderStatus status, BigDecimal income, String userId, String coinSymbol) {
        long resultStatus = updateIncome(status, income, userId, coinSymbol, false);
        if (resultStatus != 1L) {
            throw new PlatException(Constants.UPDATE_ERROR, "常规账户加资产更新失败");
        }
    }

    @Override
    public long updateOutcome(OrderEnum.OrderStatus status, BigDecimal outCome, String userId, String coinSymbol) {
        return this.updateOutcome(status, outCome, userId, coinSymbol, true);
    }

    @Override
    public long updateOutcome(OrderEnum.OrderStatus status, BigDecimal outCome, String userId, String coinSymbol, boolean force) {
        int opSign = UserCoinVolumeEventEnum.parseEvent(
                UserCoinVolumeEventEnum.SUB_VOLUME);
        UserCoinVolumeOpDTO opDTO = new UserCoinVolumeOpDTO();
        opDTO.setUserId(userId);
        opDTO.setSymbol(coinSymbol);
        opDTO.setOpLockVolume(outCome);
        opDTO.setOpVolume(outCome);
        opDTO.setOpSign(opSign);
        return volumeActionByRlockNonAddCheck(opDTO, force);
    }

    @Override
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
    public long addLockVolume(String userId, String symbol, BigDecimal blockVolume, boolean isSubtractVolume) {
        int opSign = isSubtractVolume ? UserCoinVolumeEventEnum.parseEvent(
                UserCoinVolumeEventEnum.SUB_VOLUME,
                UserCoinVolumeEventEnum.ADD_LOCKVOLUME)
                : UserCoinVolumeEventEnum.parseEvent(UserCoinVolumeEventEnum.ADD_LOCKVOLUME);
        UserCoinVolumeOpDTO opDTO = new UserCoinVolumeOpDTO();
        opDTO.setUserId(userId);
        opDTO.setSymbol(symbol);
        opDTO.setOpLockVolume(blockVolume);
        opDTO.setOpVolume(blockVolume);
        opDTO.setOpSign(opSign);
        return volumeActionByRlockNonAddCheck(opDTO, false);
    }

    @Override
    public long subtractLockVolume(String userId, String symbol, BigDecimal blockVolume, boolean isAddVolume) {
        return subtractLockVolume(userId, symbol, blockVolume, isAddVolume, true);
    }

    @Override
    public long subtractLockVolume(String userId, String symbol, BigDecimal blockVolume, boolean isAddVolume, boolean force) {
        int opSign = isAddVolume ? UserCoinVolumeEventEnum.parseEvent(
                UserCoinVolumeEventEnum.SUB_LOCKVOLUME,
                UserCoinVolumeEventEnum.ADD_VOLUME)
                : UserCoinVolumeEventEnum.parseEvent(UserCoinVolumeEventEnum.SUB_LOCKVOLUME);
        UserCoinVolumeOpDTO opDTO = new UserCoinVolumeOpDTO();
        opDTO.setUserId(userId);
        opDTO.setSymbol(symbol);
        opDTO.setOpLockVolume(blockVolume);
        opDTO.setOpVolume(blockVolume);
        opDTO.setOpSign(opSign);
        return volumeActionByRlockNonAddCheck(opDTO, force);
    }


    /**
     * The type Update result.
     * lockVolume: opVolume is opLockVolume, volume is lockVolume.
     * volume: opVolume is opVolume , volume is volume.
     *
     *  "" sixh
     */
    @Getter
    public static final class UpdateResult {
        /**
         * The Op volume.
         */
        private BigDecimal opVolume;

        /**
         * The Volume.
         */
        private BigDecimal volume;

        /**
         * Instantiates a new Update result.
         *
         * @param opVolume the op volume
         * @param volume   the volume
         */
        private UpdateResult(BigDecimal opVolume, BigDecimal volume) {
            this.opVolume = opVolume;
            this.volume = volume;
        }

        /**
         * Of update result.
         *
         * @param opVolume the op volume
         * @param volume   the volume
         * @return the update result
         */
        public static UpdateResult of(BigDecimal opVolume, BigDecimal volume) {
            return new UpdateResult(opVolume, volume);
        }
    }

    /**
     * The type Update rsp.
     * Pair left: op lockVolume.
     * Pair right: op volume.
     *
     *  "" sixh
     * @see UpdateResult
     */
    @Getter
    public final class UpdateRsp {

        /**
         * The Is update.
         */
        private Boolean isUpdate = true;

        /**
         * 是否完成.
         */
        private Boolean success = false;

        /**
         * 显示的msg.
         */
        private String msg = "";

        /**
         * success data.
         */
        private Pair<UpdateResult, UpdateResult> data;

        /**
         * Sets is update.
         *
         * @param f the f
         * @return the is update
         */
        public UpdateRsp setIsUpdate(boolean f) {
            this.isUpdate = f;
            return this;
        }

        /**
         * Gets data.
         *
         * @return the data
         */
        public Pair<UpdateResult, UpdateResult> getData() {
            return data;
        }

        /**
         * Sets data.
         *
         * @param data the data
         * @return the data
         */
        public UpdateRsp setData(Pair<UpdateResult, UpdateResult> data) {
            this.data = data;
            return this;
        }

        /**
         * Sets success.
         *
         * @param success the success
         * @return the success
         */
        public UpdateRsp setSuccess(Boolean success) {
            this.success = success;
            return this;
        }

        public UpdateRsp setMsg(String msg) {
            this.msg = msg;
            return this;
        }
    }

}
