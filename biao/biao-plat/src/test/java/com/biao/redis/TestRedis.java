package com.biao.redis;

import com.biao.PlatApplication;
import com.biao.constant.RedisConstants;
import com.biao.entity.City;
import com.biao.reactive.data.mongo.domain.RedisMatchStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/4/7 下午2:04
 * @since JDK 1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlatApplication.class)
public class TestRedis {

    @Autowired
    private ReactiveRedisTemplate reactiveRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Test
    public void testRedis() {

        RedisMatchStream receive = new RedisMatchStream();
        receive.setCoinMain("ttt");
        receive.setCoinOther("ttt");
        receive.setMinuteTime(LocalDateTime.now());
        receive.setTradeTime(LocalDateTime.now());
        receive.setPrice(new BigDecimal(1.0000));
        receive.setVolume(new BigDecimal(1.0000));
        for (int i = 0; i < 500000; i++) {
            redisTemplate.opsForList().leftPush("ttt:ttt", receive);
        }

    }

    @Test
    public void bigDataPull() {
        final List<RedisMatchStream> receives =
                redisTemplate.opsForList().range("ttt:ttt", 0, -1);
        RedisMatchStream receive = new RedisMatchStream();
        receive.setCoinMain("ttt");
        receive.setCoinOther("ttt");
        receive.setMinuteTime(LocalDateTime.now());
        receive.setTradeTime(LocalDateTime.now());
        receive.setPrice(new BigDecimal(1.0000));
        receive.setVolume(new BigDecimal(1.0000));
        redisTemplate.opsForList().leftPush("ttt:ttt", receive);
        System.out.println(receives.size());
    }

    @Test
    public void redis3() {
        if (valOpsStr.setIfAbsent(RedisConstants.USER_INVOTE_KEY, "10000")) {
            //valOpsStr.set(RedisConstants.USER_INVOTE_KEY, maxInvote);
            System.out.println("======true=======");
        }
        Long invoteCodeInc = valOpsStr.increment(RedisConstants.USER_INVOTE_KEY, 1L);
        System.out.println("======true======invoteCodeInc:" + invoteCodeInc);
    }

    @Test
    public void testRedis2() {
        valOpsStr.set("a", "b", 100, TimeUnit.SECONDS);
        System.out.println(valOpsStr.get("a"));

    }

    @Test
    public void testRedisTemplate() {

        String key = "ttt";
        City city = new City();
        city.setCityName("深圳");
        reactiveRedisTemplate.opsForValue().set(key, city).subscribe(System.out::println);

        City block1 = (City) reactiveRedisTemplate.opsForValue().get(key).block();
        Mono<Boolean> mono = reactiveRedisTemplate.hasKey(key);
        Boolean block2 = mono.block();
        mono.subscribe(result -> {
            if (result) {
                Mono<City> cityMono = reactiveRedisTemplate.opsForValue().get(key);
                City block = cityMono.block();
            }
        });


    }


    public static String getString(ByteBuffer buffer) {

        Charset charset;

        CharsetDecoder decoder;

        CharBuffer charBuffer;

        try {

            charset = Charset.forName("UTF-8");

            decoder = charset.newDecoder();


            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());

            return charBuffer.toString();

        } catch (Exception ex) {

            ex.printStackTrace();

            return "error";

        }

    }
}