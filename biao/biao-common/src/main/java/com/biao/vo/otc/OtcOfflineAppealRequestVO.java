package com.biao.vo.otc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OtcOfflineAppealRequestVO extends OtcAccountSecretVO implements Serializable {
    private String batchNo;
    private String subOrderId;
    private String resultUserId;
    private String resultExType;
    private String status;
    private String remarks;
    private String result;

    private String secretKey;
}
