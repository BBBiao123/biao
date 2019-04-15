package com.biao.service.impl;

import com.biao.service.SerialCodeService;
import com.biao.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Service
public class SerialCodeServiceImpl implements SerialCodeService {

    private static final Long dailyStart = 10000000L;
    private static final Long dailyEnd = 99999999L;

    private static final String code = "common";
    private volatile String key;
    private volatile int dayOfYear = -1;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valOpsStr;

    @Override
    public String generateSerialCode() {
        Calendar calendar = Calendar.getInstance();
        String dateTimeStr = DateUtil.formatDate(calendar.getTime(), "yyyyMMddHHmmssSSS");
        if (dayOfYear != calendar.get(Calendar.DAY_OF_YEAR)) {
            key = "biao-UTIL-SEQ:" + calendar.get(Calendar.DAY_OF_YEAR) + ":" + code;
            if (valOpsStr.setIfAbsent(key, dailyStart + "")) {
                valOpsStr.getOperations().expire(key, 24 * 3601, TimeUnit.SECONDS);
                dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            }
        }
        Long seq = valOpsStr.increment(key, 1);
        if (seq > dailyEnd) {
            valOpsStr.set(key, dailyStart + "", 24 * 3601, TimeUnit.SECONDS);
            seq = valOpsStr.increment(key, 1); //循环从头开始
        }
        return dateTimeStr + seq;
    }

    @Override
    public String generateSerialCode(String prefix) {
        return prefix + generateSerialCode();
    }
}
