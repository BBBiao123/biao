package com.biao.vo.otc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OtcCoinVolumeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mailOrMobileOrId;

    private String publishSource;

    private String key;


}
