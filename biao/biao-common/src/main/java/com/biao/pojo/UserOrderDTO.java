package com.biao.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/6/3 下午3:11
 * @since JDK 1.8
 */
@Data
public class UserOrderDTO implements Serializable {

    private String orderNo;

    private Integer exType;

    private Integer status;

    private String userId;

    private BigDecimal successVolume;

    private String coinMain;

    private String coinOther;

}
