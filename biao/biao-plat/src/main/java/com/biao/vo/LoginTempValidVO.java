package com.biao.vo;

import java.io.Serializable;

public class LoginTempValidVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tempToken;

    private Integer tokenType;

    private String code;

    public String getTempToken() {
        return tempToken;
    }

    public void setTempToken(String tempToken) {
        this.tempToken = tempToken;
    }

    public Integer getTokenType() {
        return tokenType;
    }

    public void setTokenType(Integer tokenType) {
        this.tokenType = tokenType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
