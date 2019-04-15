package com.biao.service.impl;

import com.biao.entity.OfflineTransferLog;
import com.biao.mapper.OfflineTransferLogDao;
import com.biao.pojo.ResponsePage;
import com.biao.service.OfflineTransferLogService;
import com.biao.util.SnowFlake;
import com.biao.vo.OfflineTransferListVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OfflineTransferLogServiceImpl implements OfflineTransferLogService {

    @Autowired
    private OfflineTransferLogDao offlineTransferLogDao;

    @Override
    public String save(OfflineTransferLog offlineTransferLog) {
        String id = SnowFlake.createSnowFlake().nextIdString();
        offlineTransferLog.setCreateDate(LocalDateTime.now());
        offlineTransferLog.setUpdateDate(LocalDateTime.now());
        offlineTransferLog.setId(id);
        offlineTransferLogDao.insert(offlineTransferLog);
        return id;
    }

    @Override
    public long updateById(OfflineTransferLog offlineTransferLog) {

        return 1L;
    }

    @Override
    public OfflineTransferLog findById(String id) {
        return offlineTransferLogDao.findById(id);
    }

    @Override
    public ResponsePage<OfflineTransferLog> findPage(OfflineTransferListVO offlineTransferListVO) {
        ResponsePage<OfflineTransferLog> responsePage = new ResponsePage<>();
        Page<OfflineTransferLog> page = PageHelper.startPage(offlineTransferListVO.getCurrentPage(), offlineTransferListVO.getShowCount());
        List<OfflineTransferLog> data;
        if (StringUtils.isEmpty(offlineTransferListVO.getCoinId())) {
            data = offlineTransferLogDao.findOfflineTransferLogByUserId(offlineTransferListVO.getUserId());

        } else {
            data = offlineTransferLogDao.findOfflineTransferLogByUserIdAndCoinId(offlineTransferListVO.getUserId(), offlineTransferListVO.getCoinId());
        }

        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }


}
