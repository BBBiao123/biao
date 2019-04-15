/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.biao.limiter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;

/**
 * use  RedisLimiterConfiguration.
 *
 *  ""
 */
@Configuration
@ConditionalOnClass({RedisTemplate.class})
@SuppressWarnings("unchecked")
public class RedisLimiterConfiguration {

    /**
     * init  RedisScript.
     *
     * @return {@linkplain RedisScript}
     */
    @Bean
    @SuppressWarnings("unchecked")
    public RedisScript redisScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/META-INF/scripts/request_rate_limiter.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }

    /**
     * init ReactiveRedisTemplate.
     *
     * @param reactiveRedisConnectionFactory {@linkplain ReactiveRedisConnectionFactory}
     * @return {@linkplain ReactiveRedisTemplate}
     */
    @Bean
    @Qualifier("reactiveRedisTemplate")
    public ReactiveRedisTemplate<String, String> stringReactiveRedisTemplate(final ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        RedisSerializer<String> serializer = new StringRedisSerializer();
        RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
                .<String, String>newSerializationContext()
                .key(serializer)
                .value(serializer)
                .hashKey(serializer)
                .hashValue(serializer)
                .build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory,
                serializationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisRateLimiter redisRateLimiter(@Qualifier("reactiveRedisTemplate") final ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
        return new RedisRateLimiter(reactiveRedisTemplate, redisScript());
    }
}
