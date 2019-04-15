package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 红包发送
 */
@Getter
@Setter
public class RedEnvelopeOpenVO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userId;
    private String redEnvelopeId;


}
