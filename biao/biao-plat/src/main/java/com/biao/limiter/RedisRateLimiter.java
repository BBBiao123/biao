/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one or more
 *  * contributor license agreements.  See the NOTICE file distributed with
 *  * this work for additional information regarding copyright ownership.
 *  * The ASF licenses this file to You under the Apache License, Version 2.0
 *  * (the "License"); you may not use this file except in compliance with
 *  * the License.  You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.biao.limiter;

import com.biao.constant.Constants;
import com.biao.pojo.GlobalMessageResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * See https://stripe.com/blog/rate-limiters and
 * https://gist.github.com/ptarjan/e38f45f2dfe601419ca3af937fff574d#file-1-check_request_rate_limiter-rb-L11-L34
 * See  https://github.com/spring-cloud/spring-cloud-gateway/blob/master/spring-cloud-gateway-core/src/main/java/org/springframework/cloud/gateway/filter/ratelimit/RedisRateLimiter.java
 * RedisRateLimiter.
 *
 *  ""
 */
public class RedisRateLimiter {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisRateLimiter.class);

    private ReactiveRedisTemplate<String, String> redisTemplate;

    private RedisScript<List<Long>> script;

    private AtomicBoolean initialized = new AtomicBoolean(false);

    /**
     * Instantiates a new Redis rate limiter.
     *
     * @param redisTemplate the redis template
     * @param script        the script
     */
    public RedisRateLimiter(final ReactiveRedisTemplate<String, String> redisTemplate, final RedisScript<List<Long>> script) {
        this.redisTemplate = redisTemplate;
        this.script = script;
        initialized.compareAndSet(false, true);
    }

    /**
     * This uses a basic token bucket algorithm and relies on the fact that Redis scripts
     * execute atomically. No other operations can run between fetching the count and
     * writing the new count.
     *
     * @param id            is rule id
     * @param replenishRate replenishRate
     * @param burstCapacity burstCapacity
     * @return {@code Mono<Response>} to indicate when request processing is complete
     */
    public Mono<GlobalMessageResponseVo> isAllowed(final String id, final double replenishRate, final double burstCapacity, RateLimiterHandler rateLimiterHandler, Object[] param) {
        if (!this.initialized.get()) {
            throw new IllegalStateException("RedisRateLimiter is not initialized");
        }
        try {
            List<String> keys = getKeys(id);
            List<String> scriptArgs = Arrays.asList(replenishRate + "", burstCapacity + "",
                    Instant.now().getEpochSecond() + "", "1");
            Flux<List<Long>> resultFlux = this.redisTemplate.execute(this.script, keys, scriptArgs);
            return resultFlux.onErrorResume(throwable -> Flux.just(Arrays.asList(1L, -1L)))
                    .reduce(new ArrayList<Long>(), (longs, l) -> {
                        longs.addAll(l);
                        return longs;
                    }).map(results -> {
                        boolean allowed = results.get(0) == 1L;
                        if (allowed == true) {
                            return rateLimiterHandler.handler(param);
                        }
                        return GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR, "c2c交易被限流 ");
                    });
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error determining if user allowed from redis" + e.getMessage());
        }
        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR, "c2c交易被限流 "));
    }

    private static List<String> getKeys(final String id) {
        String prefix = "request_rate_limiter.{" + id;
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

}
