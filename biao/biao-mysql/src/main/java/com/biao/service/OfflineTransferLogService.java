package com.biao.service;

import com.biao.entity.OfflineTransferLog;
import com.biao.pojo.ResponsePage;
import com.biao.vo.OfflineTransferListVO;

public interface OfflineTransferLogService {

    String save(OfflineTransferLog offlineTransferLog);

    long updateById(OfflineTransferLog offlineTransferLog);

    OfflineTransferLog findById(String id);


    ResponsePage<OfflineTransferLog> findPage(OfflineTransferListVO offlineTransferListVO);
}
