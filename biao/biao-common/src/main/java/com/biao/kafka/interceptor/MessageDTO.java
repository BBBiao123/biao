package com.biao.kafka.interceptor;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * MessageDTO.
 *
 *  ""(Myth)
 */
@Data
public class MessageDTO implements Serializable {

    private String id;

    private String orderId;

    private Integer status;

    private Integer type;

    private String coinMain;

    private String coinOther;

    private String userId;

    private Long createTime;

    private BigDecimal volume;

    private BigDecimal price;


}
