package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OfflineAppealVO implements Serializable {

    private String appealId;// 申诉单ID

    private String subOrderId;// 订单号

    private String appealUserId;// 申诉人ID

    private String appealType;// 原因类型

    private String reason;// 原因描述

    private String imagePath;// 图片1

    private String imagePath2;// 图片2

    private String imagePath3;// 图片3

}
