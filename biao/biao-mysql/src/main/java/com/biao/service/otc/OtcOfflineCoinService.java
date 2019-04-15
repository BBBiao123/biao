package com.biao.service.otc;

import com.biao.entity.otc.OtcOfflineCoin;
import com.biao.pojo.GlobalMessageResponseVo;

import java.math.BigDecimal;
import java.util.List;

public interface OtcOfflineCoinService {
    List<OtcOfflineCoin> findAll();

    List<OtcOfflineCoin> findAllByPublishSource(String publishSource);

    OtcOfflineCoin findByCoinId(String coinId);

    GlobalMessageResponseVo checkVolumeAndPrice(String coinId, BigDecimal price, BigDecimal volume, Boolean isCheckVolume);

//    void inOut(String userId, String coinId, BigDecimal volume, String symbol, String from, String to);
}
