package com.biao.vo.otc;

import com.biao.pojo.RequestQuery;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OtcOfflineOrderDetailVO extends RequestQuery {
    private String userId;
    private String coinId;
    private String symbol;
    private String orderId;
    private BigDecimal volume;
    private String subOrderId;
    private String exPassword;
    private String loginSource;

    private String exType;

    private String detailPay;

    private String status;

    private String updateBy;

    private String userMobile;

    private String ts;// 时间戳  otc动态生成
    private String loginIp; // 客户端IP
    private String publishSource; // 来源

    private String key; // 加密校验
}
