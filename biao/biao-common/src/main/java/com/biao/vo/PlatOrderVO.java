package com.biao.vo;

import com.biao.pojo.TradeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 买卖挂单实体类.
 *
 *  ""
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatOrderVO extends TradePairBaseVO {

    /**
     * 买 /卖.
     */
    private Integer type;

    /**
     * 挂单的价格.
     */
    private BigDecimal price;

    /**
     * 挂单数量.
     */
    private BigDecimal volume;

    /**
     * 累计的数量.
     */
    private BigDecimal sumTotal;

    private String orderNo;

    /**
     * 转换方法.
     *
     * @param tradeDto {@linkplain TradeDto}
     * @return TradeDto
     */
    public static PlatOrderVO transform(final TradeDto tradeDto) {
        PlatOrderVO vo = new PlatOrderVO();
        vo.setCoinMain(tradeDto.getCoinMain());
        vo.setCoinOther(tradeDto.getCoinOther());
        vo.setPrice(tradeDto.getPrice());
        vo.setVolume(tradeDto.getComputerVolume());
        vo.setType(tradeDto.getType().ordinal());
        vo.setOrderNo(tradeDto.getOrderNo());
        return vo;
    }

}
