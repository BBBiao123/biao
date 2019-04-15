package com.biao.previous.domain;

import com.biao.enums.OrderEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CancelResult.
 * <p>
 * 取消数据的DTO
 * <p>
 * 18-12-29下午4:56
 *
 *  "" sixh
 */
@Data
public class CancelResult implements TrParent, Serializable {
    /**
     * 取消的订单状态。
     */
    private OrderEnum.OrderStatus status;

    private String orderNo;

    /**
     * 退还的lockVolume.
     */
    private BigDecimal blockVolume;

    private String userId;

    private String coinSymbol;

}
