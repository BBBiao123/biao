package com.biao.service.impl;

import com.biao.entity.ReportTradeFree;
import com.biao.entity.ReportTradeFreeCoin;
import com.biao.entity.ReportTradeFreeRecord;
import com.biao.mapper.ReportTradeFreeCoinDao;
import com.biao.mapper.ReportTradeFreeDao;
import com.biao.mapper.ReportTradeFreeRecordDao;
import com.biao.service.ReportTradeFreeService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("reportTradeFreeService")
public class ReportTradeFreeServiceImpl implements ReportTradeFreeService {

    @Autowired
    private ReportTradeFreeDao reportTradeFreeDao;

    @Autowired
    private ReportTradeFreeCoinDao reportTradeFreeCoinDao;

    @Autowired
    private ReportTradeFreeRecordDao reportTradeFreeRecordDao;

    @Override
    public void batchInsert(List<ReportTradeFree> reportTradeFrees) {
        if (!CollectionUtils.isEmpty(reportTradeFrees)) {
            reportTradeFreeDao.batchInsert(reportTradeFrees);
        }
    }

    @Override
    public void insert(ReportTradeFree reportTradeFree) {
        reportTradeFreeDao.insert(reportTradeFree);
    }

    @Override
    public List<ReportTradeFree> groupCoinMainAndOtherByCountTime(LocalDate countTime) {
        return reportTradeFreeDao.groupTradeListByCountTime(countTime);
    }

    @Override
    public void exeFreeCoins(LocalDate countTime) {
        List<ReportTradeFreeCoin> reportTradeFreeCoins = reportTradeFreeDao.countTradeFreeCoins(countTime);
        if (!CollectionUtils.isEmpty(reportTradeFreeCoins)) {
            reportTradeFreeCoins.stream().forEach(reportTradeFreeCoin -> {
                reportTradeFreeCoin.setCountTime(countTime);
                reportTradeFreeCoin.setCreateTime(LocalDateTime.now());
                reportTradeFreeCoin.setId(SnowFlake.createSnowFlake().nextIdString());
            });
            reportTradeFreeCoinDao.batchInsert(reportTradeFreeCoins);
        }
    }

    @Override
    public void exeFreeRecords(LocalDate countTime) {
        List<ReportTradeFree> reportTradeFrees = reportTradeFreeDao.groupTradeListByCountTime(countTime);
        if (!CollectionUtils.isEmpty(reportTradeFrees)) {
            List<ReportTradeFreeRecord> reportTradeFreeRecords = new ArrayList<>(1000);
            reportTradeFrees.stream().forEach(reportTradeFree -> {
                List<ReportTradeFree> insertReportTradeFrees = reportTradeFreeDao.groupTradeList(countTime, reportTradeFree.getCoinMain(), reportTradeFree.getCoinOther());
                ReportTradeFreeRecord reportTradeFreeRecord = megerReportTradeFree(insertReportTradeFrees, countTime);
                if (reportTradeFreeRecord != null) {
                    reportTradeFreeRecords.add(reportTradeFreeRecord);
                }
            });
            if (reportTradeFreeRecords.size() > 0) {
                reportTradeFreeRecordDao.batchInsert(reportTradeFreeRecords);
            }
        }
    }

    public ReportTradeFreeRecord megerReportTradeFree(List<ReportTradeFree> reportTradeFrees, LocalDate countTime) {
        ReportTradeFreeRecord freeRecord = new ReportTradeFreeRecord();
        if (reportTradeFrees.size() != 2) {
            return null;
        }
        ReportTradeFree mainCoin = reportTradeFrees.stream().filter(reportTradeFree -> reportTradeFree.getCoin().equals(reportTradeFree.getCoinMain())).findFirst().get();
        ReportTradeFree ohterCoin = reportTradeFrees.stream().filter(reportTradeFree -> reportTradeFree.getCoin().equals(reportTradeFree.getCoinOther())).findFirst().get();
        freeRecord.setCoinMain(mainCoin.getCoinMain());
        freeRecord.setCoinOther(mainCoin.getCoinOther());
        freeRecord.setCountTime(countTime);
        freeRecord.setCreateTime(LocalDateTime.now());
        freeRecord.setId(SnowFlake.createSnowFlake().nextIdString());
        freeRecord.setMainFree(mainCoin.getSumFee());
        freeRecord.setOtherFree(ohterCoin.getSumFee());
        return freeRecord;
    }
}
