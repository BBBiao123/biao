package com.biao.service.impl;

import com.biao.entity.otc.OtcOfflineAppeal;
import com.biao.entity.otc.OtcOfflineOrderDetail;
import com.biao.enums.OfflineOrderDetailStatusEnum;
import com.biao.mapper.otc.OtcOfflineAppealDao;
import com.biao.mapper.otc.OtcOfflineOrderDetailDao;
import com.biao.service.OtcDetailCancelTaskService;
import com.biao.service.otc.OtcOfflineOrderDetailService;
import com.biao.vo.otc.OtcOfflineOrderDetailVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OtcDetailCancelTaskServiceImpl implements OtcDetailCancelTaskService {

    private Logger logger = LoggerFactory.getLogger(OfflineOrderDetailCancelTaskServiceImpl.class);

    @Autowired
    private OtcOfflineOrderDetailDao otcOfflineOrderDetailDao;

    @Autowired
    private OtcOfflineOrderDetailService otcOfflineOrderDetailService;

    @Autowired
    private OtcDetailCancelTaskService otcDetailCancelTaskService;

    @Autowired
    private OtcOfflineAppealDao otcOfflineAppealDao;

    @Override
    public void doCancelOrderDetail() {
        LocalDateTime curDateTime = LocalDateTime.now().minusMinutes(30);
        List<OtcOfflineOrderDetail> orderDetails = otcOfflineOrderDetailDao.findLongTimeOrderDetail(OfflineOrderDetailStatusEnum.NOMAL.getCode(), curDateTime);
        if (CollectionUtils.isEmpty(orderDetails)) {
            return;
        }
        for (OtcOfflineOrderDetail orderDetail : orderDetails) {
            // 存在申诉，不取消
            List<OtcOfflineAppeal> appealList = otcOfflineAppealDao.findBySubOrderIdAndStatus(orderDetail.getSubOrderId(), "1");
            if (CollectionUtils.isNotEmpty(appealList)) {
                continue;
            }
            // 取消订单
            try {
                logger.info("task执行自动取消OTC订单:{}", orderDetail.getSubOrderId());
                otcDetailCancelTaskService.cancelOrderDetail(orderDetail);
            } catch (Exception e) {
                logger.error("task执行自动取消订单异常[" + orderDetail.getSubOrderId() + "]", e);
            }

        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cancelOrderDetail(OtcOfflineOrderDetail otcOfflineOrderDetail) {
        OtcOfflineOrderDetailVO offlineConfirmVO = new OtcOfflineOrderDetailVO();
        offlineConfirmVO.setLoginSource("otc");
        offlineConfirmVO.setUserId(otcOfflineOrderDetail.getUserId());
        offlineConfirmVO.setOrderId(otcOfflineOrderDetail.getOrderId());
        offlineConfirmVO.setSubOrderId(otcOfflineOrderDetail.getSubOrderId());
        offlineConfirmVO.setUpdateBy("task");
        otcOfflineOrderDetailService.cancelOrderDetail(offlineConfirmVO);
    }
}
