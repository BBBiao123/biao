package com.biao.configuration.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;

/**
 * RedisConfiguration.
 *
 *  ""
 */
@Configuration
public class RedisConfiguration {

    @Autowired(required = false)
    private RedisProperties redisProperties;

    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            return Arrays.stream(params)
                    .map(obj -> sb.append(obj.toString())).toString();

        };
    }

    @Bean
    @SuppressWarnings("unchecked")
    public RedisTemplate redisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedissonClient createRedisson() {
        Config config = new Config();
        String schema = redisProperties.isSsl() ? "rediss://" : "redis://";
        //sentinel
        if (redisProperties.getSentinel() != null) {
            SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
            sentinelServersConfig.setMasterName(redisProperties.getSentinel().getMaster());
            final String[] nodes = redisProperties.getSentinel()
                    .getNodes().stream().map(node -> schema + node)
                    .toArray(String[]::new);
            sentinelServersConfig.addSentinelAddress(nodes);
            if (StringUtils.isNotBlank(redisProperties.getPassword())) {
                sentinelServersConfig.setPassword(redisProperties.getPassword());
            }
            sentinelServersConfig.setConnectTimeout(3000);
            sentinelServersConfig.setRetryAttempts(5);
            sentinelServersConfig.setRetryInterval(3000);
            sentinelServersConfig.setMasterConnectionMinimumIdleSize(5);
            sentinelServersConfig.setSlaveConnectionMinimumIdleSize(5);
            sentinelServersConfig.setPingConnectionInterval(10000);
            sentinelServersConfig.setScanInterval(2000);

        } else if (redisProperties.getCluster() != null) {
            final ClusterServersConfig clusterServersConfig = config.useClusterServers();
            final String[] nodes = redisProperties.getCluster().getNodes().stream().map(node -> schema + node)
                    .toArray(String[]::new);
            clusterServersConfig
                    .setScanInterval(2000)
                    .addNodeAddress(nodes);
            if (StringUtils.isNotBlank(redisProperties.getPassword())) {
                clusterServersConfig.setPassword(redisProperties.getPassword());
            }
        } else { //single server
            SingleServerConfig singleServerConfig = config.useSingleServer();
            // format as redis://127.0.0.1:7181 or rediss://127.0.0.1:7181 for SSL
            singleServerConfig.setAddress(schema + redisProperties.getHost() + ":" + redisProperties.getPort());
            if (StringUtils.isNotBlank(redisProperties.getPassword())) {
                singleServerConfig.setPassword(redisProperties.getPassword());
            }
        }
        return Redisson.create(config);
    }

}
