package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 支付宝
 */
@Getter
@Setter
public class AlipayVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String realName;
    private String alipayNo;
    private String alipayQrcodeId;
}
