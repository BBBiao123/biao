package com.biao.previous.main;

import com.biao.constant.TradeConstant;
import com.biao.pojo.TradeDto;
import com.biao.previous.domain.ProcessData;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @date 2018/4/7
 * <p>
 * 前置处理的Filter
 * 1.校验
 * 2.消息路由.
 */
public class PrepositionFilter extends AbstractFilter {
    private Logger logger = LoggerFactory.getLogger(PrepositionFilter.class);

    /**
     * Redis处理;
     */
    @SuppressWarnings("all")
    @Override
    public void doFilter0(TradeDto data, ProcessData processData, DataChain chain) {
        //通过订单号加锁
        boolean flag = false;
        boolean ocFlag = false;
        try {
            RLock lock = redissonClient.getLock(data.getOrderNo());
            lock.lock();
            logger.info("进入撮合引擎{}", data);
            //第一步写入Redis中进入交易引擎
            flag = redisTemplate
                    .opsForHash()
                    .hasKey(TradeConstant.TRADE_PREPOSITION_KEY, data.getOrderNo());
            ocFlag = !flag;
        } catch (Exception ex) {
            logger.error("发生异常，跳过撮合，等待别人撮合,", ex);
            ocFlag = false;
        } finally {
            //哪果不存在交易引警中进入处理，则可以进入处理引擎
            if (flag) {
                processData.setConcurrentTime(System.currentTimeMillis());
                addEx(data);//表示进入交易引擎
                processData.setSkip(false);
                processData.setOrderComplete(false);
            } else {
                processData.setSkip(true);
                processData.setOrderComplete(ocFlag);
            }
        }
    }
}
