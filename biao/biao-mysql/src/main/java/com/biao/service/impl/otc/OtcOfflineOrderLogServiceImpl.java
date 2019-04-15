package com.biao.service.impl.otc;

import com.biao.entity.otc.OtcOfflineOrder;
import com.biao.entity.otc.OtcOfflineOrderLog;
import com.biao.mapper.otc.OtcOfflineOrderDao;
import com.biao.mapper.otc.OtcOfflineOrderLogDao;
import com.biao.service.otc.OtcOfflineOrderLogService;
import com.biao.util.SnowFlake;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OtcOfflineOrderLogServiceImpl implements OtcOfflineOrderLogService {

    @Autowired
    private OtcOfflineOrderLogDao otcOfflineOrderLogDao;

    @Autowired
    private OtcOfflineOrderDao otcOfflineOrderDao;

    @Transactional
    public void saveLog(OtcOfflineOrder otcOfflineOrder, String batchNo) {
        OtcOfflineOrderLog log = new OtcOfflineOrderLog();
        BeanUtils.copyProperties(otcOfflineOrder, log);
        log.setOrderId(otcOfflineOrder.getId());// 广告ID
        log.setId(SnowFlake.createSnowFlake().nextIdString());
        log.setBatchNo(batchNo);
        log.setCreateDate(LocalDateTime.now());
        otcOfflineOrderLogDao.insert(log);
    }

    @Override
    @Transactional
    public void saveLog(String orderId, String batchNo) {
        OtcOfflineOrder otcOfflineOrder = otcOfflineOrderDao.findById(orderId);
        saveLog(otcOfflineOrder, batchNo);
    }
}
