package com.biao.entity.register;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *  ""(Myth)
 */
@SqlTable("mk_user_register_lottery_log")
@Data
public class UserRegisterLotteryLog implements Serializable {

    @SqlField("id")
    private String id;

    @SqlField("user_id")
    private String userId;

    @SqlField("lottery_id")
    private String lotteryId;

    @SqlField("lottery_name")
    private String lotteryName;

    @SqlField("rule_id")
    private String ruleId;

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("real_volume")
    private BigDecimal realVolume;

    @SqlField("reason")
    private String reason;

    @SqlField("reason_type")
    private Integer reasonType;

    @SqlField("mail")
    private String mail;

    @SqlField("phone")
    private String phone;

    @SqlField("recommend_id")
    private String recommendId;

    @SqlField("phone_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime phoneDate;

    @SqlField("source")
    private String source;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;

}
