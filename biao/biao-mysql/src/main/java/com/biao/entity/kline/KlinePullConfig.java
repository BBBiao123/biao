package com.biao.entity.kline;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;
import lombok.ToString;

/**
 *  ""(Myth)
 */
@SqlTable("kline_pull_config")
@Data
@ToString
public class KlinePullConfig extends BaseEntity {

    @SqlField("coin_main")
    private String coinMain;

    @SqlField("coin_other")
    private String coinOther;

    @SqlField("exchange_name")
    private String exchangeName;

    @SqlField("pull_url")
    private String pullUrl;

    @SqlField("proxyed")
    private Boolean proxyed;

    @SqlField("status")
    private Boolean status;
}
