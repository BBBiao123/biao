package com.biao.enums;

import java.util.Arrays;

/**
 * The enum Offline order detail status enum.
 *
 *  ""
 */
public enum OfflineOrderDetailStatusEnum {

    /**
     * Nomal offline order detail status enum.
     */
    NOMAL("0", "买入下单|卖出下单"),
    /**
     * Confirm pay offline order detail status enum.
     */
    CONFIRM_PAY("1", "确认付款了"),
    /**
     * Cancel offline order detail status enum.
     */
    CANCEL("9", "取消"),
    /**
     * Confirm in offline order detail status enum.
     */
    CONFIRM_IN("2", "确认收到款"),
    /**
     * Confirm not in offline order detail status enum.
     */
    CONFIRM_NOT_IN("3", "确认没收到款"),
    /**
     * Shensu offline order detail status enum.
     */
    SHENSU("4", "申诉"),
    /**
     * Shensu over offline order detail status enum.
     */
    SHENSU_OVER("5", "仲裁结束"),

    /**
     * Shensu over buy offline order detail status enum.
     */
    SHENSU_OVER_BUY("6", "仲裁结束买方胜"),

    /**
     * Shensu over sell offline order detail status enum.
     */
    SHENSU_OVER_SELL("7", "仲裁结束卖方胜"),


    /**
     * Task order cancel offline order detail status enum.
     */
    TASK_ORDER_CANCEL("11", "定时任务倒计时取消");


    private String code;

    private String message;

    OfflineOrderDetailStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Acquired by code offline order detail status enum.
     *
     * @param code the code
     * @return the offline order detail status enum
     */
    public static OfflineOrderDetailStatusEnum acquiredByCode(String code) {
        return Arrays.stream(OfflineOrderDetailStatusEnum.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst().orElse(OfflineOrderDetailStatusEnum.NOMAL);
    }
}
