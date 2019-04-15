package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * eth token 余额
 */
@SqlTable("eth_token_volume")
public class EthTokenVolume extends BaseEntity {

    @SqlField("name")
    private String name;

    @SqlField(value = "volume")
    private BigDecimal volume;

    @SqlField(value = "address")
    private String address;

    @SqlField(value = "tx_id")
    private String txId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
