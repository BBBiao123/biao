package com.biao.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class FeeUtils {

    /**
     * 计算手续费 按固定阶梯算法
     *
     * @param volume
     * @param sellFeeStep
     * @return
     */
    public static BigDecimal getFeeByStep(BigDecimal volume, String sellFeeStep) {
        BigDecimal result = BigDecimal.ZERO;
        String[] arr = sellFeeStep.split("\\|");
        long firstVolume = Long.parseLong(arr[0]);
        long secondVolume = Long.parseLong(arr[1]);
        BigInteger divResult = volume.toBigInteger().divide(BigInteger.valueOf(firstVolume));
        BigInteger modResult = volume.toBigInteger().mod(BigInteger.valueOf(firstVolume));
        if (divResult.compareTo(BigInteger.ZERO) == 0) {
            result = BigDecimal.valueOf(secondVolume);
        } else if (divResult.compareTo(BigInteger.ZERO) > 0) {
            if (modResult.compareTo(BigInteger.ZERO) == 1) {
                result = BigDecimal.valueOf(secondVolume).multiply(BigDecimal.valueOf(divResult.add(BigInteger.ONE).longValue()));
            } else if (modResult.compareTo(BigInteger.ZERO) == 0) {
                result = BigDecimal.valueOf(secondVolume).multiply(BigDecimal.valueOf(divResult.longValue()));
            }
        }
        return result;
    }
}
