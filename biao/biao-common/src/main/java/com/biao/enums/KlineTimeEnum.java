package com.biao.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * KlineTimeEnum.
 *
 *  ""(Myth)
 */
@Getter
@RequiredArgsConstructor
public enum KlineTimeEnum {


    /**
     * One minute kline time enum.
     */
    ONE_MINUTE("1m", 1, false,ChronoUnit.MINUTES,1),

    /**
     * Five minute kline time enum.
     */
    FIVE_MINUTE("5m", 5, true,ChronoUnit.MINUTES,5),

    /**
     * Fifteen minute kline time enum.
     */
    FIFTEEN_MINUTE("15m", 15, true,ChronoUnit.MINUTES,15),

    /**
     * Thirty minute kline time enum.
     */
    THIRTY_MINUTE("30m", 30, true,ChronoUnit.MINUTES,30),

    /**
     * One hour kline time enum.
     */
    ONE_HOUR("1h", 60, true,ChronoUnit.HOURS,1),

    /**
     * Two hour kline time enum.
     */
    TWO_HOUR("2h", 2 * 60, true,ChronoUnit.HOURS,2),

    /**
     * Four hour kline time enum.
     */
    FOUR_HOUR("4h", 4 * 60, true,ChronoUnit.HOURS,4),

    /**
     * Six hour kline time enum.
     */
    SIX_HOUR("6h", 6 * 60, true,ChronoUnit.HOURS,6),

    /**
     * Six hour kline time enum.
     */
    EIGHT_HOUR("8h", 8 * 60, true,ChronoUnit.HOURS,8),

    /**
     * Twelve hour kline time enum.
     */
    TWELVE_HOUR("12h", 12 * 60, true,ChronoUnit.HOURS,12),

    /**
     * One day kline time enum.
     */
    ONE_DAY("1d", 24 * 60, true,ChronoUnit.DAYS,1),

    /**
     * One week kline time enum.
     */
    ONE_WEEK("1w", 24 * 7 * 60, true,ChronoUnit.WEEKS,1),

    /**
     * One month kline time enum.
     */
    ONE_MONTH("1M", 24 * 30 * 60, true,ChronoUnit.MONTHS,1),;

    private final String msg;

    private final int time;

    private final Boolean stat;
    
    private final ChronoUnit chronoUnit;
    
    private final int interval ;

    public static int acquiredTime(String msg) {
        return Arrays.stream(KlineTimeEnum.values())
                .filter(e -> e.getMsg().equals(msg))
                .findFirst().orElse(KlineTimeEnum.ONE_MINUTE).getTime();
    }

    public static List<KlineTimeEnum> acquiredList() {
        return Arrays.stream(KlineTimeEnum.values())
                .filter(e -> e.stat)
                .collect(Collectors.toList());
    }

    public static boolean hasStat(String msg) {
        return Arrays.stream(KlineTimeEnum.values())
                .filter(e -> e.getMsg().contains(msg)).findAny().orElse(KlineTimeEnum.ONE_MINUTE).getStat();
    }

}
