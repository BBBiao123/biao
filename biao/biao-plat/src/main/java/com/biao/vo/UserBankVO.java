package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserBankVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String bankName;
    private String branchBankName;
    private String mobile;
    private String realName;
    private String cardNo;
}
