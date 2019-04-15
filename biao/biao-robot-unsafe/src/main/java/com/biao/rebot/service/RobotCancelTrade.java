package com.biao.rebot.service;

import com.biao.constant.TradeConstant;
import com.biao.enums.TradeEnum;
import com.biao.pojo.TradeDto;
import com.biao.rebot.common.DuplicateBlockingQueue;
import com.biao.rebot.common.NameThreadFactory;
import com.biao.rebot.common.TradeFormat;
import com.biao.rebot.config.RobotParam;
import com.biao.rebot.config.RobotWeight;
import com.biao.rebot.dao.RedisFactory;
import com.biao.rebot.service.async.AsyncData;
import com.biao.rebot.service.async.AsyncNotify;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * RobotCancelTrade.
 * <p>
 * 机器人取消相关的处理.
 * <p>
 * 18-12-19上午10:44
 *
 *  "" sixh
 */
public class RobotCancelTrade implements AsyncNotify {
    private final Logger logger = LoggerFactory.getLogger(RobotCancelTrade.class);
    /**
     * 机器人取消息。
     */
    private PriceService priceService;

    private TradeService tradeService;

    private RedisTemplate redisTemplate;

    /**
     * 启用一个队列.
     */
    private final BlockingQueue<String> queue = new DuplicateBlockingQueue<>();

    /**
     * 数据.
     */
    private volatile List<Optional<RobotWeight>> params;

    /**
     * Instantiates a new Robot cancel trade.
     *
     * @param priceService the price service
     * @param tradeService the trade service
     */
    public RobotCancelTrade(PriceService priceService, TradeService tradeService) {
        this.priceService = priceService;
        this.tradeService = tradeService;
        redisTemplate = RedisFactory.getRedisTemplate();
    }

    @Override
    public void notify(AsyncData asyncData) {
        throw new UnsupportedOperationException("notify method");
    }

    @Override
    public void addNotify(AsyncNotify notify) {
        throw new UnsupportedOperationException("addNotify method");
    }

    /**
     * 初始化.
     */
    @Override
    public  void reset() {
        params = RobotParam.get().getParams().stream()
                .collect(Collectors.groupingBy(e -> RobotParam.joinSymbol(e.getCoinMain(), e.getCoinOther())))
                .values().stream().map(e -> e.stream().findFirst()).collect(Collectors.toList());

    }

    /**
     * Gets params.
     *
     * @return the params
     */
    private List<Optional<RobotWeight>> getParams() {
        return new ArrayList<>(params);
    }

    /**
     * Init.
     */
    @SuppressWarnings("all")
    public void init() {
        reset();
        RobotParam.get().addConfigChange(this);
        ScheduledExecutorService service = Executors.newScheduledThreadPool(3, new NameThreadFactory("robot_cancel_trade"));
        CancelCompute cancelCompute = new CancelCompute();
        service.scheduleAtFixedRate(
                cancelCompute,
                0,
                5,
                TimeUnit.SECONDS);
        service.scheduleAtFixedRate(
                new CancelCompute2(cancelCompute),
                0,
                5,
                TimeUnit.SECONDS);
        service.execute(new CancelRun());
    }


    /**
     * Cancel.
     *
     * @param orders the orders
     */
    private void cancel(Collection<String> orders) {
        orders.forEach(e -> {
            if (e != null) {
                try {
                    queue.put(e);
                } catch (InterruptedException e1) {
                    logger.error("取消订单发送订单号失败{}", e);
                }
            }
        });
    }

    /**
     * The type Cancel run.
     */
    @SuppressWarnings("unchecked")
    class CancelRun implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    String orderNo = queue.take();
                    if (StringUtils.isNoneBlank(orderNo)) {
                        //这里判断一下数据.
                        TradeDto o = (TradeDto) redisTemplate.opsForHash().get(TradeConstant.TRADE_PREPOSITION_KEY, orderNo);
                        if (o != null && RobotParam.get().hasUser(o.getUserId())) {
                            String symbol = RobotParam.joinSymbolByType(o.getCoinMain(), o.getCoinOther(), o.getType());
                            tradeService.cancel(orderNo, symbol);
                        }
                    }
                } catch (Exception ex) {
                    logger.error("取消订单失败:", ex);
                }
            }
        }
    }

    /**
     * 买大，卖小的队列，
     */
    @SuppressWarnings("unchecked")
    class CancelCompute implements Runnable {

        private ConcurrentHashMap<String, Pair<BigDecimal, BigDecimal>> lastPriceMap = new ConcurrentHashMap<>();

        @Override
        public void run() {
            List<Optional<RobotWeight>> params = getParams();
            for (Optional<RobotWeight> optional : params) {
                optional.ifPresent(param -> {
                    try {
                        Pair<Pair<BigDecimal, BigDecimal>, Pair<BigDecimal, BigDecimal>> depthPrice = priceService.getDepthPrice(param.getCoinMain(), param.getCoinOther());
                        Pair<BigDecimal, BigDecimal> firstPrice = depthPrice.getLeft();
                        Pair<BigDecimal, BigDecimal> lastPrice = depthPrice.getRight();
                        lastPriceMap.put(param.getSymbolInfo().getSymbol(), lastPrice);
                        //卖单
                        BigDecimal askPrice = TradeFormat.priceFormat(param, firstPrice.getLeft());
                        //买单
                        BigDecimal bidPrice = TradeFormat.priceFormat(param, firstPrice.getRight());
                        //查询买1的价格.
                        String buyKey = TradeEnum.BUY.redisKey(param.getCoinMain(), param.getCoinOther());
                        String sellKey = TradeEnum.SELL.redisKey(param.getCoinMain(), param.getCoinOther());
                        //查询小于当前价格的信息
                        Set<ZSetOperations.TypedTuple<String>> buySet = redisTemplate.opsForZSet().rangeByScoreWithScores(buyKey, bidPrice.doubleValue(), bidPrice.doubleValue() * 10);
                        //查询大于当前从格的信息
                        Set<ZSetOperations.TypedTuple<String>> sellSet = redisTemplate.opsForZSet().rangeByScoreWithScores(sellKey, 0, askPrice.doubleValue());
                        int buySize = 0;
                        int sellSize = 0;
                        if (!CollectionUtils.isEmpty(buySet)) {
                            List<String> collect = buySet.stream()
                                    .filter(e -> e.getScore() != null && e.getScore() > 0)
                                    .filter(e -> e.getScore() > bidPrice.doubleValue())
                                    .map(ZSetOperations.TypedTuple::getValue)
                                    .collect(Collectors.toList());
                            cancel(collect);
                            buySize = buySet.size();
                        }
                        if (!CollectionUtils.isEmpty(sellSet)) {
                            sellSize = sellSet.size();
                            List<String> collect = sellSet.stream()
                                    .filter(e -> e.getScore() != null && e.getScore() > 0)
                                    .filter(e -> e.getScore() < askPrice.doubleValue())
                                    .map(ZSetOperations.TypedTuple::getValue)
                                    .collect(Collectors.toList());
                            cancel(collect);
                        }
                        logger.info("排前价格,开始同步判断价格-->{}:{}:-->准备取消价格大于{}的买单数据{}条，小于{}卖单数据{}条",
                                param.getCoinMain(),
                                param.getCoinOther(),
                                bidPrice,
                                buySize,
                                askPrice,
                                sellSize);
                    } catch (Exception ex) {
                        logger.error("同步判断价格失败:{}", param.getSymbolInfo().getSymbol(), ex);
                    }
                });
            }
        }

        /**
         * Last price pair.
         *
         * @param symbol the joinSymbol
         * @return the pair
         */
        public Pair<BigDecimal, BigDecimal> lastPrice(String symbol) {
            return lastPriceMap.get(symbol);
        }
    }

    /**
     * 买小 卖大
     */
    class CancelCompute2 implements Runnable {
        /**
         * The Ccpt.
         */
        CancelCompute ccpt;

        /**
         * Instantiates a new Cancel compute 2.
         *
         * @param ccpt the ccpt
         */
        public CancelCompute2(CancelCompute ccpt) {
            this.ccpt = ccpt;
        }

        @Override
        public void run() {
            List<Optional<RobotWeight>> params = getParams();
            for (Optional<RobotWeight> optional : params) {
                optional.ifPresent(param -> {
                    try {
                        Pair<BigDecimal, BigDecimal> lastPrice = ccpt.lastPrice(param.getSymbolInfo().getSymbol());
                        if (lastPrice == null) {
                            return;
                        }
                        //卖单
                        BigDecimal askPrice = TradeFormat.priceFormat(param, lastPrice.getLeft());
                        //买单
                        BigDecimal bidPrice = TradeFormat.priceFormat(param, lastPrice.getRight());
                        //查询买1的价格.
                        String buyKey = TradeEnum.BUY.redisKey(param.getCoinMain(), param.getCoinOther());
                        String sellKey = TradeEnum.SELL.redisKey(param.getCoinMain(), param.getCoinOther());
                        //查询小于当前价格的信息
                        Set<String> buySet = redisTemplate.opsForZSet().rangeByScore(buyKey, 0, bidPrice.doubleValue());
                        //查询大于当前从格的信息
                        Set<String> sellSet = redisTemplate.opsForZSet().rangeByScore(sellKey, askPrice.doubleValue(), askPrice.doubleValue() * 10);
                        int buySize = 0;
                        int sellSize = 0;
                        if (!CollectionUtils.isEmpty(buySet)) {
                            cancel(buySet);
                            buySize = buySet.size();
                        }
                        if (!CollectionUtils.isEmpty(sellSet)) {
                            cancel(sellSet);
                        }
                        logger.info("排后价格,开始同步判断价格-->{}:{}:-->准备取消价格小于{}的买单数据{}条，大于{}卖单数据{}条",
                                param.getCoinMain(),
                                param.getCoinOther(),
                                bidPrice,
                                buySize,
                                askPrice,
                                sellSize);
                    } catch (Exception ex) {
                        logger.error("同步判断价格失败:{}", param.getSymbolInfo().getSymbol(), ex);
                    }
                });
            }

        }
    }

}
