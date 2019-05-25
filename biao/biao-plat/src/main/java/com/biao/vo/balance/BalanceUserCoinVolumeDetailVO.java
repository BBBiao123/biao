package com.biao.vo.balance;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
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
public class BalanceUserCoinVolumeDetailVO  implements Serializable {
    private String id;
    private String userId;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime incomeDate;

    private BigDecimal detailIncome;

    private BigDecimal detailReward;

    private BigDecimal staticsIncome;

    private BigDecimal teamRecord;

    private BigDecimal teamCommunityRecord;

    private int teamLevel;

    private BigDecimal communityStaticsIncome;

    private int nodeNumber;

    private BigDecimal realityStaticsIncome;

    private BigDecimal communityManageReward;

    private BigDecimal dynamicsIncome;

    private BigDecimal equalityReward;

    private BigDecimal sumRevenue;

    private String referId;

    private int validNum;

    private BigDecimal communitySumManageReward;

    private int version;

    private String coinSymbol;

    private String createStr;

}
