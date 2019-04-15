package com.biao.kline.vo;

import com.biao.binance.config.ExPair;
import com.biao.enums.KlineTimeEnum;
import com.biao.vo.KlineVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KlineDisruptorVO {

    private KlineVO klineVo;

    private KlineTimeEnum klineTimeEnum;

    private LocalDateTime formatTradeTime;
    
    private ExPair exPair ;
}
