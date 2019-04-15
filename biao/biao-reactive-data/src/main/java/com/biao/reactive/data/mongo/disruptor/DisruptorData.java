package com.biao.reactive.data.mongo.disruptor;

import com.biao.reactive.data.mongo.domain.SecurityLog;
import com.biao.reactive.data.mongo.domain.UserLoginLog;
import com.biao.reactive.data.mongo.enums.SecurityLogEnums;
import com.lmax.disruptor.EventFactory;

import java.time.LocalDateTime;

public class DisruptorData {

    public static DisruptorFactory FACTORY_INSTANCE = new DisruptorFactory();
    //定义数据结构
    //数据类型
    private Integer type;

    //用户登录日志 type=5
    private UserLoginLog userLoginLog;

    //用户登录日志 type=6
    private SecurityLog securityLog;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public UserLoginLog getUserLoginLog() {
        return userLoginLog;
    }

    public void setUserLoginLog(UserLoginLog userLoginLog) {
        this.userLoginLog = userLoginLog;
    }


    public SecurityLog getSecurityLog() {
        return securityLog;
    }

    public void setSecurityLog(SecurityLog securityLog) {
        this.securityLog = securityLog;
    }

    public static SecurityLog buildSecurityLog(SecurityLogEnums type, int status, String remark, String userId, String mobile) {
        SecurityLog securityLog = new SecurityLog();
        securityLog.setType(type.getCode());
        securityLog.setRemark(remark);
        securityLog.setStatus(status);
        securityLog.setUserId(userId);
        securityLog.setMobile(mobile);
        securityLog.setUpdateTime(LocalDateTime.now());
        return securityLog;
    }

    public static SecurityLog buildSecurityLog(SecurityLogEnums type, int status, String remark, String userId, String mobile, String mail) {
        SecurityLog securityLog = new SecurityLog();
        securityLog.setType(type.getCode());
        securityLog.setRemark(remark);
        securityLog.setStatus(status);
        securityLog.setUserId(userId);
        securityLog.setMobile(mobile);
        securityLog.setMail(mail);
        securityLog.setUpdateTime(LocalDateTime.now());
        return securityLog;
    }

    public static void saveSecurityLog(SecurityLog securityLog) {
        DisruptorManager.instance().runConfig();
        DisruptorData data = new DisruptorData();
        data.setType(6);
        data.setSecurityLog(securityLog);
        DisruptorManager.instance().publishData(data);
    }


    public static class DisruptorFactory implements EventFactory<DisruptorData> {
        @Override
        public DisruptorData newInstance() {
            return new DisruptorData();
        }

    }
}

