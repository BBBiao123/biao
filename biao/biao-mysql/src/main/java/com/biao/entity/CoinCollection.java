package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@SqlTable("coin_collection")
@Data
public class CoinCollection implements Serializable {

    private static final long serialVersionUID = -1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    private String id;

    @SqlField("symbol")
    private String symbol;

    @SqlField("user_Id")
    private String userId;

    @SqlField("address")
    private String address;

    @SqlField("volume")
    private BigDecimal volume;

    @SqlField("status")
    private Integer status;



}
