package com.biao.redis;

import com.biao.BaseTest;
import com.biao.entity.City;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 *  ""(Myth)
 */
public class RedisClusterTest extends BaseTest {

    @Autowired
    private RedisTemplate redisTemplate;


    @Resource(name = "reactiveRedisTemplate")
    private ReactiveRedisTemplate reactiveRedisTemplate;


    @Test
    public void testCluster() {

        redisTemplate.boundValueOps("ttt").set("ttt");
        final String ttt = (String) redisTemplate.boundValueOps("ttt").get();
        System.out.println(ttt);

        String key = "ttt";
        City city = new City();
        city.setCityName("深圳");
        reactiveRedisTemplate.opsForValue().set(key, city).subscribe(System.out::println);

        City block1 = (City) reactiveRedisTemplate.opsForValue().get(key).block();

        System.out.println(block1);

    }


}
