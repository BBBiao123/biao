package com.biao.vo.redis;

import lombok.Data;

import java.io.Serializable;

/**
 *  ""(Myth)
 */
@Data
public class RedisMkAutoTradeUserVO implements Serializable {

    private String userId;

    private String username;

    private String mobile;

    private String mail;

    private String realName;

    private String idCard;

    private String remark;
}
