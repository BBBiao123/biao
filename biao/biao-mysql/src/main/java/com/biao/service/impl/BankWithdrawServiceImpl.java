package com.biao.service.impl;

import com.biao.entity.BankWithdrawLog;
import com.biao.mapper.BankWithdrawDao;
import com.biao.service.BankWithdrawService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankWithdrawServiceImpl implements BankWithdrawService {

    @Autowired
    private BankWithdrawDao bankWithdrawDao;

    @Override
    public String save(BankWithdrawLog bankWithdrawLog) {
        String id = SnowFlake.createSnowFlake().nextIdString();
        bankWithdrawLog.setStatus(0);
        bankWithdrawLog.setCreateDate(LocalDateTime.now());
        bankWithdrawLog.setId(id);
        bankWithdrawDao.insert(bankWithdrawLog);
        return id;
    }

    @Override
    public BankWithdrawLog findById(String id) {
        return bankWithdrawDao.findById(id);
    }

    @Override
    public List<BankWithdrawLog> findAll(String userId) {
        return null;
    }


}
