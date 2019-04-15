package com.biao.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Mk2PopularizeBonusService {

    void doPopularizeBonusTask();

    void saveTaskResult(String status, BigDecimal bonusVolume, String coinId, String coinSymbol, LocalDateTime beginDate, LocalDateTime endDate, String remark);

}
