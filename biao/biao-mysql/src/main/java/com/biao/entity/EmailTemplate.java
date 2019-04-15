package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;


@SqlTable("email_template")
public class EmailTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @SqlField("name")
    private String name;

    @SqlField("code")
    private String code;

    @SqlField("business_type")
    private String businessType;

    @SqlField("template_subject")
    private String templateSubject;

    @SqlField("template_content")
    private String templateContent;

    @SqlField("remarks")
    private String remarks;

    @SqlField("del_flag")
    private String delFlag;

    @SqlField("expire_time")
    private Integer expireTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTemplateSubject() {
        return templateSubject;
    }

    public void setTemplateSubject(String templateSubject) {
        this.templateSubject = templateSubject;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }
}
