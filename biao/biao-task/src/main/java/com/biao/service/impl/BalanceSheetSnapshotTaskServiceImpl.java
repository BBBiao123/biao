package com.biao.service.impl;

import com.biao.mapper.BalanceSheetSnapshotDao;
import com.biao.service.BalanceSheetSnapshotTaskService;
import com.biao.util.DateUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class BalanceSheetSnapshotTaskServiceImpl implements BalanceSheetSnapshotTaskService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(BalanceSheetSnapshotTaskServiceImpl.class);

    @Autowired
    private BalanceSheetSnapshotDao balanceSheetSnapshotDao;

    @Override
    public void triggerBalanceSheetSnapshotEntry() {
        try {

            LocalDate curDateTime = LocalDate.now().minusDays(1L);
            String curDate = DateUtils.formaterDate(curDateTime);
            LocalDateTime startDate = LocalDateTime.of(curDateTime, LocalTime.MIN);
            LocalDateTime endDate = LocalDateTime.of(curDateTime, LocalTime.MAX);

            long count = balanceSheetSnapshotDao.findBalanceSheetSnapshot(curDate);
            if (count > 0) {
                logger.info(String.format("[%s]执行失败,已存在资产负债快照！", curDate));
                return ;
            } else {
                balanceSheetSnapshotDao.insertBalanceSheetSnapshot(curDate, startDate, endDate);
                logger.info(String.format("[%s]执行成功-生成资产负债快照！", curDate));
            }
        } catch (Exception e) {
            logger.info("生成资产负债失败，" + e.getMessage());
        }
    }
}
