package com.biao.rebot.service;

import com.biao.enums.TradeEnum;
import com.biao.rebot.common.TradeFormat;
import com.biao.rebot.config.RobotParam;
import com.biao.rebot.config.RobotWeight;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.OrderBook;
import com.binance.api.client.domain.market.OrderBookEntry;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 币安价格比对信息.
 *
 *
 */
public class BinancePriceService implements PriceService {

    private final Logger logger = LoggerFactory.getLogger(BinancePriceService.class);
    private String apiKey;
    private String secret;
    private BinanceApiRestClient client;

    /**
     * 秒计算，单位毫米. 表示30分钟拉取一次最新价格.
     */
    private long pullPriceTimes = 1000 * 5;

    /**
     * 价格处理.
     */
    private RobotPriceFactory priceFactory;

    /**
     * 深度处理.
     */
    private RobotVolumeFactory volumeFactory;

    /**
     * 工厂.
     */
   protected BinanceApiClientFactory clientFactory;



    /**
     * Instantiates a new Binance price service.
     */
    public BinancePriceService() {
        apiKey = RobotParam.get().getRobotCtx().getApiKey();
        secret = RobotParam.get().getRobotCtx().getSkey();
        priceFactory = new BinanceRobotPrice();
        volumeFactory = new BinanceRobotVolume();
    }

    @Override
    public void init() {

        try {
            //设置socks代理服务器ip端口
            clientFactory = BinanceApiClientFactory.newInstance(apiKey, secret, RobotParam.get().proxy());
            this.client = clientFactory.newRestClient();
            this.client.ping();
            logger.info("币安连接初始化成功！");
        } catch (Exception e) {
            throw new RuntimeException("币安连接初始化失败!", e);
        }
    }

    @Override
    public List<Pair<BigDecimal, BigDecimal>> getDepth(String coinMain, String coinOther, RobotWeight weight) {
        String symbol = symbol(coinMain, coinOther);
        List<OrderBookEntry> all = getAll(symbol, weight.getTradeEnum());
        if(all != null){
            List<Pair<BigDecimal, BigDecimal>> list = new ArrayList<>();
            BigDecimal priceRel = priceFactory.price(weight.getPriceByRange());
            BigDecimal volumeRel = volumeFactory.volume(weight.getVolumeByRange());
            for (OrderBookEntry orderBookEntry : all) {
                //价络与数量计算.
                BigDecimal price = new BigDecimal(orderBookEntry.getPrice());
                BigDecimal volume = new BigDecimal(orderBookEntry.getQty());
                BigDecimal mulPrice = price.multiply(priceRel);
                BigDecimal mulVolume= volume.multiply(volumeRel);
                Pair<BigDecimal, BigDecimal> pair = Pair.of(
                        TradeFormat.priceFormat(weight,price.add(mulPrice)),
                        TradeFormat.volumeFormat(weight,volume.add(mulVolume)));
                list.add(pair);
            }
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public BigDecimal calPrice(String coinMain, String coinOther, RobotWeight weight) {
        String symbol = symbol(coinMain, coinOther);
        OrderBookEntry first = getFirst(symbol, weight.getTradeEnum());
        if(first == null){
            return BigDecimal.ZERO;
        }
        BigDecimal price = new BigDecimal(first.getPrice());
        return calPrice(price, coinMain, coinOther, weight);
    }

    @Override
    public BigDecimal calPrice(BigDecimal price, String coinMain, String coinOther, RobotWeight weight) {
        return TradeFormat.priceFormat(weight, price.add(priceFactory
                .price(weight.getPriceByRange())
                .multiply(price)));
    }

    @Override
    public BigDecimal calVolume(String coinMain, String coinOther, RobotWeight weight) {
        String symbol = symbol(coinMain, coinOther);
        OrderBookEntry first = getFirst(symbol, weight.getTradeEnum());
        if(first == null){
            return BigDecimal.ZERO;
        }
        BigDecimal volume = new BigDecimal(first.getQty());
        return calVolume(volume, coinMain, coinOther, weight);
    }

    @Override
    public BigDecimal calVolume(BigDecimal volume, String coinMain, String coinOther, RobotWeight weight) {
        return TradeFormat.volumeFormat(weight, volumeFactory
                .volume(weight.getVolumeByRange())
                .multiply(volume));
    }

    private String symbol(String coinMain, String coinOther){
        return  RobotParam.joinSymbol(coinMain, coinOther).toUpperCase();
    }

    private OrderBookEntry getFirst(String symbol, TradeEnum tradeEnum){
        List<OrderBookEntry> all = getAll(symbol, tradeEnum);
        if(all == null || all.isEmpty()){
            return null;
        }
       return all.get(0);
    }

    @Override
    public  Pair<Pair<BigDecimal, BigDecimal>, Pair<BigDecimal, BigDecimal>> getDepthPrice(String coinMain, String coinOther) {
        String symbol = symbol(coinMain, coinOther);
        int depth = 20;
        OrderBook orderBook = client.getOrderBook(symbol, depth);
        if(orderBook != null) {
            List<OrderBookEntry> asks = orderBook.getAsks();
            List<OrderBookEntry> bids = orderBook.getBids();
            BigDecimal askPriceFirst = BigDecimal.ZERO;
            BigDecimal bidPriceFirst = BigDecimal.ZERO;
            BigDecimal askPriceLast  = BigDecimal.ZERO;
            BigDecimal bidPriceLast  = BigDecimal.ZERO;
            if(asks != null && !asks.isEmpty()){
                int askSize = asks.size();
                askPriceFirst = new BigDecimal(asks.get(0).getPrice());
                askSize = askSize >= depth ? askSize - 1 : 0;
                askPriceLast = askSize >= depth ? new BigDecimal(asks.get(askSize-1).getPrice()) : BigDecimal.ZERO;
            }
            if(bids != null && !bids.isEmpty()){
                int bidSize = bids.size();
                bidPriceFirst = new BigDecimal(bids.get(0).getPrice());
                bidPriceLast = bidSize >= depth ? new BigDecimal(bids.get(bidSize -1).getPrice()) : BigDecimal.ZERO;
            }
            return Pair.of(Pair.of(askPriceFirst, bidPriceFirst), Pair.of(askPriceLast, bidPriceLast));
        }
        return null;
    }

    /**
     * 获取1000档数据.
     * @param symbol joinSymbol;
     * @param tradeEnum tradeEnum;
     * @return list;
     */
    private List<OrderBookEntry> getAll(String symbol, TradeEnum tradeEnum){
        OrderBook orderBook = client.getOrderBook(symbol,100);
        if(orderBook == null){
            return Collections.emptyList();
        }
        if(tradeEnum.equals(TradeEnum.BUY)){
            //买单
            return orderBook.getBids();
        }else if(tradeEnum.equals(TradeEnum.SELL)) {
            //卖单
            return orderBook.getAsks();
        }
        return Collections.emptyList();
    }

    private BigDecimal range(Pair<BigDecimal, BigDecimal> range){
        double min = range.getLeft().doubleValue();
        double max = range.getRight().doubleValue();
        double volume = RandomUtils.nextDouble(min, max);
        return new BigDecimal(volume).setScale(8, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * The type Binance robot price.
     */
    class BinanceRobotPrice implements RobotPriceFactory {
        @Override
        public BigDecimal price(Pair<BigDecimal, BigDecimal> range) {
//            return range(range);
            return BigDecimal.ZERO;
        }
    }

    /**
     * The type Binance robot volume.
     */
    class BinanceRobotVolume implements RobotVolumeFactory{

        @Override
        public BigDecimal volume(Pair<BigDecimal, BigDecimal> range) {
               return range(range);
//            return BigDecimal.ZERO;
        }
    }
}
