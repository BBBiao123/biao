package com.biao.previous.domain;

import com.biao.enums.OrderEnum;
import com.biao.pojo.TradeDto;
import com.biao.previous.main.AbstractTrade;
import lombok.Data;

import java.io.Serializable;

/**
 * 交易结果处理；
 *
 *
 */
@Data
public class TradeResult implements TrParent, Serializable {
    /**
     * index;
     */
    private Integer index;
    /**
     * 计算结果；
     */
    private AbstractTrade.ComputerResult computerResult;
    /**
     * 买入订单号；
     */
    private String buyOrderNo;
    /**
     * 买入订单状态；
     */
    private OrderEnum.OrderStatus buyOrderStatus;
    /**
     * 卖出的订单号；
     */
    private String sellOrderNo;

    /**
     * 买入订单状态；
     */
    private OrderEnum.OrderStatus sellOrderStatus;
    /**
     * 买入方的操作对象；
     */
    private TradeDto buyDto;
    /**
     * 买出方的dto;
     */
    private TradeDto sellDto;
    /**
     * 操作的tradeNo;用于处理订单撤锁的锁处理;
     */
    private String tradeNo;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TradeResult{");
        sb.append("index=").append(index);
        sb.append(", computerResult=").append(computerResult);
        sb.append(", buyOrderNo='").append(buyOrderNo).append('\'');
        sb.append(", buyOrderStatus=").append(buyOrderStatus);
        sb.append(", sellOrderNo='").append(sellOrderNo).append('\'');
        sb.append(", sellOrderStatus=").append(sellOrderStatus);
        sb.append(", buyDto=").append(buyDto);
        sb.append(", sellDto=").append(sellDto);
        sb.append('}');
        return sb.toString();
    }
}
