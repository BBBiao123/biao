package com.biao.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

public class StringRedisController {
    protected static Logger logger = LoggerFactory.getLogger(StringRedisController.class);

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;


    //例如：http://localhost:8081/redis/string/set?key=address&value=guangzhou
    @RequestMapping("/set")
    public String setKeyAndValue(String key, String value) {
        logger.debug("访问set:key={},value={}", key, value);
        valOpsStr.set(key, value);
        return "Set Ok";
    }

    //例如：http://localhost:8081/redis/string/get?key=address
    @RequestMapping("/get")
    public String getKey(String key) {
        logger.debug("访问get:key={}", key);
        return valOpsStr.get(key);
    }
}
