package com.biao.service;

import com.biao.entity.DestroyAccountLog;
import com.biao.entity.mk2.Mk2PopularizeMiningConf;
import com.biao.entity.mk2.Mk2PopularizeMiningGiveCoinLog;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface Mk2PopularizeMiningService {

    void doPopularizeMining();

    void doHoldCoinMining(LocalDateTime countDate);

    void doTeamCoinMining(LocalDateTime countDate);

    BigDecimal holdCoinMining(Mk2PopularizeMiningConf conf, LocalDateTime countDate);

    BigDecimal teamCoinMining(Mk2PopularizeMiningConf conf, LocalDateTime countDate);

    void saveTaskResult(String type, String coinId, String coinSymbol, BigDecimal miningVol, BigDecimal grantVol, LocalDateTime countDate, String status, String remark);

    void initMiningTable(LocalDateTime countDate, String coinSymbol);

    DestroyAccountLog createDestroyAccountLog(String userId, BigDecimal volume, String coinId, String coinSymbol);

    void saveDestoryLogBatch(List<DestroyAccountLog> destroyLogs);

    Mk2PopularizeMiningGiveCoinLog createGiveCoinLog(String type, String userId, String coinId,
                                                     String coinSymbol, BigDecimal volume, BigDecimal totalVol,
                                                     LocalDateTime countDate, long orderNo, BigDecimal joinVolume,
                                                     BigDecimal maxSubVolume, BigDecimal teamHoldTotal, BigDecimal sourceVolume);
}
