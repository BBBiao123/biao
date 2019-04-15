package com.biao.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * BatchCancelTradeDTO.
 * <p>
 * <p>
 * 18-11-27下午4:10
 *
 *  "" sixh
 */
@Data
public class BatchCancelTradeDTO implements Serializable {
    /**
     * 用户id.
     */
    private String userId;

    /**
     * 买入、买出的币种.
     */
    private String coinOther;

    /**
     * 主区.
     */
    private String coinMain;
}
