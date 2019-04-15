package com.biao.service.impl.otc;

import com.biao.entity.otc.OtcAppVersion;
import com.biao.mapper.otc.OtcAppVersionDao;
import com.biao.service.otc.OtcAppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtcAppVersionServiceImpl implements OtcAppVersionService {

    @Autowired
    private OtcAppVersionDao otcAppVersionDao;

    @Override
    public OtcAppVersion getLastestByType(String type) {
        return otcAppVersionDao.getLastestByType(type);
    }
}
