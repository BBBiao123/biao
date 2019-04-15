package com.biao.service.otc;

import com.biao.entity.otc.OtcAppVersion;

public interface OtcAppVersionService {
    OtcAppVersion getLastestByType(String type);
}
