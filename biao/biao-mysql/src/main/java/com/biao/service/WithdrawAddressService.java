package com.biao.service;

import com.biao.entity.WithdrawAddress;

import java.util.List;

public interface WithdrawAddressService {

    WithdrawAddress findById(String id);

    String save(WithdrawAddress withdrawAddress);

    void updateById(WithdrawAddress withdrawAddress);

    void updateStatusByUserIdAndId(String status, String userId, String id);

    List<WithdrawAddress> findAll(String userId);

    List<WithdrawAddress> findByUserIdAndCoinId(String userId, String id);

    List<WithdrawAddress> findByUserIdAndType(String userId, String type);
}
