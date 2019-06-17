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
 * 用户收益和奖励明细
 *
 *  ""
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceUserVolumeIncomeDetailVO implements Serializable {
    private String id;
    private String userId;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime incomeDate;

    private BigDecimal detailReward;

    private String rewardType;

    private int version;

    private String coinSymbol;

    private String createStr;

}
