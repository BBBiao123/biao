package com.biao.service.kline;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.biao.enums.KlineTimeEnum;

import lombok.Data;

@Data
public class KlineLogDataConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private String coinMain;

    private String coinOther;

    private KlineTimeEnum dayType;
    
    private boolean synRedis ;
    
    private boolean klineInitTag ;
    
    private LocalDateTime tradeTime ;
}
