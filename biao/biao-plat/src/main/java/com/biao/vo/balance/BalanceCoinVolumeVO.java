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
    private String coinId;
    private String  referId;
    private String  teamLevel;
    private BigDecimal teamAmount;
    private BigDecimal teamCommunityAmount;
    private BigDecimal sumRevenue;
    private BigDecimal yesterdayRevenue;
    private int validNum;
    private int oneInvite;
    private int ordNum;
    private String userName;
    private String createStr;
    private BigDecimal equalityReward;
    private BigDecimal communityManageReward;
    private BigDecimal shareReward;
    private BigDecimal scalpingReward;
    private BigDecimal differentialReward;
    private String positionName;
    private BigDecimal userSurplus;
    private String coinSymbol;
    private String coinPlatSymbol;
    private String mobile;
    private String mail;
    private String exPassword;
}
