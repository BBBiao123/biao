package com.biao.previous.main;

import com.biao.enums.OrderEnum;
import com.biao.enums.TradeEnum;
import com.biao.pojo.TradeDto;
import com.biao.previous.Tools;
import com.biao.previous.domain.ProcessData;
import com.biao.previous.domain.TradeResult;
import com.biao.previous.queue.QueueDistribute;
import com.biao.previous.queue.QueueType;
import com.biao.reactive.data.mongo.domain.TradeDetail;
import com.biao.redis.RedisCacheManager;
import com.biao.util.SnowFlake;
import com.biao.util.TradeCompute;
import com.google.common.base.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @date 2018/4/15
 * 买入交易撮合
 */

public class BuyInFilter extends AbstractTrade {

    private Logger logger = LoggerFactory.getLogger(BuyInFilter.class);

    /**
     * 初始化化啊；
     *
     * @param distribute        distribute
     * @param redisCacheManager redisCacheManager
     */
    public BuyInFilter(QueueDistribute distribute, RedisCacheManager redisCacheManager) {
        super(distribute, redisCacheManager);
    }

    @SuppressWarnings("all")
    @Override
    public void doFilter0(TradeDto buyData, ProcessData processData, DataChain chain) {
        //表示跳过数据处理;
        if (processData.getSkip()) {
            return;
        }
        logger.info("进入buy交易处理{}", buyData);
        //拿到交询的高值;
        TradeDto sellData = null;
        //判断只到下次获取数据，取不到。。则从重新放为mysql中。
        List<TradeDetail> buyList = new ArrayList<>();
        // 获取到相应的处理结果；
        List<TradeResult> results = new ArrayList<>();
        int index = 0;
        //表示是不是已经全部成交成功；false 部分成功；true 全部成功；
        boolean allSuccessful = false;
        while (true) {
            RLock sellLock = null;
            try {
                /*
                 *买出的结果消息；
                 */
                TradeResult tradeResult = new TradeResult();
                String sellKey = asKey(TradeEnum.SELL, buyData.getCoinMain(), buyData.getCoinOther());
               String  orderNo = TradeCache.INST.get(sellKey, Tools.getSellFirst(buyData.getCoinMain(), buyData.getCoinOther(), redisTemplate));
                //加入到数据处理；
                if (StringUtils.isBlank(orderNo)) {
                    break;
                }
                sellLock = redissonClient.getLock(orderNo);
                sellLock.lock();
                Supplier<TradeDto> pre = Tools.getPre(orderNo, redisTemplate);
                if(pre.get() == null) {
                    break;
                }
                sellData = pre.get();
                addEx(sellData);
                //需要修改买入与买出的值;
                boolean fx = !(sellData.getPrice().compareTo(buyData.getPrice()) > 0);
                //如果没有全部成交完毕 则data不能为null
                if (fx) {
                    logger.info("进入buy交易处理,找到对应的交易数据{}:{}", buyData.getOrderNo(), sellData.getOrderNo());
                    //buy log
                    String tradeNo = SnowFlake.createSnowFlake().nextIdString();
                    //修改卖出的锁定金额为o
                    //修改买入的金额数据；
                    //这里开始记录成功；
                    boolean isBreak;
                    ComputerResult result = compute(buyData, sellData, sellData.getPrice());
                    if (logger.isDebugEnabled()) {
                        logger.debug("buy交易计算结果：{}:{}:{}", buyData, sellData, result);
                    }
                    tradeResult.setComputerResult(result);
                    tradeResult.setBuyOrderNo(buyData.getOrderNo());
                    tradeResult.setSellOrderNo(sellData.getOrderNo());
                    tradeResult.setIndex(index++);
                    tradeResult.setBuyDto(buyData);
                    tradeResult.setSellDto(sellData);
                    //修改双方的Redis;
                    if (result.getBuySuccess()) {
                        isBreak = true;
                        makeZero(buyData);
                        tradeResult.setBuyOrderStatus(OrderEnum.OrderStatus.ALL_SUCCESS);
                        allSuccessful = true;
                    } else {
                        buyData.setCancelLock(tradeNo);
                        BigDecimal subtract = TradeCompute.subtract(buyData.getComputerVolume(), result.getSellSpent());
                        //计算可退出的金额
                        noneMakeZero(buyData, result.getBuyBalance(), subtract);
                        tradeResult.setBuyOrderStatus(OrderEnum.OrderStatus.PART_SUCCESS);
                        isBreak = false;
                    }
                    if (result.getSellSuccess()) {
                        makeZero(sellData);
                        tradeResult.setSellOrderStatus(OrderEnum.OrderStatus.ALL_SUCCESS);
                    } else {
                        sellData.setCancelLock(tradeNo);
                        noneMakeZero(sellData, result.getSellBalance(), result.getSellBalance());
                        tradeResult.setSellOrderStatus(OrderEnum.OrderStatus.PART_SUCCESS);
                    }
                    tradeResult.setTradeNo(tradeNo);
                    //下面的消息为处理的流水信息;
                    TradeDetail stream = new TradeDetail();
                    stream.setCoinMain(buyData.getCoinMain());
                    stream.setTradeNo(tradeNo);
                    stream.setExFee(result.getBuyFee());
                    stream.setCoinOther(buyData.getCoinOther());
                    LocalDateTime nowTime = LocalDateTime.now();
                    stream.setTradeTime(nowTime);
                    LocalTime time = LocalTime.of(nowTime.getHour(), nowTime.getMinute(), 0);
                    LocalDateTime minuteTime = LocalDateTime.of(nowTime.toLocalDate(), time);
                    stream.setMinuteTime(minuteTime);
                    stream.setOrderNo(buyData.getOrderNo());
                    stream.setPrice(buyData.getPrice());
                    stream.setType(buyData.getType().ordinal());
                    stream.setVolume(result.getBuyIncome());
                    stream.setToUserId(sellData.getUserId());
                    stream.setUserId(buyData.getUserId());
                    stream.setMainTrade(TradeEnum.BUY);
                    stream.setTradeVolume(result.getTradeVolume());
                    stream.setStatus(tradeResult.getBuyOrderStatus());
                    stream.setLastPrice(sellData.getPrice());
                    buyList.add(stream);

                    //sell log;
                    stream = new TradeDetail();
                    stream.setMainTrade(TradeEnum.BUY);
                    stream.setCoinMain(sellData.getCoinMain());
                    stream.setToUserId(buyData.getUserId());
                    stream.setTradeNo(tradeNo);
                    stream.setCoinOther(sellData.getCoinOther());
                    stream.setTradeTime(nowTime);
                    stream.setExFee(result.getSellFee());
                    stream.setMinuteTime(minuteTime);
                    stream.setOrderNo(sellData.getOrderNo());
                    stream.setPrice(sellData.getPrice());
                    stream.setType(sellData.getType().ordinal());
                    stream.setVolume(result.getSellIncome());
                    stream.setUserId(sellData.getUserId());
                    stream.setTradeVolume(result.getTradeVolume());
                    stream.setStatus(tradeResult.getSellOrderStatus());
                    stream.setLastPrice(sellData.getPrice());
                    buyList.add(stream);
                    results.add(tradeResult);
                    if (isBreak) {
                        break;
                    }
                } else {
                    break;
                }
            } finally {
                removeEx(sellData);
                if (sellLock != null) {
                    sellLock.unlock();
                }
            }
        }
        if (!buyList.isEmpty()) {
            //重置一次用户的计算金额
            //计算一下消息的数量；
            sendQueue(QueueType.TR_DE_MESSAGE, buyList);
            sendQueue(QueueType.TR_RE_MESSAGE, results);
        }
        processData.setOrderComplete(allSuccessful);
    }

}
