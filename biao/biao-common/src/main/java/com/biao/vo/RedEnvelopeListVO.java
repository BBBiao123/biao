package com.biao.vo;

import com.biao.pojo.RequestQuery;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * c2c 红包列表
 */
@Getter
@Setter
public class RedEnvelopeListVO extends RequestQuery implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userId;

}
