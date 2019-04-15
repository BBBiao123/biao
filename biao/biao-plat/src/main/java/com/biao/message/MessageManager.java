package com.biao.message;

import com.biao.constant.TradeConstant;
import com.biao.entity.OfflineOrderDetail;
import com.biao.entity.PlatUser;
import com.biao.kafka.interceptor.MessageDTO;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.reactive.data.mongo.domain.Message;
import com.biao.reactive.data.mongo.service.MessageService;
import com.biao.service.OfflineOrderDetailService;
import com.biao.service.PlatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The type Message manager.
 *
 *  ""(Myth)
 */
@Component
public class MessageManager {

    private final KafkaTemplate kafkaTemplate;

    private final PlatUserService platUserService;

    private final MessageService messageService;

    private final OfflineOrderDetailService offlineOrderDetailService;

    /**
     * Instantiates a new Message manager.
     *
     * @param kafkaTemplate             the kafka template
     * @param platUserService           the plat user service
     * @param messageService            the message service
     * @param offlineOrderDetailService the offline order detail service
     */
    @Autowired
    public MessageManager(final KafkaTemplate kafkaTemplate,
                          final PlatUserService platUserService,
                          final MessageService messageService,
                          final OfflineOrderDetailService offlineOrderDetailService) {
        this.kafkaTemplate = kafkaTemplate;
        this.platUserService = platUserService;
        this.messageService = messageService;
        this.offlineOrderDetailService = offlineOrderDetailService;
    }

    /**
     * Save and push message.
     *
     * @param userId      the user id
     * @param orderId     the order id
     * @param orderStatus the order status
     */
    public void saveAndPushMessage(final String userId, final String orderId, final Integer orderStatus) {
        final PlatUser platUser = platUserService.findById(userId);
        final Integer receiveMsg = platUser.getReceiveMsg();
        if (Objects.isNull(receiveMsg) || receiveMsg == 0) {
            final OfflineOrderDetail offlineOrderDetail = offlineOrderDetailService.findByUserIdAndOrderId(userId, orderId);
            Message message = new Message();
            message.setCoinMain(offlineOrderDetail.getSymbol());
            message.setToUserId(offlineOrderDetail.getAskUserId());
            message.setFromUserId(userId);
            message.setCoinOther(offlineOrderDetail.getSymbol());
            message.setOrderId(orderId);
            message.setCreateTime(LocalDateTime.now());
            message.setOrderStatus(orderStatus);
            message.setType(0);
            message.setPrice(offlineOrderDetail.getPrice());
            message.setVolume(offlineOrderDetail.getVolume());
            messageService.insert(message);

            //推送到websocket
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setId(message.getId());
            messageDTO.setCreateTime(System.currentTimeMillis());
            messageDTO.setUserId(offlineOrderDetail.getAskUserId());
            messageDTO.setOrderId(orderId);
            messageDTO.setType(0);
            messageDTO.setCoinOther(offlineOrderDetail.getSymbol());
            messageDTO.setStatus(orderStatus);
            messageDTO.setVolume(offlineOrderDetail.getVolume());
            messageDTO.setPrice(offlineOrderDetail.getPrice());
            kafkaTemplate.send(TradeConstant.MESSAGE, SampleMessage.build(messageDTO));

        }
    }

}
