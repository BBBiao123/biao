package com.biao.service.otc;

import com.biao.pojo.ResponsePage;
import com.biao.vo.otc.OtcAgentVO;

import java.util.Map;

public interface OtcAgentService {

    OtcAgentVO countAgentDetail(String userId, String loginSource);

    ResponsePage<OtcAgentVO> findGroupAgent(OtcAgentVO otcAgentVO, Map<String, String> paramMap);

    ResponsePage<OtcAgentVO> findGroupAgentAndVolume(OtcAgentVO otcAgentVO);

    OtcAgentVO findByMobileOrMail(OtcAgentVO otcAgentVO, Map<String, String> paramMap);

    void saveAgent(OtcAgentVO otcAgentVO, Map<String, String> paramMap);
}
