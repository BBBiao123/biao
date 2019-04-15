package com.biao.service;

import com.biao.entity.OfflineChangeLog;
import com.biao.pojo.OfflineChangeVO;
import com.biao.pojo.ResponsePage;
import com.biao.vo.OfflineChangeListVO;

public interface OfflineChangeLogService {

    String save(OfflineChangeLog offlineChangeLog);

    long updateById(OfflineChangeLog offlineChangeLog);

    OfflineChangeLog findById(String id);

    ResponsePage<OfflineChangeLog> findPage(OfflineChangeListVO offlineChangeListVO);

    OfflineChangeVO preCheck(OfflineChangeVO offlineChangeVO, String userId);

    OfflineChangeVO preConfirm(OfflineChangeVO offlineChangeVO, String userId);

    long confirm(OfflineChangeVO offlineChangeVO, String userId);
}
