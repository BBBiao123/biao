package com.biao.service;

import com.biao.entity.OfflineCoinVolume;
import com.biao.entity.otc.OtcOfflineOrder;
import com.biao.entity.otc.OtcOfflineOrderDetail;

import java.math.BigDecimal;
import java.util.List;

public interface OfflineCoinVolumeService {

    OfflineCoinVolume findById(String id);

    void updateById(OfflineCoinVolume offlineCoinVolume);

    List<OfflineCoinVolume> findAll(String userId);

    void in(String userId, String coinId, BigDecimal volume, String symbol);

    void out(String userId, String coinId, BigDecimal volume, String symbol);

    List<OfflineCoinVolume> findAll();

    OfflineCoinVolume findByUserIdAndCoinId(String userId, String coinId);

    void bailIn(String id, String coinId, BigDecimal volume, String symbol, String loginSource);

    void bailOut(String id, String coinId, BigDecimal volume, String symbol);

    void otcOrderSaveUpdateCoinVolume(OtcOfflineOrder otcOfflineOrder, String batchNo);

    void otcDetailSaveUpdateCoinVolume(OtcOfflineOrderDetail sellOrderDetail, OtcOfflineOrder otcOfflineOrder, String batchNo);

    void inOut(String id, String coinId, BigDecimal volume, String symbol, String from, String to, String loginSource);

    void coinVolumeAdd(String userId, String coinId, String symbol, BigDecimal addVolume, String batchNo);

    void coinVolumeSubtract(String userId, String coinId, BigDecimal subtractVolume, String batchNo);

	void coinVolumeSubBailSub(String userId, String coinId, BigDecimal subVolume, BigDecimal buyerSubtractFee, String batchNo);

	void coinVolumeAddBailSub(String userId, String coinId, String symbol, BigDecimal subVolume, BigDecimal buyerSubtractFee, String batchNo);
	}
