package com.biao.service;

import com.biao.entity.MkDistributeLog;
import com.biao.pojo.ResponsePage;
import com.biao.vo.MkDistributeLogListVO;

public interface MkDistributeLogService {

    MkDistributeLog findById(String id);

    ResponsePage<MkDistributeLog> findPage(MkDistributeLogListVO mkDistributeLogListVO);
}
