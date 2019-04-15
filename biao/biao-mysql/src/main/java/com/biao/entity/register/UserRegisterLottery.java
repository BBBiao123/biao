package com.biao.entity.register;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *  ""(Myth)
 */
@SqlTable("mk_user_register_lottery")
@Data
public class UserRegisterLottery implements Serializable {

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("name")
    private String name;

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("status")
    private Boolean status;

    @SqlField("total_prize")
    private BigDecimal totalPrize;

    @SqlField("recommend_min_volume")
    private BigDecimal recommendMinVolume;

    @SqlField("recommend_ratio")
    private Double recommendRatio;

    @SqlField("recommend_day_count")
    private Integer recommendDayCount;

    @SqlField("recommend_total_count")
    private Integer recommendTotalCount;

    @SqlField("recommend_count_limit")
    private Integer recommendCountLimit;

    @SqlField("start_date")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startDate;

    @SqlField("create_by")
    private String createBy;

    @SqlField("update_by")
    protected String updateBy;

    @SqlField("create_date")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    @SqlField("update_date")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    protected LocalDateTime updateDate;
}
