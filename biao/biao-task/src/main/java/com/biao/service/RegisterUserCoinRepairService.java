package com.biao.service;

import com.biao.entity.mk2.Mk2PopularizeRegisterConf;

import java.time.LocalDateTime;

public interface RegisterUserCoinRepairService {

//    void doRegisterRepair();

    void taskLog(String status, String reason, String curDate, Long dayGiveColume, Mk2PopularizeRegisterConf conf, LocalDateTime curDateTime);

    void doTimeRepair();

}
