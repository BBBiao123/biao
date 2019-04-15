package com.biao.reactive.data.mongo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    @Id
    protected String id;
    private String userId;
    private BigDecimal askVolume;
    private BigDecimal successVolume;
    private String coinId;
    private String coinSymbol;
    private String toCoinId;
    private String toCoinSymbol;
    private String toCoinVolume;
    private BigDecimal exFee;
    private BigDecimal exType;
    private Integer status;


    protected String createBy;

    protected String updateBy;

    protected LocalDateTime createDate;

    protected LocalDateTime updateDate;


}
