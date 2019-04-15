package com.biao.previous.message;

import com.biao.constant.TradeConstant;
import com.biao.entity.Order;
import com.biao.enums.OrderEnum;
import com.biao.enums.TradeEnum;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.UserOrderDTO;
import com.biao.previous.domain.TrType;
import com.biao.previous.domain.TradeResult;
import com.biao.service.OrderService;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 关于order的处理；
 *
 *
 */
public class OrderConsumer implements TrExecutorSubscriber<TradeResult> {
    /**
     * 操作对象；
     */
    private OrderService orderService;

    /**
     * 发送一个kafka消息；
     */
    private KafkaTemplate kafkaTemplate;


    public OrderConsumer(OrderService orderService, KafkaTemplate kafkaTemplate) {
        this.orderService = orderService;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public Flux<String> executor(Collection<? extends TradeResult> collections) {
        //修改订单；
        //修改买入方的订单信息；
        List<Order> elementsByCollection = new ArrayList<>();
        Map<String, List<TradeResult>> buyResult = collections.stream()
                .collect(Collectors.groupingBy(TradeResult::getBuyOrderNo, Collectors.toList()));
        buyResult.forEach((k, v) -> {
            double income = v.stream().mapToDouble(e -> e.getComputerResult().getBuyIncome().doubleValue()).sum();
            double spent = v.stream().mapToDouble(e -> e.getComputerResult().getBuySpent().doubleValue()).sum();
            double fee = v.stream().mapToDouble(e -> e.getComputerResult().getBuyFee().doubleValue()).sum();
            TradeResult tradeResult = v.stream().max(Comparator.comparing(TradeResult::getIndex)).get();
            int status = tradeResult.getBuyOrderStatus().getCode();
            Order order = new Order();
            order.setExFee(new BigDecimal(fee))
                    .setSuccessVolume(new BigDecimal(income + fee))
                    .setToCoinVolume(new BigDecimal(income))
                    .setSpentVolume(new BigDecimal(spent))
                    .setUserId(tradeResult.getBuyDto().getUserId())
                    .setExType(TradeEnum.BUY.ordinal())
                    .setCoinMain(tradeResult.getBuyDto().getCoinMain())
                    .setCoinOther(tradeResult.getBuyDto().getCoinOther())
                    .setStatus(status)
                    .setCancelLock(tradeResult.getTradeNo());
            order.setId(k);
            elementsByCollection.add(order);
        });
        //修改卖方的订单信息；
        Map<String, List<TradeResult>> sellResult = collections.stream().collect(Collectors.groupingBy(TradeResult::getSellOrderNo, Collectors.toList()));
        sellResult.forEach((k, v) -> {
            double income = v.stream().mapToDouble(e -> e.getComputerResult().getSellIncome().doubleValue()).sum();
            double spent = v.stream().mapToDouble(e -> e.getComputerResult().getSellSpent().doubleValue()).sum();
            double fee = v.stream().mapToDouble(e -> e.getComputerResult().getSellFee().doubleValue()).sum();
            TradeResult tradeResult = v.stream().max(Comparator.comparing(TradeResult::getIndex)).get();
            int status = tradeResult.getSellOrderStatus().getCode();
            Order order = new Order();
            order.setExFee(new BigDecimal(fee))
                    .setSuccessVolume(new BigDecimal(spent))
                    .setToCoinVolume(new BigDecimal(income))
                    .setSpentVolume(new BigDecimal(spent))
                    .setUserId(tradeResult.getSellDto().getUserId())
                    .setExType(TradeEnum.SELL.ordinal())
                    .setCoinMain(tradeResult.getSellDto().getCoinMain())
                    .setCoinOther(tradeResult.getSellDto().getCoinOther())
                    .setStatus(status)
                    .setCancelLock(tradeResult.getTradeNo());
            order.setId(k);
            elementsByCollection.add(order);
        });
        Flux.fromIterable(elementsByCollection).filter(e -> Objects.equals(e.getStatus()
                , OrderEnum.OrderStatus.PART_SUCCESS.getCode())).map(e -> {
            UserOrderDTO dto = new UserOrderDTO();
            dto.setUserId(e.getUserId());
            dto.setStatus(e.getStatus());
            dto.setExType(e.getExType());
            dto.setOrderNo(e.getId());
            dto.setCoinMain(e.getCoinMain());
            dto.setCoinOther(e.getCoinOther());
            dto.setSuccessVolume(e.getSuccessVolume());
            return dto;
        }).subscribe((e) -> {
            String key = e.getCoinMain() + "_" + e.getCoinOther();
            kafkaTemplate.send(TradeConstant.TRADE_RESULT_ORDER_TOPIC, key, SampleMessage.build(e));
        });
        return Flux.fromIterable(elementsByCollection)
                .map(e -> {
                    GlobalMessageResponseVo message = orderService.updateResultOrders(e);
                           Order data = (Order) message.getData();
                            StringBuilder builder = new StringBuilder("ORDER_RESULT订单结果处理:");
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

    @Override
    public TrType getType() {
        return TrType.TRADE;
    }
}
