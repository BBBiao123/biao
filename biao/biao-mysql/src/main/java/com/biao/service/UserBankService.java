package com.biao.service;

import com.biao.entity.UserBank;

import java.util.List;
import java.util.Optional;

public interface UserBankService {

    String save(UserBank userBank);

    UserBank findById(String id);

    void updateById(UserBank userBank);

    List<UserBank> findAll(String userId);

    Optional<UserBank> findByUserId(String userId);

    void deleteByUserId(String userId);
}
