package com.biao.previous.cache;

import com.biao.pojo.TradeUserFeeNotify;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UserFeeCache .
 * 保存用的手续费用.
 * ctime: 2018/8/31 14:29
 *
 *  "" sixh
 */
public enum UserFeeCache {
    /**
     * 实体.
     */
    INST;

    private static final Logger logger = LoggerFactory.getLogger(UserFeeCache.class);
    /**
     * 保存前端变更通知过来的信息变化.
     */
    private final Map<String, TradeUserFeeNotify> notifyMap = new ConcurrentHashMap<>();

    /**
     * put 一个缓存信息.
     */
    public void put(TradeUserFeeNotify userFee) {
        if (!check(userFee)) {
            return;
        }
        //属性为status true.
        if (!userFee.getStatus()) {
            return;
        }
        //构建一个缓存key.
        String key = Joiner.on(":").join(userFee.getUserId(), userFee.getPairOne(), userFee.getPairOther());
        notifyMap.put(key, userFee);
        logger.info("{}写入的手续费缓存中..", userFee);
    }

    /**
     * 校验.
     *
     * @param userFee userFee对象.
     * @return true or false;
     */
    private boolean check(TradeUserFeeNotify userFee) {
        if (userFee == null) {
            logger.error("传入缓存的是 is null");
            return false;
        }
        /*
         * 以下的user_id pair_one pair_other 不允许为空.
         */
        if (StringUtils.isBlank(userFee.getUserId())) {
            logger.error("传入缓存user_id is null");
            return false;
        }
        if (StringUtils.isBlank(userFee.getPairOne())) {
            logger.error("传入缓存pair_one is null");
            return false;
        }
        if (StringUtils.isBlank(userFee.getPairOther())) {
            logger.error("传入缓存pair_other is null");
            return false;
        }
        return true;
    }

    /**
     * 从缓存中获取相关的数据.
     *
     * @return keys.
     */
    private TradeUserFeeNotify get(TradeUserFeeNotify userFee) {
        if (!check(userFee)) {
            return null;
        }
        //构建一个缓存key.
        String key = Joiner.on(":").join(userFee.getUserId(), userFee.getPairOne(), userFee.getPairOther());
        return notifyMap.get(key);
    }

    /**
     * 清除一个userFeeNotify对象.
     *
     * @param userFee 需要清除的对象.
     */
    public void remove(TradeUserFeeNotify userFee) {
        if (!check(userFee)) {
            return;
        }
        //构建一个缓存key.
        String key = Joiner.on(":").join(userFee.getUserId(), userFee.getPairOne(), userFee.getPairOther());
        TradeUserFeeNotify remove = notifyMap.remove(key);
        logger.info("{}移除的手续费缓存中..", remove);
    }

    /**
     * 操作缓存处理.
     * op: status is true for put.
     * op: status is false for remove.
     *
     * @param userFee userFee.
     */
    public static void op(TradeUserFeeNotify userFee) {
        if (userFee == null) {
            logger.error("传入缓存的是 is null");
            return;
        }
        //判断....根据不同的status判断写入还是移队.
        if (userFee.getStatus()) {
            UserFeeCache.INST.put(userFee);
        } else {
            UserFeeCache.INST.remove(userFee);
        }
    }

    /**
     * 获取一个手续费的操作对象.
     *
     * @param userId    用户id.
     * @param pairOne   主交易区.
     * @param pairOther 被交易区.
     * @return 如果存在就返回对象 否则为null;
     */
    public static TradeUserFeeNotify get(String userId, String pairOne, String pairOther) {
        TradeUserFeeNotify notify = new TradeUserFeeNotify();
        notify.setPairOne(pairOne);
        notify.setPairOther(pairOther);
        notify.setUserId(userId);
        return UserFeeCache.INST.get(notify);
    }
}
