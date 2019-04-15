package com.biao.kline.vo;

import com.biao.pojo.MatchStreamDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class KlineHandlerVO {

    private String tradeTime;

    /**
     * 保存 分钟时间.
     */
    private String minuteTime;

    /**
     * 价格.
     */
    private BigDecimal price;

    /**
     * 成交数量.
     */
    private BigDecimal volume;

    /**
     * 🐽主区 币代号  eth_btc 这里就是btc.
     */
    private String coinMain;

    /**
     * 其他币种代号 eth_btc 这里存的就是eth.
     */
    private String coinOther;


    public static KlineHandlerVO convert(MatchStreamDto matchStreamDto) {
        KlineHandlerVO handlerVO = new KlineHandlerVO();
        handlerVO.setCoinMain(matchStreamDto.getCoinMain());
        handlerVO.setCoinOther(matchStreamDto.getCoinOther());
        handlerVO.setMinuteTime(matchStreamDto.getMinuteTime());
        handlerVO.setPrice(matchStreamDto.getPrice());
        handlerVO.setTradeTime(matchStreamDto.getTradeTime());
        handlerVO.setVolume(matchStreamDto.getVolume());
        return handlerVO;
    }

}
