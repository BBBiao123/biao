package com.bbex.robot;

import com.bbex.robot.common.NameThreadFactory;
import com.bbex.robot.common.TradeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 服务处理；
 *
 * @author p
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
    private PriceService priceService;

    /**
     * 启动一个机器挂单;
     */
    @SuppressWarnings("all")
    public void start() {
        try {
            logger.info("开始启动挂单机器人");
            //初始化配置信息；
            RobotCtx ctx = new ConfigLoader().loader(RobotCtx.class);
            RobotPamart.get().setRobotCtx(ctx);
            tradeService = new TradeService();
            //初始化一下这个对象；
            RobotPamart.get().init();
            //初始化价格服务；
            priceService = new PriceService();
            priceService.init();
            //登录系统;
            RobotPamart.get().refreshedToken(() -> tradeService.login(ctx.getLoginUser(), ctx.getLoginPass()));
            //启动运行机器人；
            int size = RobotPamart.get().getParams().size();
            service = Executors.newScheduledThreadPool(size, new NameThreadFactory("robot_trade"));
            RobotPamart.get().getParams().forEach(k -> service.scheduleAtFixedRate(
                    new RobotRun(k),
                    0,
                    RobotPamart.get().getRobotCtx().getTradeTime(),
                    TimeUnit.SECONDS));
        } catch (Exception ex) {
            logger.error("发生不可修复的异常，系统退出。。。。", ex);
            System.exit(1);
        }
    }

    /**
     * 执行对象；
     */
    class RobotRun implements Runnable {
        /**
         * robotWeight;
         */
        private RobotWeight weight;
        /**
         * 获取一个数量；
         */
        private RobotVolumeFactory volumeFactory;

        /**
         * 初始化一下啊；
         *
         * @param weight 处理的权重信息；
         */
        public RobotRun(RobotWeight weight) {
            this.weight = weight;
            volumeFactory = new RobotInstanceReflect().reflect(RobotPamart.get().getRobotCtx().getVolumeFactory());
        }

        @Override
        public void run() {
            try {
                buy();
                sell();
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }

        void buy() {
            if (!weight.getTradeEnum().equals(TradeEnum.BUY)) {
                return;
            }
            Trade trade = trade();
            BigDecimal price = priceService.calPrice(trade.getCoinMain(),
                    trade.getCoinOther(),
                    weight);
            trade.setPrice(price);
            tradeService.buyIn(trade);
        }

        void sell() {
            if (!weight.getTradeEnum().equals(TradeEnum.SELL)) {
                return;
            }
            Trade trade = trade();
            BigDecimal price = priceService.calPrice(trade.getCoinMain(),
                    trade.getCoinOther(),
                    weight);
            trade.setPrice(price);
            tradeService.sellOut(trade);

        }

        private Trade trade() {
            String orderNo = tradeService.getOrderNo();
            Trade trade = new Trade();
            trade.setCoinMain(weight.getCoinMain());
            trade.setCoinOther(weight.getCoinOther());
            trade.setOrderNo(orderNo);
            trade.setUserId(weight.getUserId());
            BigDecimal volume = volumeFactory.volume(weight.getVolumeByRange());
            trade.setVolume(volume);
            return trade;
        }
    }
}
