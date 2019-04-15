package com.biao.config.sercurity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UsernamePasswordAuthenticationTokenLocal extends UsernamePasswordAuthenticationToken {

    /**
     * 登录来源，为空默认为PLAT，否则为其他来源登录
     */
    private String loginSource;

    public UsernamePasswordAuthenticationTokenLocal(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public String getLoginSource() {
        return loginSource;
    }

    public void setLoginSource(String loginSource) {
        this.loginSource = loginSource;
    }
}
