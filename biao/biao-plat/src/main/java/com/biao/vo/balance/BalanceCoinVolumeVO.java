package com.biao.vo.balance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户余币宝资产vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceCoinVolumeVO implements Serializable {
    private String id;
    private String name;
    private String userId;
    private BigDecimal coinBalance;
    private BigDecimal dayRate;
    private BigDecimal accumulIncome;
    private BigDecimal yesterdayIncome;
    private BigDecimal accumulReward;
    private BigDecimal yesterdayReward;
    private BigDecimal coinNum;
    private String  rise;


}
