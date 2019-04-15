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
public class RedEnvelopeSubViewVO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userId;
    private String redEnvelopeId;
    private String coinSymbol;
    private String realName;
    private BigDecimal volume;
    private String isBest = "0";
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateDate;
    private BigDecimal price;
    private String remark;

}
