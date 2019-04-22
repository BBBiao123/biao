package com.biao.rebot.dao;


import com.biao.rebot.config.RobotParam;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisFactory.
 * <p>
 * redis信息初始化.
 * <p>
 * 18-12-19上午11:49
 *
 *  "" sixh
 */
public class RedisFactory {

    /**
     * 处理器.
     */
    private static volatile RedisTemplate redisTemplate;
    private static int maxIdle = 10;
    private static int maxTotal = 100;
    private static int maxWaitMillis = 2000;
    private static boolean testOnBorrow = false;
    private static boolean blockWhenExhausted = false;
    private static boolean testOnReturn = false;
    private static boolean testWhileIdle = true;
    private static int minEvictableIdleTimeMillis = 1000;
    private static int timeBetweenEvictionRunsMillis = 1000;
    private static int numTestsPerEvictionRun = 1000;

    /**
     * Get redis template redis template.
     *
     * @return the redis template
     */
    public static RedisTemplate getRedisTemplate(){
        if(redisTemplate == null) {
            synchronized (RedisFactory.class){
                if(redisTemplate == null){
                    init();
                }
            }
        }
        return redisTemplate;
    }

    @SuppressWarnings("unchecked")
    private static void init() {
     /*   JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setTestWhileIdle(testWhileIdle);
        poolConfig.setBlockWhenExhausted(blockWhenExhausted);
        poolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        poolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        poolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);*/
        //mode
        String mode = RobotParam.get().getRobotCtx().getRedis().getMode();
        //pwd
        String pwd = RobotParam.get().getRobotCtx().getRedis().getPassword();
        // host;
        String hosts = RobotParam.get().getRobotCtx().getRedis().getHost();
        //密码.
        RedisPassword password = RedisPassword.of(pwd);
        JedisConnectionFactory connectionFactory;
        if ("default".equalsIgnoreCase(mode)) {
            RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
            Iterable<String> splits = Splitter.on(";").split(hosts);
            splits.forEach(e -> {
                String[] host = e.split(":");
                configuration.setHostName(host[0]);
                configuration.setPort(Integer.parseInt(host[1]));
            });
            configuration.setPassword(password);
            connectionFactory = new JedisConnectionFactory(configuration);
        } else if ("cluster".equalsIgnoreCase(mode) || "default".equalsIgnoreCase(mode)) {
            RedisClusterConfiguration configuration = new RedisClusterConfiguration();
            configuration.setPassword(password);
            Iterable<String> splits = Splitter.on(";").split(hosts);
            splits.forEach(e->{
                String[] host = e.split(":");
                configuration.clusterNode(host[0],Integer.parseInt(host[1]));
            });
            connectionFactory = new JedisConnectionFactory(configuration);
        } else if ("sentinel".equalsIgnoreCase(mode)) {
            RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
            configuration.setPassword(password);
            configuration.setMaster(RobotParam.get().getRobotCtx().getRedis().getMaster());
            Iterable<String> splits = Splitter.on(";").split(hosts);
            splits.forEach(e->{
                String[] host = e.split(":");
                RedisNode node = new RedisNode(host[0],Integer.parseInt(host[1]));
                configuration.addSentinel(node);
            });
            connectionFactory = new JedisConnectionFactory(configuration);

        }else {
            throw new RuntimeException("singleton mode is not cluster or default");
        }
        connectionFactory.afterPropertiesSet();
        redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
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
    }
}
