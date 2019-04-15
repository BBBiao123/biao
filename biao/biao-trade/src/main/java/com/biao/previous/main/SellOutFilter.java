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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 *
 * @date 2018/4/15
 * 卖出撮合交易；
 */
public class SellOutFilter extends AbstractTrade {
    private Logger logger = LoggerFactory.getLogger(SellOutFilter.class);

    /**
     * 初始化；
     *
     * @param distribute        provider
     * @param redisCacheManager redisCacheManager
     */
    public SellOutFilter(QueueDistribute distribute, RedisCacheManager redisCacheManager) {
        super(distribute, redisCacheManager);
    }

    @SuppressWarnings("all")
    @Override
    public void doFilter0(TradeDto sellDto, ProcessData processData, DataChain chain) {
        if (processData.getSkip()) return;
        logger.info("进入sell交易处理{}", sellDto);
        //拿到交询的高值;
        TradeDto buyDto = null;
        //判断只到下次获取数据，取不到。。则从重新放为mysql中。
        List<TradeDetail> buyList = new ArrayList<>();
        // 获取到相应的处理结果；
        List<TradeResult> results = new ArrayList<>();
        int index = 0;
        //表示是不是已经全部成交成功；false 部分成功；true 全部成功；
        boolean allSuccessful = false;
        //这下面是获取自己的数据
        while (true) {
            Lock buyLock = null;
            try {
                long size = (Long) redisTemplate.opsForZSet().size(sellDto.getType().reverseRedisKey(sellDto.getCoinMain(),
                        sellDto.getCoinOther()));
                if (size == 0) break;
                /*
                 *买出的结果消息；
                 */
                TradeResult tradeResult = new TradeResult();
                String buyKey = asKey(TradeEnum.BUY, sellDto.getCoinMain(), sellDto.getCoinOther());
                String orderNo = TradeCache.INST.get(buyKey, Tools.getBuyFirst(sellDto.getCoinMain(), sellDto.getCoinOther(), redisTemplate));
                if (StringUtils.isBlank(orderNo)) {
                    break;
                }
                buyLock = redissonClient.getLock(orderNo);
                buyLock.lock();
                Supplier<TradeDto> pre = Tools.getPre(orderNo, redisTemplate);
                if(pre.get() == null) {
                    break;
                }
                buyDto = pre.get();
                addEx(buyDto);
                //需要修改买入与买出的值;
                boolean fx = !(sellDto.getPrice().compareTo(buyDto.getPrice()) > 0);
                //如果没有全部成交完毕 则data不能为null
                if (fx) {
                    String tradeNo = SnowFlake.createSnowFlake().nextIdString();
                    logger.info("进入sell交易处理,找到对应的交易数据{}:{}", sellDto.getOrderNo(), buyDto.getOrderNo());
                    //修改卖出的锁定金额为o
                    //修改买入的金额数据；
                    //这里开始记录成功；
                    boolean isBreak;
                    AbstractTrade.ComputerResult result = compute(buyDto, sellDto, buyDto.getPrice());
                    if (logger.isDebugEnabled()) {
                        logger.debug("sell交易计算结果：{}:{}:{}", buyDto, sellDto, result);
                    }
                    tradeResult.setComputerResult(result);
                    tradeResult.setBuyOrderNo(buyDto.getOrderNo());
                    tradeResult.setSellOrderNo(sellDto.getOrderNo());
                    tradeResult.setIndex(index++);
                    tradeResult.setBuyDto(buyDto);
                    tradeResult.setSellDto(sellDto);
                    //修改双方的Redis;
                    if (result.getBuySuccess()) {
                        makeZero(buyDto);
                        tradeResult.setBuyOrderStatus(OrderEnum.OrderStatus.ALL_SUCCESS);
                    } else {
                        buyDto.setCancelLock(tradeNo);
                        BigDecimal subtract = TradeCompute.subtract(buyDto.getComputerVolume(), result.getSellSpent());
                        noneMakeZero(buyDto, result.getBuyBalance(), subtract);
                        tradeResult.setBuyOrderStatus(OrderEnum.OrderStatus.PART_SUCCESS);
                    }
                    if (result.getSellSuccess()) {
                        isBreak = true;
                        makeZero(sellDto);
                        tradeResult.setSellOrderStatus(OrderEnum.OrderStatus.ALL_SUCCESS);
                        allSuccessful = true;
                    } else {
                        sellDto.setCancelLock(tradeNo);
                        noneMakeZero(sellDto, result.getSellBalance(), result.getSellBalance());
                        tradeResult.setSellOrderStatus(OrderEnum.OrderStatus.PART_SUCCESS);
                        isBreak = false;
                    }
                    //buy log
                    tradeResult.setTradeNo(tradeNo);
                    //下面的消息为处理的流水信息;
                    TradeDetail stream = new TradeDetail();
                    stream.setCoinMain(buyDto.getCoinMain());
                    stream.setTradeNo(tradeNo);
                    stream.setExFee(result.getBuyFee());
                    stream.setCoinOther(sellDto.getCoinOther());
                    LocalDateTime nowTime = LocalDateTime.now();
                    stream.setTradeTime(nowTime);
                    LocalTime time = LocalTime.of(nowTime.getHour(), nowTime.getMinute(), 0);
                    LocalDateTime minuteTime = LocalDateTime.of(nowTime.toLocalDate(), time);
                    stream.setMinuteTime(minuteTime);
                    stream.setOrderNo(buyDto.getOrderNo());
                    stream.setPrice(buyDto.getPrice());
                    stream.setType(buyDto.getType().ordinal());
                    stream.setVolume(result.getBuyIncome());
                    stream.setToUserId(sellDto.getUserId());
                    stream.setUserId(buyDto.getUserId());
                    stream.setTradeVolume(result.getTradeVolume());
                    stream.setMainTrade(TradeEnum.SELL);
                    stream.setStatus(tradeResult.getBuyOrderStatus());
                    stream.setLastPrice(buyDto.getPrice());
                    buyList.add(stream);

                    //sell log;
                    stream = new TradeDetail();
                    stream.setMainTrade(TradeEnum.SELL);
                    stream.setCoinMain(sellDto.getCoinMain());
                    stream.setToUserId(buyDto.getUserId());
                    stream.setUserId(sellDto.getUserId());
                    stream.setTradeNo(tradeNo);
                    stream.setCoinOther(sellDto.getCoinOther());
                    stream.setTradeTime(nowTime);
                    stream.setExFee(result.getSellFee());
                    stream.setMinuteTime(minuteTime);
                    stream.setOrderNo(sellDto.getOrderNo());
                    stream.setPrice(sellDto.getPrice());
                    stream.setType(sellDto.getType().ordinal());
                    stream.setVolume(result.getSellIncome());
                    stream.setTradeVolume(result.getTradeVolume());
                    stream.setStatus(tradeResult.getSellOrderStatus());
                    stream.setLastPrice(buyDto.getPrice());
                    buyList.add(stream);
                    results.add(tradeResult);
                    if (isBreak) {
                        break;
                    }
                } else {
                    break;
                }
            } finally {
                removeEx(buyDto);
                if (null != buyLock) {
                    buyLock.unlock();
                }
            }
        }
        if (!buyList.isEmpty()) {
            //计算一下消息的数量；
            sendQueue(QueueType.TR_DE_MESSAGE, buyList);
            sendQueue(QueueType.TR_RE_MESSAGE, results);
        }
        processData.setOrderComplete(allSuccessful);
    }
}
