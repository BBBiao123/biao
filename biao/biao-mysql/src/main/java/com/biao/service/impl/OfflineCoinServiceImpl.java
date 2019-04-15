package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.OfflineCoin;
import com.biao.mapper.OfflineCoinDao;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.OfflineCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class OfflineCoinServiceImpl implements OfflineCoinService {

    @Autowired(required = false)
    private OfflineCoinDao offlineCoinDao;

    @Override
    public List<OfflineCoin> findAll() {
        return offlineCoinDao.findAll();
    }

    @Override
    public OfflineCoin findByCoinId(String coinId) {
        return offlineCoinDao.findByCoinId(coinId);
    }

    @Override
    public GlobalMessageResponseVo checkVolumeAndPrice(String coinId, BigDecimal price,
                                                       BigDecimal volume, Boolean isCheckVolume) {
        final OfflineCoin offlineCoin = this.findByCoinId(coinId);
        //检查价格 数量是否符合规则
        if (Objects.isNull(offlineCoin)) {
            return GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR, "当前币种未启用!是否非法攻击?");
        }
        if (Objects.nonNull(price)) {
            final BigDecimal maxPrice = offlineCoin.getMaxPrice();
            final BigDecimal minPrice = offlineCoin.getMinPrice();
            final Integer pointPrice = offlineCoin.getPointPrice();
            if (price.compareTo(maxPrice) > 0 || price.compareTo(minPrice) < 0) {
                return GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR,
                        "当前输入的价格不在限制范围，请您重新输入!");
            }
            //判断位数
            final int digits = getNumberDecimalDigits(price);
            if (digits > pointPrice) {
                return GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR,
                        "当前输入的价格精度超过限制位数，请您重新输入!");
            }
        }

        final BigDecimal maxVolume = offlineCoin.getMaxVolume();
        final BigDecimal minVolume = offlineCoin.getMinVolume();
        final Integer pointVolume = offlineCoin.getPointVolume();
        if (isCheckVolume) {
            if (volume.compareTo(maxVolume) > 0 || volume.compareTo(minVolume) < 0) {
                return GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR, "当前输入的数量不在限制范围，请您重新输入!");
            }
        }
        //判断位数
        final int digitsV = getNumberDecimalDigits(volume);
        if (digitsV > pointVolume) {
            return GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR, "当前输入的数量精度超过限制位数，请您重新输入!");
        }
        return GlobalMessageResponseVo.newSuccessInstance("成功!");
    }

    private int getNumberDecimalDigits(BigDecimal bigDecimal) {
        int dcimalDigits = 0;
        String balanceStr = bigDecimal.toString();
        int indexOf = balanceStr.indexOf(".");
        if (indexOf > 0) {
            dcimalDigits = balanceStr.length() - 1 - indexOf;
        }
        return dcimalDigits;
    }

    @Override
    public List<OfflineCoin> findAccountAll() {
        return offlineCoinDao.findAccountAll();
    }

    @Override
    public OfflineCoin findByCoinIdForChange(String coinId) {
        return offlineCoinDao.findByCoinIdForChange(coinId);
    }
}
