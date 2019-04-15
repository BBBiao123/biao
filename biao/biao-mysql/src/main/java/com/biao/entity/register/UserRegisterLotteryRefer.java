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
@SqlTable("mk_user_register_lottery_refer")
@Data
public class UserRegisterLotteryRefer implements Serializable {

    @SqlField("id")
    private String id;

    @SqlField("user_id")
    private String userId;

    @SqlField("refer_id")
    private String referId;

    @SqlField("lottery_id")
    private String lotteryId;

    @SqlField("rule_id")
    private String ruleId;

    @SqlField("lottery_name")
    private String lotteryName;

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("volume")
    private BigDecimal volume;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;

}
