package com.biao.service.impl;

import com.biao.entity.OfflineCoinVolume;
import com.biao.entity.OfflineCoinVolumeLog;
import com.biao.mapper.OfflineCoinVolumeDao;
import com.biao.mapper.OfflineCoinVolumeLogDao;
import com.biao.service.OfflineCoinVolumeLogService;
import com.biao.util.SnowFlake;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OfflineCoinVolumeLogServiceImpl implements OfflineCoinVolumeLogService {

    @Autowired
    private OfflineCoinVolumeLogDao offlineCoinVolumeLogDao;

    @Autowired
    private OfflineCoinVolumeDao offlineCoinVolumeDao;


    @Transactional
    public void saveLog(OfflineCoinVolume offlineCoinVolume, String batchNo) {
       saveLog(offlineCoinVolume, batchNo, "");
    }

    @Transactional
    public void saveLog(OfflineCoinVolume offlineCoinVolume, String batchNo, String remark) {
        OfflineCoinVolumeLog log = new OfflineCoinVolumeLog();
        BeanUtils.copyProperties(offlineCoinVolume, log);
        log.setId(SnowFlake.createSnowFlake().nextIdString());
        log.setOfflineVolumeId(offlineCoinVolume.getId());
        log.setCreateDate(LocalDateTime.now());
        log.setBatchNo(batchNo);
        log.setRemark(remark);
        offlineCoinVolumeLogDao.insert(log);
    }

    @Override
    @Transactional
    public void saveLog(String userId, String coinId, String batchNo) {
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        saveLog(offlineCoinVolume, batchNo);
    }
}
