package com.biao.previous.message;

import com.biao.previous.domain.CancelResult;
import com.biao.previous.domain.TrType;
import com.biao.service.UserCoinVolumeExService;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Optional;

/**
 * CancelUserCoinVolumeConsumer.
 * <p>
 * <p>
 * 18-12-29下午5:47
 *
 *  "" sixh
 */
public class CancelUserCoinVolumeConsumer implements TrExecutorSubscriber<CancelResult>  {
    /**
     * 修改用户资产信息；
     */
    private UserCoinVolumeExService userCoinVolumeService;

    public CancelUserCoinVolumeConsumer(UserCoinVolumeExService userCoinVolumeService) {
        this.userCoinVolumeService = userCoinVolumeService;
    }
    @Override
    public Flux<String> executor(Collection<? extends CancelResult> collections) {
        Optional<? extends CancelResult> first = collections.stream().findFirst();
        if(first.isPresent()) {
            CancelResult cr = first.get();
            return Flux.just(cr).flatMap(e -> {
                long l = userCoinVolumeService.subtractLockVolume(e.getUserId(), e.getCoinSymbol(), e.getBlockVolume(),true);
                StringBuilder builder = new StringBuilder("[取消]-->订单退资产(lock_volume)：");
                builder.append("用户:").append(e.getUserId())
                        .append("币种:").append(e.getCoinSymbol())
                        .append("扣减lock数量:").append(e.getBlockVolume())
                        .append("退扣数量:").append(e.getBlockVolume())
                        .append("订单号：").append(e.getOrderNo())
                        .append("处理结果:").append(l > 0);
                return Flux.just(builder.toString());
            });
        }
        return Flux.just("发送过来的消息没有找到更新资产信息..不影响资产状态.......");
    }

    @Override
    public TrType getType() {
        return TrType.CANCEL;
    }
}
