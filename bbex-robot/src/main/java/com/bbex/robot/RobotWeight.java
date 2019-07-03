package com.bbex.robot;

import com.bbex.robot.common.TradeEnum;
import com.google.common.base.Splitter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;

/**
 * robot权重因子数据计算；
 * @author p
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
     * 服务信息；
     */
    private String userId;
    /**
     * 数量活动因子取值范围
     */
    private String volumeRange = "1-30";
    /**
     * 价格范转；
     */
    private String priceRange = "1-30";

    public RobotWeight(TradeEnum tradeEnum, String coinMain, String coinOther, String userId, String volumeRange) {
        this.tradeEnum = tradeEnum;
        this.coinMain = coinMain;
        this.coinOther = coinOther;
        this.userId = userId;
        this.volumeRange = volumeRange;
    }

    public RobotWeight(TradeEnum tradeEnum, String coinMain, String coinOther, String userId) {
        this.tradeEnum = tradeEnum;
        this.coinMain = coinMain;
        this.coinOther = coinOther;
        this.userId = userId;
    }

    public RobotWeight() {
    }

    /**
     * 获取一个volume的获取取值；
     *
     * @return range;
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
     * @return range;
     */
    public Pair<BigDecimal, BigDecimal> getPriceByRange() {
        if (StringUtils.isBlank(priceRange)) {
            throw new RuntimeException("volume range error ....");
        }
        List<String> splits = Splitter.on(Constants.SPLIT_MARK2).splitToList(priceRange);
        return Pair.of(new BigDecimal(splits.get(0)), new BigDecimal(splits.get(1)));
    }

}
