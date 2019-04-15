package com.biao.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.BiConsumer;

public interface MkCommonService {

    /**
     * 汇总平台一天手续费入某账户
     *
     * @param userId
     */
    void statisticsCoin2User(LocalDateTime beginDate, LocalDateTime endDate, String userId, BiConsumer<String, BigDecimal> action);

    void mk2StatisticsCoin2User();

    void saveTaskResult(LocalDateTime beginDate, LocalDateTime endDate, String status, String remark);
}
