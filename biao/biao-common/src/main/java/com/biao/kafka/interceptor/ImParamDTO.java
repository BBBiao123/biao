package com.biao.kafka.interceptor;

import com.biao.enums.OfflineOrderDetailStatusEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * ImKafkaDTO.
 *
 *  ""(Myth)
 */
@Data
public class ImParamDTO implements Serializable {

    /**
     * 发送给谁 限制是userId_orderId.
     */
    private String toUserId;

    private String fromUserId;

    private String orderId;

    /**
     * 是否双方都发.
     */
    private Boolean twoSides;

    private OfflineOrderDetailStatusEnum orderDetailStatusEnum;


}
