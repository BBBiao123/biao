package com.biao.vo.otc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OtcVolumeChangeQueryVO extends OtcAccountSecretVO implements Serializable {

    private String batchNo;


}
