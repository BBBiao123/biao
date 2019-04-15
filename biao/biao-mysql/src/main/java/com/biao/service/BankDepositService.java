package com.biao.service;

import com.biao.entity.BankDepositLog;

import java.util.List;

public interface BankDepositService {

    String save(BankDepositLog bankDepositLog);

    BankDepositLog findById(String id);

    List<BankDepositLog> findAll(String userId);

}
