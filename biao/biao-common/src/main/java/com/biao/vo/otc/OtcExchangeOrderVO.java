package com.biao.vo.otc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class OtcExchangeOrderVO extends OtcAccountSecretVO implements Serializable {

    private String batchNo;
    private String symbol;
    private String coinId;
    private String userId; // 支出方用户ID
    private BigDecimal volume;
    private String askUserId; // 收款方用户ID
    private BigDecimal askVolume; // 收款方到账数量
    private String remarks;

    private BigDecimal feeVolume;// 手续费

    private String payCode; // 商家支付时，调用接口

}
