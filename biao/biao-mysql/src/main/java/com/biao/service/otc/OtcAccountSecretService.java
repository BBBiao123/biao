package com.biao.service.otc;

import com.biao.entity.otc.OtcAccountSecret;
import com.biao.vo.otc.OtcAccountSecretVO;

import java.util.Map;

public interface OtcAccountSecretService {

    void checkIsMasterAccount(String userId);

    OtcAccountSecret findByPublishSource(String publishSource);

    void checkAccountSecret(OtcAccountSecretVO otcAccountSecretVO, Map<String, String> secretMap);

    void checkAccountSecret(String ip, String key, String publishSource, Map<String, String> secretMap);
}
