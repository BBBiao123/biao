package com.biao.service.impl;

import com.biao.entity.ReportTradeDay;
import com.biao.mapper.ReportTradeDayDao;
import com.biao.service.ReportTradeDayService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("reportTradeDayService")
public class ReportTradeDayServiceImpl implements ReportTradeDayService {

    @Autowired
    private ReportTradeDayDao reportTradeDayDao;

    @Transactional
    @Override
    public void insert(ReportTradeDay reportTradeDay) {
        if (reportTradeDay.getId() == null) {
            String id = SnowFlake.createSnowFlake().nextIdString();
            reportTradeDay.setId(id);
        }
        reportTradeDayDao.insert(reportTradeDay);
    }

    @Transactional
    @Override
    public void batchInsert(List<ReportTradeDay> reportTradeDays) {
        reportTradeDayDao.batchInsert(reportTradeDays);
    }

}
