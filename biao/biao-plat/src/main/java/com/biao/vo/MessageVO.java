package com.biao.vo;

import java.io.Serializable;

public class MessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mail;

    private String type;

    private String code;

    private String mobile;

    private String appKey;

    private String sessionId;

    private String sig;

    private String vtoken;

    private String scene;

    private String source;

    private String authTag;

    private String vaildCodeKey;

    private String countrySyscode;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getVtoken() {
        return vtoken;
    }

    public void setVtoken(String vtoken) {
        this.vtoken = vtoken;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthTag() {
        return authTag;
    }

    public void setAuthTag(String authTag) {
        this.authTag = authTag;
    }

    public String getVaildCodeKey() {
        return vaildCodeKey;
    }

    public void setVaildCodeKey(String vaildCodeKey) {
        this.vaildCodeKey = vaildCodeKey;
    }

    public String getCountrySyscode() {
        return countrySyscode;
    }

    public void setCountrySyscode(String countrySyscode) {
        this.countrySyscode = countrySyscode;
    }
}
