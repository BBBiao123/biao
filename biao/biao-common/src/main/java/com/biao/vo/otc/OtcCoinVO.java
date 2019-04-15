package com.biao.vo.otc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OtcCoinVO extends OtcAccountSecretVO implements Serializable {

    private String id;
    private String name;
    private String fullName;
    private String key;
}
