package com.bbex.robot;

import com.bbex.robot.common.NameThreadFactory;
import com.bbex.robot.common.TradeEnum;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 查询价格的接口；
 *
 * @author p
 */
final class PriceService {
    private final Logger logger = LoggerFactory.getLogger(PriceService.class);
    /**
     * 保存价格体系；
     */
    private final Map<String, BigDecimal> prices = Maps.newConcurrentMap();
    /**
     * 10查询一接口信息；
     */
    private final Integer QUERY_TIME = 10;
    /**
     * price接口；
     */
    private RobotPriceFactory priceFactory;

    private Random ran = new Random();

    /**
     * 交易的价格；初始化；
     */
    @SuppressWarnings("all")
    public void init() {
        priceFactory = new RobotInstanceReflect().reflect(RobotPamart.get().getRobotCtx().getPriceFactory());
        String skey = RobotPamart.get().getRobotCtx().getSkey();
        String apiKey = RobotPamart.get().getRobotCtx().getApiKey();
        //第一次需要查询一次；
        queryPrice();
        Executors.newScheduledThreadPool(1, new NameThreadFactory("price_query"))
                .scheduleAtFixedRate(() -> queryPrice(), 0, QUERY_TIME, TimeUnit.SECONDS);
        logger.info("初始化价格信息成功！");
    }

    /**
     * 循环查询一个价络；
     */
    private void queryPrice() {
        List<RobotWeight> pairs = RobotPamart.get().getParams();
        pairs.forEach(e -> {
            try {
                String key = key(e.getTradeEnum(), e.getCoinMain(), e.getCoinOther());
//                TickerPrice price = binanceApiRestClient.getPrice( e.getCoinOther()+e.getCoinMain());
                BigDecimal bgPrice = BigDecimal.ZERO;
                logger.info("自动查询{}的价格为{}", key, bgPrice);
                prices.put(key, bgPrice);
            } catch (Exception ex) {
                logger.error("", ex);
            }
        });
    }

    /**
     * 获取一个价格信息；
     *
     * @param coinMain  主区
     * @param coinOther 被交易区；
     * @return 价格；
     */
    public BigDecimal getPrice(String coinMain, String coinOther, TradeEnum tradeEnum) {
        /*
         * key组装；
         */
        String key = key(tradeEnum, coinMain, coinOther);
        return Optional.ofNullable(prices.get(key)).orElse(new BigDecimal(0));
    }

    /**
     * 计算一个合理的价格进行交易；
     *
     * @param coinMain  主区
     * @param coinOther 被交易区；
     * @return 价格；
     */
    BigDecimal calPrice(String coinMain, String coinOther, RobotWeight weight) {
        BigDecimal price = getPrice(coinMain, coinOther, weight.getTradeEnum());
        //这里需要计算一下；
        BigDecimal decimal = priceFactory.price(weight.getPriceByRange());
        return price.add(decimal).setScale(8, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 返回一个key 信息；
     *
     * @param f 第一个字符串；
     * @param s 第二个字符串
     * @return key;
     */
    private String key(TradeEnum f, String s, String t) {
        return Joiner.on(Constants.SPLIT_MARK).join(s, t);
    }

}
