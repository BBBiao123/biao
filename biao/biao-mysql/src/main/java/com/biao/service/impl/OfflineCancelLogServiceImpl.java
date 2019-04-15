package com.biao.service.impl;

import com.biao.entity.OfflineCancelLog;
import com.biao.mapper.OfflineCancelLogDao;
import com.biao.service.OfflineCancelLogService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class OfflineCancelLogServiceImpl implements OfflineCancelLogService {

    @Autowired
    private OfflineCancelLogDao offlineCancelLogDao;


    @Override
    @Transactional
    public void save(OfflineCancelLog offlineCancelLog) {

        String id = SnowFlake.createSnowFlake().nextIdString();
        offlineCancelLog.setId(id);
        offlineCancelLog.setDate(LocalDate.now());
        offlineCancelLogDao.insert(offlineCancelLog);

    }

    @Override
    public OfflineCancelLog findById(String id) {
        return offlineCancelLogDao.findById(id);
    }

    @Override
    public long findCountByUserIdAndTypeAndDate(String userId, String type, LocalDate date) {
        return offlineCancelLogDao.findCountByUserIdAndTypeAndDate(userId, type, date);
    }


}
