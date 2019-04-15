package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * coin address
 */
@SqlTable("js_plat_coin_address")
@Data
public class CoinAddress implements Serializable {
    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;
    @SqlField("address")
    private String address;
    @SqlField("user_id")
    private String userId;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("symbol")
    private String symbol;
    @SqlField("type")
    private Integer type;
    @SqlField("status")
    private String status;
    @SqlField("create_date")
    protected LocalDateTime createDate;
    @SqlField("update_date")
    protected LocalDateTime updateDate;

}
