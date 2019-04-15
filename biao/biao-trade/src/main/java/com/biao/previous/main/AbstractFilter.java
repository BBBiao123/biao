package com.biao.previous.main;

import com.biao.constant.TradeConstant;
import com.biao.enums.TradeEnum;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.pojo.TradeDto;
import com.biao.previous.Tools;
import com.biao.previous.domain.ProcessData;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

/**
 *
 * @date 2018/4/15
 * 处理
 */
public abstract class AbstractFilter implements Filter {
    /**
     * Redis处理程序；
     */
    RedisTemplate redisTemplate;

    /**
     * Reiis锁处理；
     */
    RedissonClient redissonClient;

    /**
     * kafka消息发送模板
     */
    KafkaTemplate<Object, SampleMessage> kafkaTemplate;


    @Override
    public Filter setRedis(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        return this;
    }

    @Override
    public Filter setRedisson(RedissonClient redisson) {
        this.redissonClient = redisson;
        return this;
    }

    @Override
    public Filter setKafka(KafkaTemplate<Object, SampleMessage> kafka) {
        this.kafkaTemplate = kafka;
        return this;
    }

    @Override
    public void doFilter(TradeDto data, ProcessData processData, DataChain chain) {
        doFilter0(data, processData, chain);
        chain.doFilter(data, processData, chain);
    }

    /**
     * 加入到redis，标识进行引擎处理了；
     */
    void addEx(TradeDto dto) {
        if (dto == null) {
            return;
        }
        //放入交易reids判断;
        redisTemplate.opsForHash().put(TradeConstant.TRADE_REDIS_ENGINE_ID_KEY, dto.getOrderNo(), true);
    }

    /**
     * 表示退出交易所；
     *
     * @param dto 交易所；
     */
    void removeEx(TradeDto dto) {
        if (dto == null) {
            return;
        }
        //放入交易reids判断;
        redisTemplate.opsForHash().delete(TradeConstant.TRADE_REDIS_ENGINE_ID_KEY, dto.getOrderNo());
    }

    /**
     * 业务执行的Filter;
     *
     * @param data        数据处理；
     * @param processData 过程中存在的数据;
     * @param chain       处理链;
     */
    public abstract void doFilter0(TradeDto data, ProcessData processData, DataChain chain);

    /**
     * 获取一个KEY;
     *
     * @param dto dto;
     * @return key;
     */
    String asKey(TradeDto dto) {
        return Tools.asCacheKey(dto);
    }

    /**
     * 获取一个缓存key;
     *
     * @param type      type;
     * @param coinMain  主币
     * @param coinOther 交易区;
     * @return key;
     */
    String asKey(TradeEnum type, String coinMain, String coinOther) {
        return Tools.asCacheKey(type, coinMain, coinOther);
    }
}
