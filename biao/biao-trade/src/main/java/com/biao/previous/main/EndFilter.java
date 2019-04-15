package com.biao.previous.main;

import com.biao.pojo.TradeDto;
import com.biao.previous.domain.ProcessData;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @date 2018/4/15
 */
public class EndFilter extends AbstractFilter {

    private Logger logger = LoggerFactory.getLogger(EndFilter.class);

    @Override
    public void doFilter0(TradeDto data, ProcessData processData, DataChain chain) {
        try {
            //释放交易所
            removeEx(data);
            //对锁进行释放
            RLock lock = redissonClient.getLock(data.getOrderNo());
            lock.unlock();
            logger.info("锁已释放，交易完成:{}", data);
        } catch (Exception ex) {
            logger.error("锁已释放，交易错误:{}", ex);
        }
    }
}
