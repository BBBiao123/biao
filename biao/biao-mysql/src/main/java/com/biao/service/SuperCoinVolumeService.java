package com.biao.service;

import com.biao.entity.SuperCoinVolume;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SuperCoinVolumeService {

    SuperCoinVolume findById(String id);

    void updateById(SuperCoinVolume offlineCoinVolume);

    List<SuperCoinVolume> findAll(String userId);

    void in(String userId, String coinId, BigDecimal volume, String symbol);

    void out(String userId, String coinId, BigDecimal volume, String symbol);

    void handleExpireAccount(LocalDateTime expireDate);


	}
