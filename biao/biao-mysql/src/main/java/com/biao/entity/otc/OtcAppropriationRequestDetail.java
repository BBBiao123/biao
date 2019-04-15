package com.biao.entity.otc;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.biao.sql.build.otc.OtcBaseEntity;

import java.math.BigDecimal;

/**
 * 拨币请求实体
 */
@SqlTable("otc_appropriation_request_detail")
public class OtcAppropriationRequestDetail extends OtcBaseEntity {

    @SqlField("batch_no")
    private String batchNo;

    @SqlField("symbol")
    private String symbol;

    @SqlField("coin_id")
    private String coinId;

    @SqlField("user_id")
    private String userId;

    @SqlField("volume")
    private BigDecimal volume;

    @SqlField("status")
    private String status;

    @SqlField("type")
    private String type;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
