package com.biao.service.impl;

import com.biao.entity.BankDepositLog;
import com.biao.mapper.BankDepositDao;
import com.biao.service.BankDepositService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankDepositServiceImpl implements BankDepositService {

    @Autowired
    private BankDepositDao bankDepositDao;

    @Override
    public String save(BankDepositLog bankDepositLog) {
        String id = SnowFlake.createSnowFlake().nextIdString();
        bankDepositLog.setStatus(0);
        bankDepositLog.setCreateDate(LocalDateTime.now());
        bankDepositLog.setId(id);
        bankDepositDao.insert(bankDepositLog);
        return id;
    }

    @Override
    public BankDepositLog findById(String id) {
        return bankDepositDao.findById(id);
    }

    @Override
    public List<BankDepositLog> findAll(String userId) {
        return null;
    }


}
