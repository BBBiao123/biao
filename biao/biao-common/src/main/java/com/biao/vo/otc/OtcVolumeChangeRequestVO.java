package com.biao.vo.otc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class OtcVolumeChangeRequestVO extends OtcAccountSecretVO implements Serializable {

    private String type;
    private String batchNo;
    private String coinId;
    private String symbol;
    private String sellUserId;
    private String buyUserId;
    private String orderId;
    private String adType;
    private String subOrderId;
    private BigDecimal volume;
    private BigDecimal feeVolume;
    private String remarks;
    private String loginUserId;
    private String requestLogId;
    private String requestUri;
    private String feeDeductType;

}
