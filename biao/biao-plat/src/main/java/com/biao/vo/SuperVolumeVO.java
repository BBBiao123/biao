package com.biao.vo;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户超级账本资产vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperVolumeVO implements Serializable {

    private String coinId;
    private String coinSymbol;
    private BigDecimal volume;
    private BigDecimal multiple;
    private BigDecimal breakRatio;
    private Long remainingDays;
    private Long frozenDays;


    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime depositBegin;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime depositEnd;


}
