package com.biao.service.impl;

import com.biao.service.SuperCoinVolumeService;
import com.biao.service.SuperCoinVolumeTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SuperCoinVolumeTaskServiceImpl implements SuperCoinVolumeTaskService {

    private static Logger logger = LoggerFactory.getLogger(SuperCoinVolumeTaskServiceImpl.class);

    @Autowired
    private SuperCoinVolumeService superCoinVolumeService;

    @Override
    public void triggerHandleExpireAccountEntry() {
        logger.info("开时执行过期超级钱包");
        superCoinVolumeService.handleExpireAccount(LocalDateTime.now());
        logger.info("结束执行过期超级钱包");
    }
}
