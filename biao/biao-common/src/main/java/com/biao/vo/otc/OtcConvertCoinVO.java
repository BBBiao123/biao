package com.biao.vo.otc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class OtcConvertCoinVO extends OtcAccountSecretVO implements Serializable {
    private String userId;

    private String batchNo;

    private String fromCoinId;

    private BigDecimal fromVolume;

    private String toCoinId;

    private BigDecimal toVolume;

    private BigDecimal rate;

    private BigDecimal feeVolume;

    private String remarks;

    private String publishSource;
}
