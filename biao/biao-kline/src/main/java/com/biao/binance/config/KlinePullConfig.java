package com.biao.binance.config;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * The type Kline pull config.
 *
 *  ""(Myth)
 */
@Data
@ToString
public class KlinePullConfig implements Serializable {

    private String coinMain;

    private String coinOther;

    private String exchangeName;

    private String pullUrl;

    private Boolean proxyed;

    private Boolean status;
}
