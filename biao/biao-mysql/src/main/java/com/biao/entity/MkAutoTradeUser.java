package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

/**
 * 交易对
 */
@SqlTable("mk_auto_trade_user")
@Data
public class MkAutoTradeUser extends BaseEntity {

    @SqlField("user_id")
    private String userId;

    @SqlField("username")
    private String username;

    @SqlField("mobile")
    private String mobile;

    @SqlField("mail")
    private String mail;

    @SqlField("real_name")
    private String realName;

    @SqlField("id_card")
    private String idCard;

    @SqlField("remark")
    private String remark;

}