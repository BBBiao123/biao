package com.biao.vo.otc;

import com.biao.pojo.RequestQuery;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OtcOfflineOrderVO extends RequestQuery {

    private String orderId;
    private String userId;
    private String coinId;
    private String exType;
    private String loginSource;
    private BigDecimal volume;
    private BigDecimal price;
    private String symbol;


    private BigDecimal minVolume;
    private BigDecimal maxVolume;

    private String supportCurrencyCode;
    private String supportBank;

    private String exPassword;
}
