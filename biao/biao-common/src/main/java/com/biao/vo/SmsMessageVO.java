package com.biao.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SmsMessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String countryCode;

    private String mobile;

    private String code;

    private Integer googleCode;

    private String mail;

    private String countrySysid;

    private String countrySyscode;

    private String countrySysname;
}
