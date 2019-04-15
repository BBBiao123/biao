package com.biao.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/4/30 下午5:08
 * @since JDK 1.8
 */
@Data
public class BankVO implements Serializable {

    private String bankName;
    private String branchBankName;
    private String mobile;
    private String realName;
    private String cardNo;
    private String alipayNo;
    private String alipayQrcodeId;
    private String wechatQrcodeId;
    private String wechatNo;
    private String bankUserName;
}
