package com.biao.pojo;

import com.biao.enums.TradeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 *  ""(""611 @ qq.com)
 */
@Data
public class RemovePlatOrderDTO implements Serializable {

    private String coinMain;

    private String coinOther;

    private TradeEnum type;

    private String orderNo;

}
