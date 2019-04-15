package com.biao.service;

import com.biao.entity.ReportTradeDay;

import java.util.List;

public interface ReportTradeDayService {

    void insert(ReportTradeDay reportTradeDay);

    void batchInsert(List<ReportTradeDay> reportTradeDays);
}
