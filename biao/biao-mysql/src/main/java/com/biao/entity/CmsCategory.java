package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

@SqlTable("cms_category")
public class CmsCategory extends BaseEntity {

    @SqlField("remarks")
    private String remarks;
    @SqlField("name")
    private String name;
    @SqlField("image")
    private String image;
    @SqlField("keywords")
    private String keywords;
    @SqlField("description")
    private String description;
    @SqlField("del_flag")
    private Integer delFlag;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
