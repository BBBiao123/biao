package com.bbex.robot;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;

/**
 * 一个随机的实现获取volume的处理；
 */
public class RandomRobotVolume implements RobotVolumeFactory {

    @Override
    public BigDecimal volume(Pair<BigDecimal, BigDecimal> range) {
        double min = range.getLeft().doubleValue();
        double max = range.getRight().doubleValue();
        double volume = RandomUtils.nextDouble(min, max);
        return new BigDecimal(volume).setScale(8, BigDecimal.ROUND_HALF_UP);
    }
}
