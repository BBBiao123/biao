package com.biao.config;

import lombok.Data;

@Data
public class MailConfigInfo {

    private String password;


    private String username;

    private String alias;

    private String sendname;

    private Boolean auth;

    private String host;

    private Boolean openssl;

    private Integer port;
}
