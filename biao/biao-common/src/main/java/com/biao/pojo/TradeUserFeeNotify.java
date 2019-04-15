package com.biao.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * TradeUserFeeNotify .
 * 通知撮合系统，修改用户的手续费.
 * ctime: 2018/8/31 14:22
 *
 *  "" sixh
 */
@Data
public class TradeUserFeeNotify implements Serializable {

    /**
     * 用户id.
     */
    private String userId;

    /**
     * 主区.
     */
    private String pairOne;

    /**
     * 被交易区.
     */
    private String pairOther;

    /**
     * 是否生效
     */
    private Boolean status;

    /**
     * 手续费费用fee;
     */
    private BigDecimal fee;
}
