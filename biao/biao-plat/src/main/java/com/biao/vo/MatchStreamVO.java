package com.biao.vo;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.pojo.MatchStreamDto;
import com.biao.reactive.data.mongo.domain.MatchStream;
import com.biao.util.DateUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDateTime;

/**
 * 撮合流水 实体
 *
 *  ""
 * @version 1.0
 * @date 2018/4/6 上午10:19
 * @since JDK 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchStreamVO extends TradePairBaseVO implements Serializable {


    /**
     * 时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime date;

    /**
     * 买入 / 卖出
     */
    private Integer type;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 成交数量
     */
    private BigDecimal volume;


    public static MatchStreamVO transfrom(MatchStream matchStream) {
        MatchStreamVO vo = new MatchStreamVO();
        vo.setVolume(matchStream.getVolume());
        vo.setDate(matchStream.getTradeTime());
        vo.setPrice(matchStream.getPrice());
        vo.setType(matchStream.getType());
        vo.setCoinMain(matchStream.getCoinMain());
        vo.setCoinOther(matchStream.getCoinOther());
        return vo;

    }


    public static MatchStreamVO transform(MatchStreamDto matchStream) {
        MatchStreamVO vo = new MatchStreamVO();
        vo.setVolume(matchStream.getVolume().setScale(8, RoundingMode.HALF_UP));
        try {
            vo.setDate(DateUtils.parseLocalDateTime(matchStream.getTradeTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        vo.setPrice(matchStream.getPrice());
        vo.setType(matchStream.getType());
        vo.setCoinMain(matchStream.getCoinMain());
        vo.setCoinOther(matchStream.getCoinOther());
        return vo;

    }


}
