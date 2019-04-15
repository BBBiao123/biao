package com.biao.service;

import com.biao.entity.ReportTradeFree;

import java.time.LocalDate;
import java.util.List;

public interface ReportTradeFreeService {

    void insert(ReportTradeFree reportTradeFree);

    void batchInsert(List<ReportTradeFree> reportTradeFrees);

    /**
     * 分组所有的交易对
     *
     * @return
     */
    List<ReportTradeFree> groupCoinMainAndOtherByCountTime(LocalDate countTime);

    void exeFreeCoins(LocalDate countTime);

    void exeFreeRecords(LocalDate countTime);
}
