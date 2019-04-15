package com.biao.reactive.data.mongo.service.impl;

import com.biao.reactive.data.mongo.domain.SecurityLog;
import com.biao.reactive.data.mongo.repository.SecurityLogRepository;
import com.biao.reactive.data.mongo.service.SecurityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("securityLogService")
public class SecurityLogServiceImpl implements SecurityLogService {

    @Autowired
    private SecurityLogRepository securityLogRepository;

    public void insert(SecurityLog securityLog) {
        securityLogRepository.insert(securityLog);
    }
}
