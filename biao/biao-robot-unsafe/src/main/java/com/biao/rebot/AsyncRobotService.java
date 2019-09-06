package com.biao.rebot;

import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.enums.TradeEnum;
import com.biao.rebot.config.RobotParam;
import com.biao.rebot.config.RobotWeight;
import com.biao.rebot.service.async.AsyncData;
import com.biao.rebot.service.async.AsyncDepth;
import com.biao.rebot.service.async.AsyncNotify;
import jodd.util.RandomString;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AsyncRobotService.
 * <p>
 * 异步处理进程
 * <p>
 * 18-12-18下午3:04
 *
 *  "" sixh
 */
@SuppressWarnings("all")
public class AsyncRobotService<D extends AsyncData> extends RobotService implements AsyncNotify<D> {
    private final Logger logger = LoggerFactory.getLogger(AsyncRobotService.class);

    //实始化一个异步通知的服务..

    /**
     * 保存交易对一些信息.
     */
    private Map<String, RobotWeight> params = new ConcurrentHashMap<>();

    private AsyncNotify<D> asyncNotify;
    /**
     * 执行异步任务.
     */

    private ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() << 1, BbexThreadFactory.create("robot_trade_buy_sell"));

    private final Integer defSs = 10;

    @Override
    public void start() {

        super.start();
        //异步的处理.
        //初始化价格服务；
        asyncNotify = (AsyncNotify) priceService;
        asyncNotify.addNotify(this);
        loadParams();
        RobotParam.get().addConfigChange(this);
        logger.info("------------------     tradeTime " + RobotParam.get().getRobotCtx().getTradeTime() + "       --------------");
    }

    @Override
    public boolean getAsync() {
        return true;
    }

    @Override
    public void notify(D d) {
        boolean f = LocalDateTime.now().getMinute() % RobotParam.get().getRobotCtx().getTradeTime() == 0;
        if (!f) {
            return;
        }
        logger.info("------------------  " + new Date() + "     notify  [in]       --------------");
        try {
            Random random = new Random();
            int delay = random.nextInt(  RobotParam.get().getRobotCtx().getTradeTime()* 60);
            Thread.sleep(delay * 1000);
            logger.info("-------------------------- delay : "+delay +"  ------------- " + new Date() + " -----------------------------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String symbol = d.getSymbol();
        //发送买单.
        String buyKey = symbol + "_" + TradeEnum.BUY;
        RobotWeight robotWeight = params.get(buyKey);
        if (robotWeight == null) {
            return;
        }
        service.execute(new AsyncRobotRun(robotWeight, d));
        String sellKey = symbol + "_" + TradeEnum.SELL;
        robotWeight = params.get(sellKey);
        if (robotWeight == null) {
            return;
        }
        service.execute(new AsyncRobotRun(robotWeight, d));
    }

    @Override
    public void reset() {
        loadParams();
        //初始化一下挂单
        initTrade();
        asyncNotify.reset();
    }

    /**
     * 重新加载parmas;
     */
    public void loadParams() {
        params.clear();
        params.putAll(RobotParam.get().getParamsMap());
    }

    @Override
    public void addNotify(AsyncNotify notify) {
        //这里不需要实现了。
    }

    class AsyncRobotRun extends RobotRun {

        private AsyncData<List<AsyncDepth>> data;

        public AsyncRobotRun(RobotWeight weight, AsyncData<List<AsyncDepth>> data) {
            super(weight);
            this.data = data;
        }

        @Override
        void sell() {
            if (!getWeight().getTradeEnum().equals(TradeEnum.SELL)) {
                return;
            }
            List<AsyncDepth> asks = data.getAsks();
            asks.forEach(e -> {
                BigDecimal price = priceService.calPrice(e.getPrice(), getWeight().getCoinMain(), getWeight().getCoinOther(), getWeight());
                BigDecimal volume = priceService.calVolume(e.getVolume(), getWeight().getCoinMain(), getWeight().getCoinOther(), getWeight());
                //查询一下是否经存在相当价格的订单.
                sell(price, volume);
            });
        }

        @Override
        void buy() {
            if (!getWeight().getTradeEnum().equals(TradeEnum.BUY)) {
                return;
            }
            List<AsyncDepth> bids = data.getBids();
            bids.forEach(e -> {
                BigDecimal price = priceService.calPrice(e.getPrice(), getWeight().getCoinMain(), getWeight().getCoinOther(), getWeight());
                BigDecimal volume = priceService.calVolume(e.getVolume(), getWeight().getCoinMain(), getWeight().getCoinOther(), getWeight());
                buy(price, volume);
            });
        }
    }
}
