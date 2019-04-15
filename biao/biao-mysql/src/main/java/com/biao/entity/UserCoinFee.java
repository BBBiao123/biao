package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * UserCoinFee .
 * ctime: 2018/8/31 14:59
 * 用户手续费处理.
 *
 *  "" sixh
 */
@SqlTable("js_plat_user_coin_fee")
@Data
public class UserCoinFee extends BaseEntity implements Serializable {

    /**
     * 用户id.
     */
    @SqlField("user_id")
    private String userId;

    /**
     * 主区.
     */
    @SqlField("pair_one")
    private String pairOne;

    /**
     * 被交易区.
     */
    @SqlField("pair_other")
    private String pairOther;

    /**
     * 是否生效
     */
    @SqlField("status")
    private Boolean status;

    /**
     * 手续费费用fee;
     */
    @SqlField("fee")
    private BigDecimal fee;
}
