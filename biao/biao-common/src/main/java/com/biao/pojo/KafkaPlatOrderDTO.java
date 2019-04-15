package com.biao.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送订单消息到可处理的kafka队列中。
 */
@Data
public class KafkaPlatOrderDTO implements Serializable {

    /**
     * 保存一个动作;
     */
    private Action action;
    /**
     * 加入数据动作;
     */
    private TradeDto tradeDto;
    /**
     * 删除数据的动作;
     */
    private RemovePlatOrderDTO removeTradeDto;

    /**
     * 构建一个通知kafka put消息通知的构建;
     *
     * @param dto dto;
     * @return 消息；
     */
    public static KafkaPlatOrderDTO buildPut(TradeDto dto) {
        KafkaPlatOrderDTO kpt = new KafkaPlatOrderDTO();
        kpt.setAction(Action.PUT);
        kpt.setTradeDto(dto);
        return kpt;
    }

    /**
     * 构建一个通知kafka remove消息通知的构建;
     *
     * @param dto dto;
     * @return 消息；
     */
    public static KafkaPlatOrderDTO buildRemove(RemovePlatOrderDTO dto) {
        KafkaPlatOrderDTO kpt = new KafkaPlatOrderDTO();
        kpt.setAction(Action.REMOVE);
        kpt.setRemoveTradeDto(dto);
        return kpt;
    }

    /**
     * 定义一个可处理的动作;
     */
    public enum Action {
        /**
         * 增加与修改数据;
         */
        PUT,
        /**
         * 删除数据;
         */
        REMOVE
    }
}
