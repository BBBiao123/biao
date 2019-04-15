package com.biao.vo.redis;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MainCnbVO implements Serializable {

    private String mainCoinId;

    private String mainCoinSymbol;

    private BigDecimal cnbRate;
}
