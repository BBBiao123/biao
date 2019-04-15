package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 红包发送
 */
@Getter
@Setter
public class RedEnvelopeMySendVO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String realName;
    private Integer totalNumber;
    private BigDecimal volume;
    private String coinSymbol;


}
