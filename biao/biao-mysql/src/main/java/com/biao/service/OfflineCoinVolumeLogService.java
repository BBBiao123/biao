package com.biao.service;

import com.biao.entity.OfflineCoinVolume;

public interface OfflineCoinVolumeLogService {
    void saveLog(OfflineCoinVolume offlineCoinVolume, String batchNo);

    void saveLog(OfflineCoinVolume offlineCoinVolume, String batchNo, String remark);

    void saveLog(String userId, String coinId, String batchNo);
}
