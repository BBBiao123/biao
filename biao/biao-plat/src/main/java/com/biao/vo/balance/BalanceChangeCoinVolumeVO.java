package com.biao.vo.balance;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余币宝资产vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceChangeCoinVolumeVO implements Serializable {
    private String id;
    private String coinSymbol;
    private String coinPlatSymbol;
    private String userId;
    private BigDecimal coinNum;
    private String userName;
    private String createStr;
    private BigDecimal accumulIncome;
    private long createTime;
    private String takeOutTimeStr;
    private int flag;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime createDate;
    private String exPassword;
    private Integer contractTime;
}
