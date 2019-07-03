package com.bbex.robot;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;

/**
 * 一个处理的算法信息；
 */
public class RandomRobotPrice implements RobotPriceFactory {
    @Override
    public BigDecimal price(Pair<BigDecimal, BigDecimal> range) {
        double min = range.getLeft().doubleValue();
        double max = range.getRight().doubleValue();
        double volume = RandomUtils.nextDouble(min, max);
        return new BigDecimal(volume).setScale(8, BigDecimal.ROUND_HALF_UP);
    }
}
