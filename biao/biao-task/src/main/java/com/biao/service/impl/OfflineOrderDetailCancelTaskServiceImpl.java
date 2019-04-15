package com.biao.service.impl;

import com.biao.entity.OfflineAppeal;
import com.biao.entity.OfflineOrderDetail;
import com.biao.entity.PlatUser;
import com.biao.enums.OfflineOrderDetailStatusEnum;
import com.biao.mapper.OfflineAppealDao;
import com.biao.mapper.OfflineOrderDetailDao;
import com.biao.mapper.PlatUserDao;
import com.biao.service.OfflineOrderDetailCancelTaskService;
import com.biao.service.OfflineOrderDetailService;
import com.biao.vo.OfflineConfirmVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OfflineOrderDetailCancelTaskServiceImpl implements OfflineOrderDetailCancelTaskService {

    private Logger logger = LoggerFactory.getLogger(OfflineOrderDetailCancelTaskServiceImpl.class);

    @Autowired
    private OfflineOrderDetailService offlineOrderDetailService;

    @Autowired
    private OfflineOrderDetailDao offlineOrderDetailDao;

    @Autowired
    private OfflineOrderDetailCancelTaskService offlineOrderDetailCancelTaskService;

    @Autowired
    private OfflineAppealDao offlineAppealDao;

    @Autowired
    private PlatUserDao platUserDao;

    @Override
    public void doCancelOrderDetail() {

        Map<String, String> doneCancelSuborderIdMap = new HashMap<String, String>();
        LocalDateTime curDateTime = LocalDateTime.now().minusMinutes(30);
        List<OfflineOrderDetail> orderDetails = offlineOrderDetailDao.findLongTimeOrderDetail(OfflineOrderDetailStatusEnum.NOMAL.getCode(), curDateTime);
        if (CollectionUtils.isEmpty(orderDetails)) {
            return;
        }
        for (OfflineOrderDetail orderDetail : orderDetails) {
            // 存在申诉，不取消
            List<OfflineAppeal> appealList = offlineAppealDao.findBySubOrderIdAndStatus(orderDetail.getSubOrderId(), "1");
            if (CollectionUtils.isNotEmpty(appealList)) {
                continue;
            }
            // 本次task已取消，不用重复取消
            if (doneCancelSuborderIdMap.containsKey(orderDetail.getSubOrderId())) {
                continue;
            }
            // 取消订单
            try {
                offlineOrderDetailCancelTaskService.cancelOrderDetail(orderDetail);
            } catch (Exception e) {
                logger.error("task执行自动取消订单异常[" + orderDetail.getSubOrderId() + "]", e);
            }
            // 记录本次取消订单ID
            doneCancelSuborderIdMap.put(orderDetail.getSubOrderId(), orderDetail.getSubOrderId());
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cancelOrderDetail(OfflineOrderDetail offlineOrderDetail) {
        OfflineConfirmVO offlineConfirmVO = new OfflineConfirmVO();
        offlineConfirmVO.setOrderId(offlineOrderDetail.getOrderId());
        offlineConfirmVO.setSubOrderId(offlineOrderDetail.getSubOrderId());
        offlineConfirmVO.setUserId(offlineOrderDetail.getUserId());
        offlineConfirmVO.setUpdateBy("task");
        PlatUser user = platUserDao.findById(offlineOrderDetail.getUserId());
        if (Objects.isNull(user)) {
            logger.error("task执行自动取消订单异常，用户不存在[订单号：" + offlineOrderDetail.getSubOrderId() + "] + 用户ID：" + offlineOrderDetail.getUserId());
        } else {
            offlineOrderDetailService.detailCancel(offlineConfirmVO, user.getTag());
        }
    }
}
