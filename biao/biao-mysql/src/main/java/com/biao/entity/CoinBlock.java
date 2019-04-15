package com.biao.entity;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  ""(""611 @ qq.com)
 */
@Data
@SqlTable("js_plat_coin_block")
public class CoinBlock implements Serializable {

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    private String id;

    @SqlField("symbol")
    private String symbol;

    @SqlField("block_height")
    private Integer blockHeight;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    @SqlField("update_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateDate;
}
