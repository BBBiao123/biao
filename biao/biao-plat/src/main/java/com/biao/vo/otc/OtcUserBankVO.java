package com.biao.vo.otc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OtcUserBankVO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String type;
    private String payNo;
    private String qrcodeId;
    private String bankName;
    private String branchBankName;
    private String country;
    private String status;
    private String mobile;
    private String realName;
    private String cardNo;
    private String supportCurrencyCode;
}
