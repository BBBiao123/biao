package com.biao.entity.otc;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.biao.sql.build.otc.OtcBaseEntity;

/**
 * OTC资产兑换表
 */
@SqlTable("otc_convert_coin_conf")
public class OtcConvertCoinConf extends OtcBaseEntity {
    @SqlField("from_symbol")
    private String fromSymbol;
    @SqlField("to_symbol")
    private String toSymbol;
    @SqlField("status")
    private String status;

    public String getFromSymbol() {
        return fromSymbol;
    }

    public void setFromSymbol(String fromSymbol) {
        this.fromSymbol = fromSymbol;
    }

    public String getToSymbol() {
        return toSymbol;
    }

    public void setToSymbol(String toSymbol) {
        this.toSymbol = toSymbol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
