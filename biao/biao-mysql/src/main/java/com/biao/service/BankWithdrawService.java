package com.biao.service;

import com.biao.entity.BankWithdrawLog;

import java.util.List;

public interface BankWithdrawService {

    String save(BankWithdrawLog bankWithdrawLog);

    BankWithdrawLog findById(String id);

    List<BankWithdrawLog> findAll(String userId);
}
