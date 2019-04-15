package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.io.Serializable;

/**
 * finance_black_config
 */
@SqlTable("finance_black_config")
@Data
public class FinanceBlackConfig extends BaseEntity implements Serializable {

    /**
     * 用户id
     */
    @SqlField("userId")
    private String uesrId;

    @SqlField("c2c_in")
    private String c2cIn;

    @SqlField("coin_out")
    private String coinOut;

    /**
     * 描述
     */
    @SqlField("remarks")
    private String remarks;

}
