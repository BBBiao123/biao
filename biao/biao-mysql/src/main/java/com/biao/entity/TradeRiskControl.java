package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.math.BigDecimal;

/**
 *  ""(Myth)
 */
@Data
@SqlTable("trade_risk_control")
public class TradeRiskControl extends BaseEntity {

    @SqlField("coin_main")
    private String coinMain;

    @SqlField("coin_other")
    private String coinOther;

    @SqlField("user_ids")
    private String userIds;

    @SqlField("fixed_volume")
    private BigDecimal fixedVolume;

    @SqlField("risk_ratio")
    private double riskRatio;

}
