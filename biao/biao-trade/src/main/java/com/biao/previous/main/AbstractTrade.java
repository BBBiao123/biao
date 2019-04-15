package com.biao.previous.main;

import com.biao.constant.TradeConstant;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.pojo.KafkaPlatOrderDTO;
import com.biao.pojo.RemovePlatOrderDTO;
import com.biao.pojo.TradeDto;
import com.biao.pojo.TradeUserFeeNotify;
import com.biao.previous.cache.UserFeeCache;
import com.biao.previous.queue.QueueDistribute;
import com.biao.previous.queue.QueueType;
import com.biao.redis.RedisCacheManager;
import com.biao.util.TradeCompute;
import com.biao.vo.redis.RedisExPairVO;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 *
 */
@SuppressWarnings("all")
public abstract class AbstractTrade extends AbstractFilter {

    /**
     * 消息队列；
     */
    private QueueDistribute distribute;
    /**
     * 手续费；
     */
    private BigDecimal fee = new BigDecimal(0.001);
    /**
     * redis换存的管理实现；
     */
    private RedisCacheManager redisCacheManager;

    /**
     * 初始化；
     *
     * @param distribute        provider
     * @param redisCacheManager redisCacheManager
     */

    AbstractTrade(QueueDistribute distribute, RedisCacheManager redisCacheManager) {
        this.distribute = distribute;
        this.redisCacheManager = redisCacheManager;
    }

    /**
     * 发送交易数据；
     *
     * @param value 数据；
     */
    void sendQueue(QueueType queueType, Object value) {
        distribute.send(queueType, value);
    }

    /**
     * 获取当前的手续费；
     * 这里可能会有并发性问题,但是问题不大...
     */
    public BigDecimal getFree(String userId, String coinMain, String coinOther) {
        //从缓存用户绑定缓存中获取当前的用户有没有对单个用户设置手续费.
        TradeUserFeeNotify notify = UserFeeCache.get(userId, coinMain, coinOther);
        if (notify == null) {
            RedisExPairVO redisExPairVO = redisCacheManager.acquireExPair(coinMain, coinOther);
            if (redisExPairVO != null && StringUtils.isNotBlank(redisExPairVO.getFree())) {
                String free = redisExPairVO.getFree();
                return new BigDecimal(free);
            } else {
                return fee;
            }
        } else {
            return notify.getFee();
        }
    }

    /**
     * 以买出消耗为准；
     *
     * @param buyData  买入数据；
     * @param sellData 卖出数据；
     * @return 计算结果；
     */
    ComputerResult compute(TradeDto buyData, TradeDto sellData, BigDecimal price) {
        /*
         *第一步计算买入得到金额
         */
        //
        int i = buyData.getComputerVolume().compareTo(sellData.getComputerVolume());
        BigDecimal tempBuyIncome;
        //大于
        if (i > 0) {
            tempBuyIncome = sellData.getComputerVolume();
        } else {//小于等；
            tempBuyIncome = buyData.getComputerVolume();
        }
        BigDecimal tempSellSpent = tempBuyIncome;

        /*
         *第二步计算花费了多少USDT;实际花费的数量
         */
        BigDecimal tempBuySpent = TradeCompute.multiply(tempBuyIncome, price);
        //本来需要花费的数量;
        BigDecimal stempBuySpen = TradeCompute.multiply(buyData.getPrice(), tempSellSpent);

        BigDecimal tempSellIncome = tempBuySpent;
        /*
         * 第三步计算余额多少；
         */
        BigDecimal tempBuyBalance = TradeCompute.subtract(buyData.getTradeVolume(), stempBuySpen);
        BigDecimal tempSellBalance = TradeCompute.subtract(sellData.getTradeVolume(), tempSellSpent);
        //需要退的数量;
        BigDecimal buyRefund = TradeCompute.subtract(stempBuySpen, tempBuySpent);
        BigDecimal buyLock = stempBuySpen;
        BigDecimal sellLock = tempSellSpent;
        BigDecimal sellRefund = BigDecimal.ZERO;
        //买入方的手续费记录
        BigDecimal buyFeeReal = getFree(buyData.getUserId(), buyData.getCoinMain(),
                buyData.getCoinOther());
        BigDecimal buyFee = TradeCompute.multiply(tempBuyIncome, buyFeeReal);
        /*
         *卖出方的手续费记录.
         */
        BigDecimal sellFeeReal = getFree(sellData.getUserId(), buyData.getCoinMain(),
                buyData.getCoinOther());
        BigDecimal sellFee = TradeCompute.multiply(tempSellIncome, sellFeeReal);
        ComputerResult result = new ComputerResult();
        result.setBuyLock(buyLock);
        result.setSellLock(sellLock);
        result.setBuyRefund(buyRefund);
        result.setSellRefund(sellRefund);
        //余额；
        result.setBuyBalance(tempBuyBalance);
        //余额
        result.setSellBalance(tempSellBalance);
        //得到；
        result.setBuyIncome(TradeCompute.subtract(tempBuyIncome, buyFee));
        result.setBuyFee(buyFee);
        //buy的得到就是sell的花费；
        result.setSellIncome(TradeCompute.subtract(tempSellIncome, sellFee));
        result.setSellFee(sellFee);
        //花费
        result.setBuySpent(tempBuySpent);
        //buy的花费就是sell的得到;
        result.setSellSpent(tempBuyIncome);
        result.setTradeVolume(tempBuyIncome);
        //计算一下手续费用哦；
        //判断是不是全部完成；
        boolean f = TradeCompute.divide(tempBuyBalance, price, 9).compareTo(new BigDecimal(0.00000001)) < 0;
        result.setBuySuccess(TradeCompute.subtract(buyData.getComputerVolume(), tempBuyIncome).doubleValue() <= 0
                || tempBuyBalance.doubleValue() <= 0 || f);
        result.setSellSuccess(tempSellBalance.doubleValue() <= 0);
        return result;
    }


    /**
     * 非归零操作；
     *
     * @param tradeDto 对象；
     */
    void noneMakeZero(TradeDto tradeDto, BigDecimal tradeVolume, BigDecimal compVolume) {
        tradeDto.setTradeVolume(tradeVolume);
        tradeDto.setComputerVolume(compVolume);
        redisTemplate.opsForHash()
                .put(TradeConstant.TRADE_PREPOSITION_KEY, tradeDto.getOrderNo(), tradeDto);
        //修改内存数据
        String key = asKey(tradeDto);
        TradeCache.INST.update(key, tradeDto);
        //发送远程消息给傻叼;
        kafkaTemplate.send(TradeConstant.TRADE_KAFKA_ORDER_TOPIC,
                tradeDto.ackKey(),
                SampleMessage
                        .build(KafkaPlatOrderDTO.buildPut(tradeDto)));
    }

    /**
     * 归零；
     *
     * @param tradeDto 数据处理；
     */
    protected boolean makeZero(TradeDto tradeDto) {
        try {
            String buyKey = tradeDto.getType().redisKey(tradeDto.getCoinMain(), tradeDto.getCoinOther());
            //删除排序列队的数据；
            redisTemplate.opsForZSet().remove(
                    buyKey,
                    tradeDto.getOrderNo());
            //删除数据存储队列
            redisTemplate.opsForHash()
                    .delete(TradeConstant.TRADE_PREPOSITION_KEY, tradeDto.getOrderNo());
            TradeCache.INST.remove(asKey(tradeDto));
            //发送远程消息给傻叼;
            RemovePlatOrderDTO dto = new RemovePlatOrderDTO();
            dto.setCoinMain(tradeDto.getCoinMain());
            dto.setCoinOther(tradeDto.getCoinOther());
            dto.setType(tradeDto.getType());
            dto.setOrderNo(tradeDto.getOrderNo());
            kafkaTemplate.send(TradeConstant.TRADE_KAFKA_ORDER_TOPIC, tradeDto.ackKey(),
                    SampleMessage.build(KafkaPlatOrderDTO.buildRemove(dto)));
            return  true;
        }catch (Exception ex){
            return false;
        }
    }

    /**
     *  还原数据.
     * @param dto dto.
     * @return
     */
    protected void reduction(TradeDto tradeDto) {
        //删除数据存储队列
        redisTemplate.opsForHash()
                .put(TradeConstant.TRADE_PREPOSITION_KEY, tradeDto.getOrderNo(),tradeDto);
    }

    /**
     * 返回一个计算结果的对象；
     */
    public static class ComputerResult {
        /**
         * 买入完成；
         */
        private Boolean buySuccess;
        /**
         * 买出完成！
         */
        private Boolean sellSuccess;
        /**
         * 买入者余额
         */
        private BigDecimal buyBalance;
        /**
         * 卖出者的余额
         */
        private BigDecimal sellBalance;
        /**
         * 买入者花费；
         */
        private BigDecimal buySpent;

        /**
         * 卖出者的花费
         */
        private BigDecimal sellSpent;
        /**
         * 买入者得到；
         */
        private BigDecimal buyIncome;
        /**
         * 卖出者得到；
         */
        private BigDecimal sellIncome;
        /**
         * 买出的手续费；
         */
        private BigDecimal buyFee;
        /**
         * 买入的手续费；
         */
        private BigDecimal sellFee;
        /**
         * 处理数量；
         */
        private BigDecimal tradeVolume;
        /**
         * 退回金额;
         */
        private BigDecimal buyRefund;
        /**
         * 卖入退回
         */
        private BigDecimal sellRefund;
        /**
         * 买入锁定消耗
         */
        private BigDecimal buyLock;
        /**
         * 卖出锁定消耗
         */
        private BigDecimal sellLock;

        public BigDecimal getBuyLock() {
            return buyLock;
        }

        public void setBuyLock(BigDecimal buyLock) {
            this.buyLock = buyLock;
        }

        public BigDecimal getSellLock() {
            return sellLock;
        }

        public void setSellLock(BigDecimal sellLock) {
            this.sellLock = sellLock;
        }

        public BigDecimal getBuyRefund() {
            return buyRefund;
        }

        public void setBuyRefund(BigDecimal buyRefund) {
            this.buyRefund = buyRefund;
        }

        public BigDecimal getSellRefund() {
            return sellRefund;
        }

        public void setSellRefund(BigDecimal sellRefund) {
            this.sellRefund = sellRefund;
        }

        public BigDecimal getTradeVolume() {
            return tradeVolume;
        }

        public void setTradeVolume(BigDecimal tradeVolume) {
            this.tradeVolume = tradeVolume;
        }

        public Boolean getBuySuccess() {
            return buySuccess;
        }

        public void setBuySuccess(Boolean buySuccess) {
            this.buySuccess = buySuccess;
        }

        public Boolean getSellSuccess() {
            return sellSuccess;
        }

        public void setSellSuccess(Boolean sellSuccess) {
            this.sellSuccess = sellSuccess;
        }

        public BigDecimal getBuyFee() {
            return buyFee;
        }

        public void setBuyFee(BigDecimal buyFee) {
            this.buyFee = buyFee;
        }

        public BigDecimal getSellFee() {
            return sellFee;
        }

        public void setSellFee(BigDecimal sellFee) {
            this.sellFee = sellFee;
        }

        public BigDecimal getBuyBalance() {
            return buyBalance;
        }

        public void setBuyBalance(BigDecimal buyBalance) {
            this.buyBalance = buyBalance;
        }

        public BigDecimal getSellBalance() {
            return sellBalance;
        }

        public void setSellBalance(BigDecimal sellBalance) {
            this.sellBalance = sellBalance;
        }

        public BigDecimal getBuySpent() {
            return buySpent;
        }

        public void setBuySpent(BigDecimal buySpent) {
            this.buySpent = buySpent;
        }

        public BigDecimal getSellSpent() {
            return sellSpent;
        }

        public void setSellSpent(BigDecimal sellSpent) {
            this.sellSpent = sellSpent;
        }

        public BigDecimal getBuyIncome() {
            return buyIncome;
        }

        public void setBuyIncome(BigDecimal buyIncome) {
            this.buyIncome = buyIncome;
        }

        public BigDecimal getSellIncome() {
            return sellIncome;
        }

        public void setSellIncome(BigDecimal sellIncome) {
            this.sellIncome = sellIncome;
        }

        @Override
        public String toString() {
            return "ComputerResult{" +
                    "buySuccess=" + buySuccess +
                    ", sellSuccess=" + sellSuccess +
                    ", buyBalance=" + buyBalance +
                    ", sellBalance=" + sellBalance +
                    ", buySpent=" + buySpent +
                    ", sellSpent=" + sellSpent +
                    ", buyIncome=" + buyIncome +
                    ", sellIncome=" + sellIncome +
                    ", buyFee=" + buyFee +
                    ", sellFee=" + sellFee +
                    ", tradeVolume=" + tradeVolume +
                    '}';
        }
    }

    /**
     * 用户资产消耗过程;
     */
    public static class UserVolume {

    }
}
