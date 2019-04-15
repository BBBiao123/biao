package com.biao.service.impl.push;

import com.biao.constant.TradeConstant;
import com.biao.entity.OfflineOrderDetail;
import com.biao.enums.OfflineOrderDetailStatusEnum;
import com.biao.kafka.interceptor.ImParamDTO;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.mapper.OfflineOrderDetailDao;
import com.biao.service.ImOrderService;
import com.biao.service.push.OfflineOrderPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * The type Offline order push service.
 *
 *  ""(Myth)
 */
@Service("offlineOrderPushService")
public class OfflineOrderPushServiceImpl implements OfflineOrderPushService {

    private final OfflineOrderDetailDao offlineOrderDetailDao;

    @Value("${remainingTime:30}")
    private Integer remainingTime;

    private final KafkaTemplate kafkaTemplate;

    private final ImOrderService imOrderService;

    @Autowired(required = false)
    public OfflineOrderPushServiceImpl(final OfflineOrderDetailDao offlineOrderDetailDao,
                                       final KafkaTemplate kafkaTemplate,
                                       final ImOrderService imOrderService) {
        this.offlineOrderDetailDao = offlineOrderDetailDao;
        this.kafkaTemplate = kafkaTemplate;
        this.imOrderService = imOrderService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void pushData(final String userId, final String subOrderId, final Integer status) {
        final OfflineOrderDetail offlineOrderDetail = pushWebsocket(userId, subOrderId, status);
        ImParamDTO dto = new ImParamDTO();
        dto.setFromUserId(userId);
        dto.setToUserId(offlineOrderDetail.getUserId());
        dto.setOrderId(subOrderId);
        dto.setOrderDetailStatusEnum(OfflineOrderDetailStatusEnum.acquiredByCode(status.toString()));
        dto.setTwoSides(Boolean.FALSE);
        //发送消息到im
        imOrderService.sendOfflineOrder(dto);
    }

    @Override
    public OfflineOrderDetail pushWebsocket(final String userId, final String subOrderId, final Integer status) {
        OfflineOrderDetail orderDetail = offlineOrderDetailDao.findByUserIdAndOrderId(userId, subOrderId);
        orderDetail.setSubOrderId(subOrderId);
        orderDetail.setStatus(status);
        orderDetail.setUserId(orderDetail.getAskUserId());
        orderDetail.setAskUserId(userId);
        orderDetail.setAskUserMobile(orderDetail.getUserMobile());
        //设置取消时间
        if (Integer.parseInt(OfflineOrderDetailStatusEnum.NOMAL.getCode()) == status) {
            final LocalDateTime endTime = orderDetail.getCreateDate().plusMinutes(remainingTime);
            final LocalDateTime now = LocalDateTime.now();
            if (now.isEqual(endTime) || now.isAfter(endTime)) {
                orderDetail.setRemainingTime(0);
            } else {
                final int until = (int) now.until(endTime, ChronoUnit.SECONDS);
                orderDetail.setRemainingTime(until);
            }
        }
        if (Objects.equals(orderDetail.getRemarks(), "buy")) {
            orderDetail.setRemarks("sell");
        } else {
            orderDetail.setRemarks("buy");
        }
        orderDetail.setCreateDate(null);
        orderDetail.setUpdateDate(null);
        kafkaTemplate.send(TradeConstant.C2C_USER_ORDER, SampleMessage.build(orderDetail));
        return orderDetail;
    }
}
