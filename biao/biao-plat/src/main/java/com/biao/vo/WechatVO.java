package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 支付宝
 */
@Getter
@Setter
public class WechatVO implements Serializable {

    private static final long serialVersionUID = 1L;


    private String wechatQrcodeId;

    private String wechatNo;

    private String realName;

}
