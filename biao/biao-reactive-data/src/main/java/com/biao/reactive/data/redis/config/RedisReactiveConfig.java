package com.biao.reactive.data.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/4/6 下午12:43
 * @since JDK 1.8
 */
@Configuration
@EnableRedisRepositories(basePackages = {"com.biao.reactive.data.redis.domain"})
public class RedisReactiveConfig {

    /**
     * Configures a {@link ReactiveRedisTemplate} with {@link String} keys and {@link GenericJackson2JsonRedisSerializer}.
     */
    @Bean(name = "reactiveRedisTemplate")
    public ReactiveRedisTemplate<Object, Object> reactiveJsonObjectRedisTemplate(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {

        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder = RedisSerializationContext
                .newSerializationContext(new StringRedisSerializer());

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisSerializationContext serializationContext =
                builder
                        .value(jackson2JsonRedisSerializer)
                        .hashKey(new StringRedisSerializer())
                        .hashValue(jackson2JsonRedisSerializer)
                        .key(new StringRedisSerializer()).build();

        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
    }


}