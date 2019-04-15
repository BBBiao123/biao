package com.biao.kafka.interceptor;

import lombok.Data;

import java.io.Serializable;

/**
 * ImKafkaDTO.
 *
 *  ""(Myth)
 */
@Data
public class ImKafkaDTO implements Serializable {

    /**
     * 发送给谁 限制是userId_orderId.
     */
    private String to;

    private String from;

    private Boolean twoSides;

    private String message;
}
