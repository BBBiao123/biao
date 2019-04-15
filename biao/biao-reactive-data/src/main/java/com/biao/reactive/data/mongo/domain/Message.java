package com.biao.reactive.data.mongo.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *  ""(Myth)
 */
@Data
@RequiredArgsConstructor
@Document
public class Message {

    @Id
    private String id;

    private String fromUserId;

    private String toUserId;

    private String orderId;

    /**
     * 0 c2c  1 bb.
     */
    private Integer type;

    private Integer orderStatus;

    private String coinMain;

    private String coinOther;

    private BigDecimal volume;

    private BigDecimal price;

    private LocalDateTime createTime;


}
