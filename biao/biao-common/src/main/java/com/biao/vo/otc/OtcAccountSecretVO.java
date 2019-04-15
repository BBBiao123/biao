package com.biao.vo.otc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OtcAccountSecretVO implements Serializable {

    private String loginIp; // 客户端IP
    private String publishSource; // 来源

    private String key; // 加密校验

}
