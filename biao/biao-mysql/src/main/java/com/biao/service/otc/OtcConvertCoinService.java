package com.biao.service.otc;

import com.biao.entity.otc.OtcConvertCoin;
import com.biao.vo.otc.OtcConvertCoinVO;

public interface OtcConvertCoinService {

    OtcConvertCoin findByBatchNo(OtcConvertCoinVO otcConvertCoinVO);

    OtcConvertCoin executeConvert(OtcConvertCoin otcConvertCoin, OtcConvertCoinVO otcConvertCoinVO);

    void doConvertCore(OtcConvertCoin otcConvertCoin);
}
