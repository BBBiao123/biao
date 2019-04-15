package com.biao.redis;

import com.biao.BaseTest;
import com.biao.vo.redis.RedisUserCoinVolume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/6/18 下午5:22
 * @since JDK 1.8
 */
public class RedisCacheManagerTest extends BaseTest {

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Test
    public void test() {
        RedisUserCoinVolume coinVolume = redisCacheManager.acquireUserCoinVolume("1", "USDT");
        System.out.println(coinVolume);
    }
}
