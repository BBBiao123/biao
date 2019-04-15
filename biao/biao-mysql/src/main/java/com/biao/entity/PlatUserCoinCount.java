package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户资金统计按天
 */
@SqlTable("js_plat_user_coin_volume_count")
public class PlatUserCoinCount {
    private static final long serialVersionUID = 1L;
    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("type")
    private String type;

    @SqlField("type_desc")
    private String typeDesc;

    @SqlField("person_count")
    private Long personCount;

    @SqlField("hold_coin_volume")
    private BigDecimal holdCoinVolume;

    @SqlField("count_date")
    private LocalDateTime countDate;

    @SqlField("create_date")
    private LocalDateTime createDate;

    @SqlField("update_date")
    private LocalDateTime updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public Long getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Long personCount) {
        this.personCount = personCount;
    }

    public BigDecimal getHoldCoinVolume() {
        return holdCoinVolume;
    }

    public void setHoldCoinVolume(BigDecimal holdCoinVolume) {
        this.holdCoinVolume = holdCoinVolume;
    }

    public LocalDateTime getCountDate() {
        return countDate;
    }

    public void setCountDate(LocalDateTime countDate) {
        this.countDate = countDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
