package com.biao.previous.message;

import com.biao.entity.Order;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.previous.domain.CancelResult;
import com.biao.previous.domain.TrType;
import com.biao.service.OrderService;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

/**
 * CancelOrderConsumer.
 * <p>
 * <p>
 * 18-12-29下午5:02
 *
 *  "" sixh
 */
public class CancelOrderConsumer implements TrExecutorSubscriber<CancelResult>{

    /**
     * 操作对象；
     */
    private OrderService orderService;

    @Override
    public Flux<String> executor(Collection<? extends CancelResult> collections) {
        Optional<? extends CancelResult> first = collections.stream().findFirst();
        if(first.isPresent()){
            CancelResult cancelResult = first.get();
            Order order = new Order();
            order.setExFee(BigDecimal.ZERO);
            order.setId(cancelResult.getOrderNo());
            order.setUserId(cancelResult.getUserId());
            order.setSpentVolume(BigDecimal.ZERO);
            order.setToCoinVolume(BigDecimal.ZERO);
            order.setSuccessVolume(BigDecimal.ZERO);
            order.setStatus(cancelResult.getStatus().getCode());
            return Flux.just(order)
                    .map(e -> {
                        GlobalMessageResponseVo message = orderService.updateResultOrders(e);
                        Order data = (Order) message.getData();
                        StringBuilder builder = new StringBuilder("[取消-->]订单结果处理:");
                        builder.append("订单号：").append(message.getMsg())
                                .append("successVolume:").append(e.getSuccessVolume())
                                .append("exfree:").append(e.getExFee())
                                .append("toCoinVolume:").append(e.getToCoinVolume())
                                .append("spentVolume:").append(e.getSpentVolume())
                                .append("tradeNo:").append(e.getCancelLock())
                                .append("status:").append(e.getStatus())
                                .append("<-------前[变化]后-------->")
                                .append("successVolume:").append(data.getSuccessVolume())
                                .append("exfree:").append(data.getExFee())
                                .append("toCoinVolume:").append(data.getToCoinVolume())
                                .append("spentVolume:").append(data.getSpentVolume())
                                .append("tradeNo:").append(data.getCancelLock())
                                .append("status:").append(data.getStatus())
                                .append("处理结果：").append(message.getCode() >= 0);
                        return builder.toString();
                    });

        }
        return Flux.just("发送过来的消息没有找到取消订单信息..不影响订单状态.......");
    }

    @Override
    public TrType getType() {
        return TrType.CANCEL;
    }

    /**
     * 发送一个kafka消息；
     */
    private KafkaTemplate kafkaTemplate;


    public CancelOrderConsumer(OrderService orderService, KafkaTemplate kafkaTemplate) {
        this.orderService = orderService;
        this.kafkaTemplate = kafkaTemplate;
    }

}
