package com.biao.lottery;

import lombok.Data;

import java.io.Serializable;

/**
 *  ""(Myth)
 */
@Data
public class LotteryVO implements Serializable {

    private String coinSymbol;

    private Integer count;

    private String msg;
}
