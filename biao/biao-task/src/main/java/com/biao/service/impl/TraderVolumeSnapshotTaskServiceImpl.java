package com.biao.service.impl;

import com.biao.mapper.TraderVolumeSnapshotDao;
import com.biao.service.TraderVolumeSnapshotTaskService;
import com.biao.util.DateUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TraderVolumeSnapshotTaskServiceImpl implements TraderVolumeSnapshotTaskService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(TraderVolumeSnapshotTaskServiceImpl.class);

    @Autowired
    private TraderVolumeSnapshotDao traderVolumeSnapshotDao;

    @Override
    public void triggerTraderVolumeSnapshotEntry() {
        try {
            LocalDate curDateTime = LocalDate.now();
            String curDate = DateUtils.formaterDate(curDateTime);
            long count = traderVolumeSnapshotDao.findTraderVolumeSnapshot(curDate);
            if (count > 0) {
                logger.info(String.format("[%s]执行失败,已存在操盘手资产快照！", curDate));
                return;
            } else {
                //操盘手
                traderVolumeSnapshotDao.insertTraderVolumeSnapshot(curDate);
                //ALL
                traderVolumeSnapshotDao.insertAllVolumeSnapshot(curDate);
                //AUTO-TRADER
                traderVolumeSnapshotDao.insertAutoTraderVolumeSnapshot(curDate);
                logger.info(String.format("[%s]执行成功-生成操盘手资产快照！", curDate));
            }
        } catch (Exception e) {
            logger.info("生成操盘手快照失败，" + e.getMessage());
        }
    }
}
