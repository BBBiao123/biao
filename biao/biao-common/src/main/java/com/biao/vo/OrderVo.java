package com.biao.vo;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单数据的封装；
 */
@Data
public class OrderVo {
    /**
     * 委托时间；
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime time;
    /**
     * 交易类别
     *
     * @see com.biao.enums.TradeEnum
     */
    private Integer exType;
    /**
     * 价格；
     */
    private BigDecimal price;
    /**
     * 数量；
     */
    private BigDecimal volume;
    /**
     * 成功数量；
     */
    private BigDecimal successVolume;
    /**
     * 状态；
     *
     * @see com.biao.enums.OrderEnum.OrderStatus
     */
    private Integer status;

    private String orderNo;

}
