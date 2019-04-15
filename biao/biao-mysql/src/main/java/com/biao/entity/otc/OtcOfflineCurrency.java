package com.biao.entity.otc;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

/**
 * otc法币
 */
@SqlTable("otc_offline_currency")
public class OtcOfflineCurrency extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @SqlField("code")
    private String code;
    @SqlField("name_en")
    private String nameEn;
    @SqlField("name_cn")
    private String nameCn;
    @SqlField("country")
    private String country;
    @SqlField("status")
    private String status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
