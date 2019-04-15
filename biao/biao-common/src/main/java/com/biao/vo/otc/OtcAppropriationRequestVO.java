package com.biao.vo.otc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OtcAppropriationRequestVO extends OtcAccountSecretVO implements Serializable {

    private String batchNo;
    private String coinId;
    private String symbol;
    private String userIdStr;
    private String volumeStr;
    private String remarks;
    private String type;// 拨币类型，0广告商拨币，1收益拨币

    private String secretKey;
}
