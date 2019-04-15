package com.biao.limiter;

import com.biao.pojo.GlobalMessageResponseVo;

public interface RateLimiterHandler {

    GlobalMessageResponseVo handler(Object[] param);
}
