package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class OfflineConfirmVO implements Serializable {


    private String orderId;
    private String subOrderId;
    private Integer status;
    private String userId;
    private String updateBy;


}
