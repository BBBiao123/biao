package com.biao.service.otc;

import com.biao.entity.otc.OtcExchangeOrder;
import com.biao.vo.otc.OtcExchangeOrderVO;

import java.util.Map;

public interface OtcExchangeOrderService {

    OtcExchangeOrder findByBatchNo(String batchNo);

    String getBatchNo();

//    OtcExchangeOrder userPay(OtcExchangeOrder otcExchangeOrder);

//    OtcExchangeOrder otcPay(OtcExchangeOrder otcExchangeOrder, Map<String, String> paramMap, OtcExchangeOrderVO otcExchangeOrderVO);

//    OtcExchangeOrder shPay(OtcExchangeOrder otcExchangeOrder, Map<String, String> paramMap, OtcExchangeOrderVO otcExchangeOrderVO);

//    void exchangeVolumeInOut(OtcExchangeOrder otcExchangeOrder);

    OtcExchangeOrder custUserPay(OtcExchangeOrder otcExchangeOrder);

    OtcExchangeOrder custOtcPay(OtcExchangeOrder otcExchangeOrder, Map<String, String> paramMap, OtcExchangeOrderVO otcExchangeOrderVO);

    void exchangeCustVolumeInOut(OtcExchangeOrder otcExchangeOrder);
}
