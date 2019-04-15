package com.biao.rebot.service;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;

/**
 * robotVolume规则获取；
 *
 *
 */
public interface RobotVolumeFactory {
    /**
     * 处理的volume工厂实现；
     *
     * @param range 因子范围；
     * @return 结果 ；
     */
    BigDecimal volume(Pair<BigDecimal, BigDecimal> range);
}
