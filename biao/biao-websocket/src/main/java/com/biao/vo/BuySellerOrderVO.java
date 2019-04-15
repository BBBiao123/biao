package com.biao.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 买单卖单封装实体类.
 *
 *  ""
 */
@Getter
@Setter
@ToString
public class BuySellerOrderVO implements Serializable {

    /**
     * 平台买单挂单集合.
     */
    private List<PlatOrderVO> buyOrderVOList;


    /**
     * 平台卖单挂单集合.
     */
    private List<PlatOrderVO> sellOrderVOList;
}
