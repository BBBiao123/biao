package com.biao.previous.message;

import com.biao.enums.OrderEnum;
import com.biao.enums.TradeEnum;
import com.biao.previous.domain.TrType;
import com.biao.previous.domain.TradeResult;
import com.biao.previous.main.AbstractTrade;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.TradeCompute;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 关于用户资产后的一些计算；
 *
 *
 */
public class UserCoinVolumeConsumer implements TrExecutorSubscriber<TradeResult> {

    /**
     * 修改用户资产信息；
     */
    private UserCoinVolumeExService userCoinVolumeService;

    public UserCoinVolumeConsumer(UserCoinVolumeExService userCoinVolumeService) {
        this.userCoinVolumeService = userCoinVolumeService;
    }

    @Override
    public TrType getType() {
        return TrType.TRADE;
    }

    /**
     * 修改服务资产信息；
     *
     * @param collections 数据；
     * @return List<String>
     */
    @Override
    public Flux<String> executor(Collection<? extends TradeResult> collections) {
        //根据用户分组计算；
        Map<String, List<TradeResult>> resultMap = collections.stream().collect(Collectors.groupingBy(e -> e.getBuyDto().getUserId(), Collectors.toList()));
        //扣减退款算法;
        List<CoinVolumeTemp> temps1 = new ArrayList<>();
        List<CoinVolumeTemp> temps2 = new ArrayList<>();
        //buy返回volume
        Map<String, BigDecimal> buyBalanceRestitution = new HashMap<>();
        //sell返还volume;
        Map<String, BigDecimal> sellBalanceRestitution = new HashMap<>();
        //计算买入用户的处理情况；
        resultMap.forEach((k, v) -> {
            BigDecimal income = BigDecimal.ZERO;
            BigDecimal balance = BigDecimal.ZERO;
            BigDecimal refundVolume = BigDecimal.ZERO;
            BigDecimal lockVolume = BigDecimal.ZERO;
            for (TradeResult tr : v) {
                AbstractTrade.ComputerResult cr = tr.getComputerResult();
                income = TradeCompute.add(income, cr.getBuyIncome());
                if (Objects.equals(tr.getBuyOrderStatus(), OrderEnum.OrderStatus.ALL_SUCCESS)) {
                    //当前的余额
                    balance = TradeCompute.add(balance, cr.getBuyBalance());
                }
                refundVolume = TradeCompute.add(refundVolume, cr.getBuyRefund());
                lockVolume = TradeCompute.add(lockVolume, cr.getBuyLock());
            }
            TradeResult ts = v.stream().max(Comparator.comparing(TradeResult::getIndex)).get();
            CoinVolumeTemp t = new CoinVolumeTemp();
            //减
            //花费的币种； usdt
            t.volume = TradeCompute.add(lockVolume, balance);//减掉；
            t.symbol = ts.getBuyDto().getTradeCoin();
            t.userId = k;
            t.status = ts.getBuyOrderStatus();
            t.type = TradeEnum.BUY;
            t.refundVolume = TradeCompute.add(refundVolume, balance);//退回;//加上;
            t.orderNo = ts.getBuyOrderNo();
            t.tradeNo = ts.getTradeNo();
            temps1.add(t);
//            buyBalanceRestitution.put(k, balance);//usdt
            //加
            t = new CoinVolumeTemp();
            //得到的币种； bcc
            t.symbol = ts.getBuyDto().getToTradeCoin();
            t.userId = k;
            t.volume = income;
            t.status = ts.getBuyOrderStatus();
            t.orderNo = ts.getBuyOrderNo();
            t.tradeNo = ts.getTradeNo();
            t.type = TradeEnum.BUY;
            temps2.add(t);
        });
        //计算卖用户的处理情况；
        resultMap = collections.stream().collect(Collectors.groupingBy(e -> e.getSellDto().getUserId(), Collectors.toList()));
        resultMap.forEach((k, v) -> {
            BigDecimal income = BigDecimal.ZERO;
            BigDecimal balance = BigDecimal.ZERO;
            BigDecimal refundVolume = BigDecimal.ZERO;
            BigDecimal lockVolume = BigDecimal.ZERO;
            for (TradeResult tr : v) {
                AbstractTrade.ComputerResult cr = tr.getComputerResult();
                income = TradeCompute.add(income, cr.getSellIncome());
                if (Objects.equals(tr.getSellOrderStatus(), OrderEnum.OrderStatus.ALL_SUCCESS)) {
                    balance = TradeCompute.add(balance, cr.getSellBalance());
                }
                refundVolume = TradeCompute.add(refundVolume, cr.getSellRefund());
                lockVolume = TradeCompute.add(lockVolume, cr.getSellLock());
            }
            TradeResult ts = v.stream().max(Comparator.comparing(TradeResult::getIndex)).get();
            CoinVolumeTemp t = new CoinVolumeTemp();
            //减
            //花费的币种；
            t.volume = TradeCompute.add(lockVolume, balance);//减掉；
            t.symbol = ts.getSellDto().getTradeCoin();
            t.userId = k;
            t.status = ts.getSellOrderStatus();
            t.refundVolume = TradeCompute.add(refundVolume, balance);//退回
            t.orderNo = ts.getSellOrderNo();
            t.tradeNo = ts.getTradeNo();
            t.type = TradeEnum.SELL;
            temps1.add(t);
//            sellBalanceRestitution.put(k,balance);//bcc
            //加
            //得到的币种；
            t = new CoinVolumeTemp();//usdt
            t.symbol = ts.getSellDto().getToTradeCoin();
            t.userId = k;
            t.volume = income;
            t.status = ts.getSellOrderStatus();
            t.type = TradeEnum.SELL;
            t.orderNo = ts.getSellOrderNo();
            t.tradeNo = ts.getTradeNo();
            temps2.add(t);
        });
        return Flux.fromIterable(temps1).flatMap(e -> {
            long l = userCoinVolumeService.updateSpent(e.status, e.volume, e.refundVolume, e.userId, e.symbol);
            StringBuilder builder = new StringBuilder("USER_VOLUME:已花费信息(lock_volume)：");
            builder.append("用户:").append(e.userId)
                    .append("币种:").append(e.symbol)
                    .append("扣减lock数量:").append(e.volume)
                    .append("退扣数量:").append(e.refundVolume)
                    .append("订单号：").append(e.orderNo)
                    .append("交易号：").append(e.tradeNo)
                    .append("订单状态：").append(e.status)
                    .append("处理结果:").append(l > 0);
            return Flux.just(builder.toString());
        }).mergeWith(Flux.fromIterable(temps2).map(e -> {
            BigDecimal b;
            if (Objects.equals(e.type, TradeEnum.BUY)) {
                b = Optional.ofNullable(sellBalanceRestitution.get(e.userId)).orElse(BigDecimal.ZERO);//bcc
            } else {
                b = Optional.ofNullable(buyBalanceRestitution.get(e.userId)).orElse(BigDecimal.ZERO);//usdt
            }
            e.volume = TradeCompute.add(e.volume, b);
            return e;
        }).flatMap(e -> {
            long l = userCoinVolumeService.updateIncome(e.status, e.volume, e.userId, e.symbol);
            StringBuilder builder = new StringBuilder("USER_VOLUME:已得到信息(volume)：");
            builder.append("用户:").append(e.userId)
                    .append("币种:").append(e.symbol)
                    .append("扣减lock数量:").append(e.volume)
                    .append("退扣数量:").append(e.refundVolume)
                    .append("订单号：").append(e.orderNo)
                    .append("交易号：").append(e.tradeNo)
                    .append("订单状态：").append(e.status)
                    .append("处理结果:").append(l > 0);
            return Flux.just(builder.toString());
        }));
    }

    /**
     * 计算一个临时的属性；
     */
    private static class CoinVolumeTemp {
        /**
         * 订单状态；
         */
        OrderEnum.OrderStatus status;
        /**
         * 且户ID;
         */
        String userId;
        /**
         * 数量
         */
        BigDecimal volume;
        /**
         * 退回
         */
        BigDecimal refundVolume;
        /**
         * 币种；
         */
        String symbol;
        /**
         * 类型;
         */
        TradeEnum type;
        /**
         * 订单号;
         */
        String orderNo;
        /**
         * 交易号
         */
        String tradeNo;

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("CoinVolumeTemp{");
            sb.append("status=").append(status);
            sb.append(", userId='").append(userId).append('\'');
            sb.append(", volume=").append(volume);
            sb.append(", symbol='").append(symbol).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
