package com.biao.service;

import com.biao.entity.OfflineCoin;
import com.biao.pojo.GlobalMessageResponseVo;

import java.math.BigDecimal;
import java.util.List;

public interface OfflineCoinService {

    List<OfflineCoin> findAll();

    OfflineCoin findByCoinId(String coinId);

    GlobalMessageResponseVo checkVolumeAndPrice(String coinId, BigDecimal price, BigDecimal volume, Boolean isCheckVolume);

    List<OfflineCoin> findAccountAll();

    OfflineCoin findByCoinIdForChange(String coinId);


}
