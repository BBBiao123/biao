package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 币种提现验证
 */
@Setter
@Getter
public class WithdrawValidateVO {
    private String id;
    private Integer googleCode;
    private String emailCode;
    private Integer exValidType;
    private String code;
}
