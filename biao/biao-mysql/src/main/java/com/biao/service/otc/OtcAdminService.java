package com.biao.service.otc;

import com.biao.entity.otc.OtcAppropriationRequest;
import com.biao.entity.otc.OtcOfflineAppealRequest;
import com.biao.entity.otc.OtcOfflineOrderDetail;
import com.biao.pojo.ResponsePage;
import com.biao.vo.otc.OtcAppropriationRequestVO;
import com.biao.vo.otc.OtcCoinVO;
import com.biao.vo.otc.OtcOfflineAppealRequestVO;
import com.biao.vo.otc.OtcOfflineOrderDetailVO;

import java.util.Map;

public interface OtcAdminService {

    OtcCoinVO findCoin(OtcCoinVO otcCoinVO, Map<String, String> paramMap);

    String getBatchNo();

//    OtcOfflineAppealRequest doAppeal(OtcOfflineAppealRequestVO otcOfflineAppealRequestVO, Map<String, String> paramMap);
//
//    OtcOfflineAppealRequest findAppeal(OtcOfflineAppealRequestVO otcOfflineAppealRequestVO);
//
//    void doAppealActtion(OtcOfflineAppealRequestVO otcOfflineAppealRequestVO, Map<String, String> paramMap, OtcOfflineAppealRequest appealRequest);

    OtcAppropriationRequest findAppropriation(OtcAppropriationRequestVO otcOfflineAppealRequestVO);

    OtcAppropriationRequest doAppropriation(OtcAppropriationRequestVO otcAppropriationRequestVO, Map<String, String> paramMap);

    void doAppropriationAction(OtcAppropriationRequestVO otcAppropriationRequestVO, Map<String, String> paramMap, OtcAppropriationRequest otcAppropriationRequest);

//    ResponsePage<OtcOfflineOrderDetail> getDetails(OtcOfflineOrderDetailVO otcOfflineOrderDetailVO, Map<String, String> paramMap);

    String getLastRate(String mainCoin, String otherCoin);
}
