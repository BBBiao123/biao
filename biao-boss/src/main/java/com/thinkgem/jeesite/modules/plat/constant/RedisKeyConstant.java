package com.thinkgem.jeesite.modules.plat.constant;

/**
 * project :bbex
 *
 * @author xiaoyu
 * @version 1.0
 * @date 2018/4/22 下午3:19
 * @since JDK 1.8
 */
public final class RedisKeyConstant {

    public static final String TASK_STAT_PRE_FIX = "task:trade-date:";

    public static final String USER_COIN_VOLUME = "user:coin:volume:";
    
    public static final String TRADE_STATISTIC_TOPIC = "topic.trade.statistic" ;

    public static String buildUserCoinVolumeKey(String userId ) {

        return String.join("", USER_COIN_VOLUME, userId);

    }

    public static String buildTaskStatTradeToDay(String coinMain,
                                                 String coinOther) {
        return String.join("", TASK_STAT_PRE_FIX, coinMain + "_" + coinOther);

    }
}
