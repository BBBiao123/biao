package com.biao.reactive.data.mongo.disruptor;

import com.biao.reactive.data.mongo.service.SecurityLogService;
import com.biao.reactive.data.mongo.service.UserLoginLogService;
import com.biao.spring.SpringBeanFactoryContext;
import com.lmax.disruptor.WorkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisruptorDataEventHandler implements WorkHandler<DisruptorData> {

    private static Logger logger = LoggerFactory.getLogger(DisruptorDataEventHandler.class);


    private UserLoginLogService userLoginLogService;

    private SecurityLogService securityLogService;

    public DisruptorDataEventHandler() {
        userLoginLogService = SpringBeanFactoryContext.findBean(UserLoginLogService.class);
        securityLogService = SpringBeanFactoryContext.findBean(SecurityLogService.class);
    }

    @Override
    public void onEvent(DisruptorData disruptorData) throws Exception {
        //获取
        if (disruptorData.getType() == null) {
            logger.error("Disruptor receive data error, not type value");
            return;
        }
        if (disruptorData.getType() == 5) {
            //记录用户登录日志
            userLoginLogService.insert(disruptorData.getUserLoginLog());
        }
        if (disruptorData.getType() == 6) {
            securityLogService.insert(disruptorData.getSecurityLog());
        }
    }

}
