package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.io.Serializable;

@SqlTable("mobile_template")
public class MobileTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    private String id;

    @SqlField("code")
    private String code;

    @SqlField("access_key")
    private String accessKey;

    @SqlField("access_secret")
    private String accessSecret;

    @SqlField("sign_name")
    private String signName;

    @SqlField("template_param")
    private String templateParam;

    @SqlField("template_code")
    private String templateCode;

    @SqlField("work_sign")
    private String workSign;

    @SqlField("remark")
    private String remark;

    @SqlField("expand_json")
    private String expandJson;

    @SqlField("time_out")
    private Integer timeOut;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getWorkSign() {
        return workSign;
    }

    public void setWorkSign(String workSign) {
        this.workSign = workSign;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExpandJson() {
        return expandJson;
    }

    public void setExpandJson(String expandJson) {
        this.expandJson = expandJson;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

}
