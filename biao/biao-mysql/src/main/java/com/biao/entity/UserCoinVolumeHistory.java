package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户资产
 *
 *  ""zj
 */
@SqlTable("js_plat_user_coin_volume_history")
@Data
public class UserCoinVolumeHistory extends BaseEntity {

    @SqlField("user_id")
    private String userId;

    @SqlField("account")
    private String account;

    @SqlField("type")
    private String type;

    @SqlField("coin_id")
    private String coinId;

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("volume")
    private BigDecimal volume;

    @SqlField("remark")
    private String remark;

}
