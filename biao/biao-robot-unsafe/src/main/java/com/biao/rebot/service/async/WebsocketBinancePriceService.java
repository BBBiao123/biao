package com.biao.rebot.service.async;

import com.biao.rebot.common.TradeFormat;
import com.biao.rebot.config.RobotParam;
import com.biao.rebot.config.RobotWeight;
import com.biao.rebot.dao.RedisFactory;
import com.biao.rebot.service.BinancePriceService;
import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.DepthEvent;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Closeable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * The type Websocket binance price service.
 *
 *
 */
@SuppressWarnings("unused")
public final class WebsocketBinancePriceService extends BinancePriceService implements AsyncNotify<BinanceAsyncData> {

    private final Logger logger = LoggerFactory.getLogger(WebsocketBinancePriceService.class);

    /**
     * 通知体.
     */
    private final Set<AsyncNotify<BinanceAsyncData>> notifies = new HashSet<>();

    /**
     * 拉取数据.
     */
    private Pull pull;

    private Closeable closeable;

    private BinanceApiWebSocketClient webSocketClient;

    private RedisTemplate redisTemplate;

    @Override
    public void init() {
        super.init();
        /*
         * webSocket数据.
         */
        webSocketClient = clientFactory.newWebSocketClient();
        redisTemplate = RedisFactory.getRedisTemplate();
        reset();
    }

    private String getSymbols() {
        //得到交易对信息
        List<String> symbols = RobotParam.get().getParams().stream()
                .map((o -> (RobotParam.joinSymbol(o.getCoinMain(),
                        o.getCoinOther())).toLowerCase()))
                .distinct().collect(toList());
        if (symbols.isEmpty()) {
            throw new RuntimeException("没有设置交易对信息(robot_config_unsafe)没有设置数据!");
        }
        //启动实时拉取价格的线程处理.
        return Joiner.on(",").join(symbols);
    }

    @Override
    public void notify(BinanceAsyncData binanceAsyncData) {
        notifies.forEach(e -> e.notify(binanceAsyncData));
    }

    @Override
    public void addNotify(AsyncNotify<BinanceAsyncData> notify) {
        notifies.add(notify);
    }

    @Override
    public void reset() {
        try {
            if (closeable != null) {
                closeable.close();
            }
            String join = getSymbols();
            if (webSocketClient == null) {
                throw new NullPointerException("client is null");
            }
            if (pull == null) {
                pull = new Pull();
            }
            logger.info("订阅重新初始化，{}", join);
            closeable = webSocketClient.onDepthEvent(join, pull);
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
        }
    }

    @Override
    public BigDecimal calPrice(BigDecimal price, String coinMain, String coinOther, RobotWeight weight) {
        String s = weight.getTradeEnum().redisKey(coinMain, coinOther);
        BigDecimal newPrice = TradeFormat.priceFormat(weight, price);
        Set<String> set = redisTemplate.opsForZSet().rangeByScore(s, newPrice.doubleValue(), newPrice.doubleValue());
        if (set == null || set.isEmpty()) {
            return super.calPrice(price, coinMain, coinOther, weight);
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 价格拉取线程.
     */
    public class Pull implements BinanceApiCallback<DepthEvent> {

        @Override
        public void onResponse(DepthEvent depthEvent) {
            //卖单
            List<Depth> asks = depthEvent.getAsks().stream().map(e -> {
                Depth depth = new Depth();
                depth.setPrice(new BigDecimal(e.getPrice()));
                depth.setVolume(new BigDecimal(e.getQty()));
                depth.setSymbol(depthEvent.getSymbol());
                return depth;
            }).collect(toList());
            //买单
            List<Depth> bids = depthEvent.getBids().stream().map(e -> {
                Depth depth = new Depth();
                depth.setPrice(new BigDecimal(e.getPrice()));
                depth.setVolume(new BigDecimal(e.getQty()));
                depth.setSymbol(depthEvent.getSymbol());
                return depth;
            }).collect(toList());

            BinanceAsyncData<Depth> depths = new BinanceAsyncData<>();
            depths.setAsks(asks);
            depths.setBids(bids);
            depths.setSymbol(depthEvent.getSymbol());
            WebsocketBinancePriceService.this.notify(depths);
        }

        @Override
        public void onFailure(Throwable cause) {
            logger.error("币安数据增量通知失败:{}", cause.getMessage());
        }
    }
}
