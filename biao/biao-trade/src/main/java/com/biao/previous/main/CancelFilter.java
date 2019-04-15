package com.biao.previous.main;

import com.biao.entity.Order;
import com.biao.enums.OrderEnum;
import com.biao.pojo.TradeDto;
import com.biao.previous.Tools;
import com.biao.previous.domain.CancelResult;
import com.biao.previous.domain.ProcessData;
import com.biao.previous.queue.QueueDistribute;
import com.biao.previous.queue.QueueType;
import com.biao.redis.RedisCacheManager;
import com.biao.service.OrderService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * CancelFilter.
 * <p>
 *     取消资产的相关处理.
 * <p>
 * 18-12-29下午3:50
 *
 *  "" sixh
 */
public class CancelFilter extends AbstractTrade{

    private Logger logger = LoggerFactory.getLogger(CancelFilter.class);

    private OrderService service;
    /**
     * 初始化；
     *
     * @param distribute        provider
     * @param redisCacheManager redisCacheManager
     */
   public CancelFilter(OrderService orderService, QueueDistribute distribute, RedisCacheManager redisCacheManager) {
        super(distribute, redisCacheManager);
        this.service = orderService;
    }

    /**
     * 相关取消处理.
     * @param data        数据处理；
     * @param processData 过程中存在的数据;
     * @param chain       处理链;
     */
    @Override
    public void doFilter0(TradeDto data, ProcessData processData, DataChain chain) {
        String orderNo = data.getOrderNo();
        String userId = data.getUserId();
        try {
            processData.setOrderComplete(true);
            Supplier<TradeDto> optional = Tools.getPre(orderNo, redisTemplate);
            if (optional.get() == null) {
                logger.error("根据订单号Redis没有找到可撤单订单数据信息{}", orderNo);
                return;
            }
            TradeDto dto = optional.get();
            String biUserId = dto.getUserId();
            //判断是当前的用户.
            if(!Objects.equals(biUserId,userId)){
                logger.error("不是相同的用户进行撤消订单{}", orderNo);
                return;
            }
            //查询订单信息；
            Order order = service.findById(orderNo);
            if(order == null) {
                logger.error("根据订单号DB没有找到可撤单订单数据信息{}", orderNo);
                return;
            }
            //判断一下订单的状态；
            OrderEnum.OrderStatus status = OrderEnum.OrderStatus.valueOf(order.getStatus());
            if (Objects.equals(status, OrderEnum.OrderStatus.NOT_SUCCESS) ||
                    Objects.equals(status, OrderEnum.OrderStatus.PART_SUCCESS)) {
                //判断是否与数据库的数据一致。表示是否所有数据已经落库完成。
                boolean nextFlag = false;
                if (Objects.equals(status, OrderEnum.OrderStatus.NOT_SUCCESS)) {
                    if (StringUtils.isNotBlank(dto.getCancelLock()) || StringUtils.isNotBlank(order.getCancelLock())) {
                        nextFlag = true;
                    }
                } else {
                    nextFlag = !Objects.equals(dto.getCancelLock(), order.getCancelLock());
                }
                if (nextFlag) {
                    logger.error("{}数据处理中，暂不能撤单，等待落库!", orderNo);
                    return;
                }
                data = dto;
                //计算加回资产
                BigDecimal blockVolume = Optional.ofNullable(dto.getTradeVolume()).map(e -> {
                    if (e.doubleValue() < 0) {
                        return new BigDecimal(0);
                    } else {
                        return e;
                    }
                }).get();
                //干掉redis前置处理；
                boolean b = makeZero(data);
                if(!b) {
                    reduction(data);
                    processData.setOrderComplete(false);
                    return;
                }
                //发送数据库处理.
                CancelResult result = new CancelResult();
                result.setOrderNo(orderNo);
                result.setUserId(data.getUserId());
                result.setCoinSymbol(data.getTradeCoin());
                result.setStatus(status.cancelChangeStatus());
                result.setBlockVolume(blockVolume);
                List<CancelResult> list = Lists.newArrayList();
                list.add(result);
                sendQueue(QueueType.TR_RE_MESSAGE,list);
                //表示订单处理是成功的.否则要重新放到redis中.
            } else {
                logger.warn("订单已经不允许取消，已完成交易!{}",orderNo);
            }
        } catch (Exception ex) {
            logger.error("取消订单失败:{}", orderNo,ex);
            processData.setOrderComplete(true);
        }
    }
}
