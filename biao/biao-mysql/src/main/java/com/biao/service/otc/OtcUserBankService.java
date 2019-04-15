package com.biao.service.otc;

import com.biao.entity.otc.OtcUserBank;

import java.util.List;

public interface OtcUserBankService {

    List<OtcUserBank> findByParam(OtcUserBank otcUserBank);

    String save(OtcUserBank otcUserBank);

    List<OtcUserBank> findByUserIdAndStatus(String userId, String status);
}
