package com.biao.service;

import com.biao.enums.KlineTimeEnum;
import com.biao.service.kline.KlineLogDataConfig;

public interface KlineLogDataService {

    void klineLogData(KlineLogDataConfig config, boolean cache);

    void executePullKlineLogData(KlineTimeEnum dayType);
    
    void executePullKlineLogData(KlineTimeEnum dayType,boolean synRedis);
    
    void executePullKlineLogData(KlineTimeEnum dayType,boolean synRedis,boolean klineInitTag);
    
    void synRedis(KlineLogDataConfig config);
    
    void initKlineData(KlineLogDataConfig config);
}
