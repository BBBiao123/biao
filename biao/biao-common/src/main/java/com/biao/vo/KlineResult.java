package com.biao.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * KlineResult.
 *
 *  ""
 */
@Data
public class KlineResult implements Serializable {

    private Integer code;

    private String type;

    private List<KlineVO> data;
}
