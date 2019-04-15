package com.biao.vo;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 红包发送
 */
@Getter
@Setter
public class RedEnvelopeViewVO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userId;
    private String redEnvelopeId;
    private String coinSymbol;
    private String realName;
    private BigDecimal volume;
    private BigDecimal receiveVolume;
    private Integer totalNumber;
    private Integer receiveNumber;
    private String status;
    private String bestWith;
    private String remark;
    private String isReceived = "0";
    private BigDecimal myReceiveVolume;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateDate;

    private String type;

}
