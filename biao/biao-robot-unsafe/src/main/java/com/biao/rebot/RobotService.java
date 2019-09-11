package com.biao.rebot;

import com.biao.enums.TradeEnum;
import com.biao.rebot.common.NameThreadFactory;
import com.biao.rebot.common.RobotInstanceReflect;
import com.biao.rebot.config.ConfigLoader;
import com.biao.rebot.config.RobotCtx;
import com.biao.rebot.config.RobotParam;
import com.biao.rebot.config.RobotWeight;
import com.biao.rebot.service.PriceService;
import com.biao.rebot.service.RobotCancelTrade;
import com.biao.rebot.service.Trade;
import com.biao.rebot.service.TradeService;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 服务处理；
 */
public class RobotService {
    private final Logger logger = LoggerFactory.getLogger(RobotService.class);
    /**
     * 启动一个定时任务的线程池；
     */
    private ScheduledExecutorService service;

    /**
     * 交易对象；
     */
    private TradeService tradeService;
    /**
     * 价格服务；
     */
    PriceService priceService;

    /**
     * The Reflect.
     */
    private RobotInstanceReflect reflect;

    /**
     * Instantiates a new Robot service.
     */
    public RobotService() {
        reflect = new RobotInstanceReflect();
    }

    /**
     * 是否异步
     *
     * @return the boolean
     */
    public boolean getAsync() {
        return false;
    }

    /**
     * 启动一个机器挂单;
     */
    @SuppressWarnings("all")
    public void start() {
        try {
            logger.info("开始启动挂单机器人");
            //初始化配置信息；
            RobotCtx ctx = new ConfigLoader().loader(RobotCtx.class);
            RobotParam.get().setRobotCtx(ctx);
            tradeService = new TradeService();
            //初始化一下这个对象；
            RobotParam.get().init();
            //登录系统;
            RobotParam.get().refreshedToken(() -> tradeService.login(RobotParam.get().getUsers()));
            //初始化价格服务；
            priceService = reflect.reflect(RobotParam.get().getRobotCtx().getPriceService());
            priceService.init();
            //取消订单的服务
            /**
             * 取消订单的服务.
             */
            RobotCancelTrade cancelTrade = new RobotCancelTrade(priceService, tradeService);
            cancelTrade.init();
            //设始化挂单.
            initTrade();
            sync();
        } catch (Exception ex) {
            logger.error("发生不可修复的异常，系统退出。。。。", ex);
            System.exit(1);
        }
    }

    private void sync() {
        /*
         * 如果非异步处理，就需要启线程.
         */
        if (!getAsync()) {
            //启动运行机器人；
            int size = RobotParam.get().getParams().size();
            service = Executors.newScheduledThreadPool(size, new NameThreadFactory("robot_trade"));
            logger.info("------------------------------------  tradeTime =  " + RobotParam.get().getRobotCtx().getTradeTime() );
            RobotParam.get().getParams().forEach(v -> service.scheduleWithFixedDelay(
                    new RobotRun(v),
                    0,
                    RobotParam.get().getRobotCtx().getTradeTime(),
                    TimeUnit.SECONDS));
        }
    }

    /**
     * 初始化挂单数据.
     */
    protected void initTrade() {
        for (RobotWeight param : RobotParam.get().getParams()) {
            //如果配置了初始化，重新初始化数据.
            if (param.getInit()) {
                //修改数据库.
                RobotParam.get().completeInit(param);
                List<Pair<BigDecimal, BigDecimal>> depth = priceService.getDepth(param.getCoinMain(), param.getCoinOther(), param);
                RobotRun robotRun = new RobotRun(param);
                depth.forEach(e -> {
                    robotRun.buy(e.getLeft(), e.getRight());
                    robotRun.sell(e.getLeft(), e.getRight());
                });
            }
        }
    }

    /**
     * 执行对象；
     */
    class RobotRun implements Runnable {
        /**
         * robotWeight;
         * 价格权重处理.
         */
        private RobotWeight weight;

        /**
         * 初始化一下啊；
         *
         * @param weight 处理的权重信息；
         */
        public RobotRun(RobotWeight weight) {
            this.weight = weight;
        }

        @Override
        public void run() {
            try {
                Random random = new Random();
                int delay = random.nextInt(  RobotParam.get().getRobotCtx().getTradeTime()* 5);
                Thread.sleep(delay * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                buy();
                sell();
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }

        /**
         * Buy.
         */
        void buy() {
            if (!weight.getTradeEnum().equals(TradeEnum.BUY)) {
                return;
            }
            BigDecimal price = priceService.calPrice(weight.getCoinMain(),
                    weight.getCoinOther(),
                    weight);
            BigDecimal volume = priceService.calVolume(weight.getCoinMain(),
                    weight.getCoinOther(),
                    weight);
            buy(price, volume);
        }

        /**
         * Buy.
         *
         * @param price  the price
         * @param volume the volume
         */
        void buy(BigDecimal price, BigDecimal volume) {
            if (!weight.getTradeEnum().equals(TradeEnum.BUY)) {
                return;
            }
            if (price.doubleValue() <= 0 || volume.doubleValue() <= 0) {
                return;
            }
            Trade trade = trade();
            trade.setPrice(price);
            trade.setVolume(volume);
            tradeService.buyIn(trade);
        }

        /**
         * Sell.
         */
        void sell() {
            if (!weight.getTradeEnum().equals(TradeEnum.SELL)) {
                return;
            }
            BigDecimal price = priceService.calPrice(weight.getCoinMain(),
                    weight.getCoinOther(),
                    weight);
            BigDecimal volume = priceService.calVolume(weight.getCoinMain(),
                    weight.getCoinOther(),
                    weight);
            sell(price, volume);
        }

        /**
         * Gets weight.
         *
         * @return the weight
         */
        RobotWeight getWeight() {
            return weight;
        }

        /**
         * Buy.
         *
         * @param price  the price
         * @param volume the volume
         */
        void sell(BigDecimal price, BigDecimal volume) {
            if (!weight.getTradeEnum().equals(TradeEnum.SELL)) {
                return;
            }
            if (price.doubleValue() <= 0 || volume.doubleValue() <= 0) {
                return;
            }
            Trade trade = trade();
            trade.setPrice(price);
            trade.setVolume(volume);
            tradeService.sellOut(trade);
        }

        private Trade trade() {
            String symbolf = RobotParam.joinSymbolByType(weight);
            Trade trade = new Trade();
            String orderNo = tradeService.getOrderNo(symbolf);
            trade.setCoinMain(weight.getCoinMain());
            trade.setCoinOther(weight.getCoinOther());
            trade.setOrderNo(orderNo);
            trade.setUserId(weight.getLogin().getUserId());
            return trade;
        }
    }
}
