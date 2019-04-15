package com.biao.service.impl.otc;

import com.biao.entity.otc.OtcOfflineOrderDetail;
import com.biao.entity.otc.OtcOfflineOrderDetailLog;
import com.biao.mapper.otc.OtcOfflineOrderDetailDao;
import com.biao.mapper.otc.OtcOfflineOrderDetailLogDao;
import com.biao.service.otc.OtcOfflineOrderDetailLogService;
import com.biao.util.SnowFlake;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OtcOfflineOrderDetailLogServiceImpl implements OtcOfflineOrderDetailLogService {

    @Autowired
    private OtcOfflineOrderDetailLogDao otcOfflineOrderDetailLogDao;


    @Autowired
    private OtcOfflineOrderDetailDao otcOfflineOrderDetailDao;

    @Transactional
    public void saveLog(OtcOfflineOrderDetail otcOfflineOrderDetail, String batchNo) {
        OtcOfflineOrderDetailLog log = new OtcOfflineOrderDetailLog();
        BeanUtils.copyProperties(otcOfflineOrderDetail, log);
        log.setId(SnowFlake.createSnowFlake().nextIdString());
        log.setBatchNo(batchNo);
        log.setCreateDate(LocalDateTime.now());
        otcOfflineOrderDetailLogDao.insert(log);
    }

    @Override
    @Transactional
    public void saveLog(String userId, String subOrderId, String loginSource, String batchNo) {
        OtcOfflineOrderDetail otcOfflineOrderDetail = otcOfflineOrderDetailDao.findByUserIdSubOrderId(userId, subOrderId, loginSource);
        saveLog(otcOfflineOrderDetail, batchNo);
    }

    public void saveLog(String subOrderId, String batchNo) {
        List<OtcOfflineOrderDetail> otcOfflineOrderDetails = otcOfflineOrderDetailDao.findBySubOrderId(subOrderId);
        for (OtcOfflineOrderDetail otcOfflineOrderDetail : otcOfflineOrderDetails) {
            saveLog(otcOfflineOrderDetail, batchNo);
        }
    }
}
