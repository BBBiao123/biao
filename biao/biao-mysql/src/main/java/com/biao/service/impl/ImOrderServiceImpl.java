package com.biao.service.impl;

import com.biao.constant.ImKafkaConstants;
import com.biao.enums.OfflineOrderDetailStatusEnum;
import com.biao.kafka.interceptor.ImKafkaDTO;
import com.biao.kafka.interceptor.ImParamDTO;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.service.ImOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * impl ImOrderService.
 *
 *  ""(Myth)
 */
@Service("imOrderService")
public class ImOrderServiceImpl implements ImOrderService {

    private static final String ORDER_PAY = "买家已付款，请卖家及时确认收款情况!";

    private static final String ORDER_CANCEL = "订单已取消，如有疑问请及时联系客服!";

    private static final String ORDER_COMPLATE = "卖家确认收款，订单完成!";

    private static final String COMPLAINTING = "订单正在申诉中!";

    private static final String COMPLAINT_BUY = "申诉已判决，买方胜，如有疑问请及时联系客服!";

    private static final String COMPLAINT_SELL = "申诉已判决，卖方胜，如有疑问请及时联系客服!";

    private static final String COUNTDOWN = "倒计时30m结束,订单已失效，如有疑问请及时联系客服!";

    private final KafkaTemplate kafkaTemplate;

    @Autowired
    public ImOrderServiceImpl(final KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @SuppressWarnings("all")
    public void sendOfflineOrder(ImParamDTO paramDTO) {
        ImKafkaDTO imKafkaDTO = new ImKafkaDTO();
        imKafkaDTO.setFrom(buildImUniqueId(paramDTO.getFromUserId(), paramDTO.getOrderId()));
        imKafkaDTO.setTo(buildImUniqueId(paramDTO.getToUserId(), paramDTO.getOrderId()));
        imKafkaDTO.setMessage(buildMessage(paramDTO.getOrderDetailStatusEnum()));
        imKafkaDTO.setTwoSides(paramDTO.getTwoSides());
        kafkaTemplate.send(ImKafkaConstants.IM_ORDER, SampleMessage.build(imKafkaDTO));
    }


    private String buildImUniqueId(final String userId, final String orderId) {
        return String.join("_", orderId, userId);
    }

    private String buildMessage(final OfflineOrderDetailStatusEnum orderStatusEnum) {
        switch (orderStatusEnum) {
            case CONFIRM_PAY:
                return ORDER_PAY;
            case CANCEL:
                return ORDER_CANCEL;
            case TASK_ORDER_CANCEL:
                return COUNTDOWN;
            case CONFIRM_IN:
                return ORDER_COMPLATE;
            case SHENSU:
                return COMPLAINTING;
            case SHENSU_OVER_BUY:
                return COMPLAINT_BUY;
            case SHENSU_OVER_SELL:
                return COMPLAINT_SELL;
            default:
                return "";

        }
    }
}
