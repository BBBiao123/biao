package com.biao.rebot.config;

import com.biao.enums.TradeEnum;
import com.biao.rebot.SymbolInfo;
import com.biao.rebot.common.Constants;
import com.google.common.base.Splitter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;

/**
 * robot权重因子数据计算；
 *
 *
 */
@Data
public class RobotWeight {
    /**
     * 因子属于买 入还是卖出；
     */
    private TradeEnum tradeEnum;
    /**
     * 主交易区；
     */
    private String coinMain;
    /**
     * 被交易区
     */
    private String coinOther;
    /**
     * 数量活动因子取值范围
     */
    private String volumeRange = "1-30";
    /**
     * 价格范转；
     */
    private String priceRange = "1-30";

    private Boolean init;
    /**
     * 交易对相关信息.
     */
    private SymbolInfo symbolInfo;

    private Login login;

    /**
     * Instantiates a new Robot weight.
     */
    public RobotWeight() {
    }

    /**
     * 获取一个volume的获取取值；
     *
     * @return range ;
     */
    public Pair<BigDecimal, BigDecimal> getVolumeByRange() {
        if (StringUtils.isBlank(volumeRange)) {
            throw new RuntimeException("volume range error ....");
        }
        List<String> splits = Splitter.on(Constants.SPLIT_MARK2).splitToList(volumeRange);
        return Pair.of(new BigDecimal(splits.get(0)), new BigDecimal(splits.get(1)));
    }


    /**
     * 获取一个price的获取取值；
     *
     * @return range ;
     */
    public Pair<BigDecimal, BigDecimal> getPriceByRange() {
        if (StringUtils.isBlank(priceRange)) {
            throw new RuntimeException("volume range error ....");
        }
        List<String> splits = Splitter.on(Constants.SPLIT_MARK2).splitToList(priceRange);
        return Pair.of(new BigDecimal(splits.get(0)), new BigDecimal(splits.get(1)));
    }

}
