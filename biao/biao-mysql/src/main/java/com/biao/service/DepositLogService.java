package com.biao.service;

import com.biao.entity.DepositLog;
import com.biao.pojo.DepdrawLogVO;
import com.biao.pojo.ResponsePage;
import com.biao.vo.DepositListVO;

public interface DepositLogService {


    DepositLog findById(String id);

    String save(DepositLog depositLog);

    void updateById(DepositLog depositLog);

    ResponsePage<DepositLog> findPage(DepositListVO depositListVO);

    ResponsePage<DepdrawLogVO> findDepdrawLogPage(DepositListVO depositListVO);
}
