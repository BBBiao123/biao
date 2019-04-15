package com.biao.service;

import com.biao.entity.OfflineCancelLog;

import java.time.LocalDate;

public interface OfflineCancelLogService {

    void save(OfflineCancelLog offlineCancelLog);

    OfflineCancelLog findById(String id);

    long findCountByUserIdAndTypeAndDate(String userId, String i, LocalDate now);
}
