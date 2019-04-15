package com.biao.vo.otc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OtcOfflineDetailCountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private long noPayDetailCount;

    private long payDetailCount;
}
