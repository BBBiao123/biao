package com.biao.vo.redis;

import lombok.Data;

import java.io.Serializable;

/**
 *  ""(Myth)
 */
@Data
public class RedisExPairVO implements Serializable {

    private String pairOther;

    private String pairOne;

    private String status;

    private Integer sort;

    private Integer type;

    private String free;

    private String maxVolume;

    private String minVolume;

    private Integer pricePrecision;

    private Integer volumePrecision;

    private Integer volumePercent;
}
