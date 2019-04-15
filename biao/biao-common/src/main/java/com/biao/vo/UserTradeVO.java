package com.biao.vo;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * UserTradeVO.
 *
 *  ""(Myth)
 */
@Data
public class UserTradeVO implements Serializable {

    private String id;


    /**
     * 数量.
     */
    private BigDecimal askVolume;

    /**
     * 挂单价格.
     */
    private BigDecimal price;

    /**
     * 成交数量.
     */
    private BigDecimal successVolume;

    /**
     * 主区.
     */
    private String coinMain;

    /**
     * 得到币种标识.
     */
    private String coinOther;

    /**
     * 得到币种数量.
     */
    private BigDecimal toCoinVolume;

    /**
     * 手续费.
     */
    private BigDecimal exFee;

    /**
     * 挂单类型 0：买入 1：卖出.
     */
    private Integer exType;

    /**
     * 成交状态0：未成交 1：部分成交 2：全部成交 3：部分取消 4：全部取消.
     */
    private Integer status;

    private String userId;

    private String toUserId;

    private String orderNo;

    private String tradeNo;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;


}
