package com.biao.rebot.service;

import com.biao.rebot.config.RobotWeight;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface Price service.
 *
 *
 */
public interface PriceService {

    /**
     * Init.
     */
    void init();

    /**
     * 获取交易对深度.
     *
     * @param coinMain  主区
     * @param coinOther 被交易区；
     * @param weight    the weight
     * @return 价格 ；
     */
    List<Pair<BigDecimal,BigDecimal>> getDepth(String coinMain, String coinOther, RobotWeight weight);

    /**
     * 计算一个合理的价格进行交易；
     *
     * @param coinMain  主区
     * @param coinOther 被交易区；
     * @param weight    the weight
     * @return 价格 ；
     */
    BigDecimal calPrice(String coinMain, String coinOther, RobotWeight weight);

    /**
     * 计算一个合理的价格进行交易；
     *
     * @param price     the price
     * @param coinMain  主区
     * @param coinOther 被交易区；
     * @param weight    the weight
     * @return 价格 ；
     */
    BigDecimal calPrice(BigDecimal price, String coinMain, String coinOther, RobotWeight weight);

    /**
     * Cal depth big decimal.
     *
     * @param volume    the volume
     * @param coinMain  the coin main
     * @param coinOther the coin other
     * @param weight    the weight
     * @return the big decimal
     */
    BigDecimal calVolume(BigDecimal volume, String coinMain, String coinOther, RobotWeight weight);

    /**
     * Cal depth big decimal.
     *
     * @param coinMain  the coin main
     * @param coinOther the coin other
     * @param weight    the weight
     * @return the big decimal
     */
    BigDecimal calVolume(String coinMain, String coinOther, RobotWeight weight);

    /**
     * 获取一个最新的价格.
     * left: first price
     * right:last price
     *
     * first -- >left: ask right: bid
     * last ---> left: ask right: bid
     * 如果没有获取到相关的价格则返回{@code null}
     *
     * @param coinMain  the coin main
     * @param coinOther the coin other
     * @return first price
     */
    Pair<Pair<BigDecimal, BigDecimal>, Pair<BigDecimal, BigDecimal>> getDepthPrice(String coinMain, String coinOther);
}
