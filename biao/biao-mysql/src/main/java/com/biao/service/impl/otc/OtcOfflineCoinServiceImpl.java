package com.biao.service.impl.otc;

import com.biao.constant.Constants;
import com.biao.entity.otc.OtcOfflineCoin;
import com.biao.mapper.otc.OtcOfflineCoinDao;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.otc.OtcOfflineCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class OtcOfflineCoinServiceImpl implements OtcOfflineCoinService {

    @Autowired(required = false)
    private OtcOfflineCoinDao otcOfflineCoinDao;

    /**
     * 默认查询Plat C2C的可交易币种（即publish_source为空）
     *
     * @return
     */
    @Override
    public List<OtcOfflineCoin> findAll() {
        return otcOfflineCoinDao.findAll();
    }

    @Override
    public List<OtcOfflineCoin> findAllByPublishSource(String publishSource) {
        return otcOfflineCoinDao.findAllByPublishSource(publishSource);
    }

    @Override
    public OtcOfflineCoin findByCoinId(String coinId) {
        return otcOfflineCoinDao.findByCoinId(coinId);
    }

    @Override
    public GlobalMessageResponseVo checkVolumeAndPrice(String coinId, BigDecimal price,
                                                       BigDecimal volume, Boolean isCheckVolume) {
        final OtcOfflineCoin offlineCoin = this.findByCoinId(coinId);
        //检查价格 数量是否符合规则
        if (Objects.isNull(offlineCoin)) {
            return GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR, "当前币种未启用！是否非法攻击？");
        }
//        if (Objects.nonNull(price)) {
//            final BigDecimal maxPrice = offlineCoin.getMaxPrice();
//            final BigDecimal minPrice = offlineCoin.getMinPrice();
//            final Integer pointPrice = offlineCoin.getPointPrice();
//            if (price.compareTo(maxPrice) > 0 || price.compareTo(minPrice) < 0) {
//                return GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR,
//                        "当前输入的价格不在限制范围，请您重新输入！");
//            }
//            //判断位数
//            final int digits = getNumberDecimalDigits(price);
//            if (digits > pointPrice) {
//                return GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR,
//                        "当前输入的价格精度超过限制位数，请您重新输入！");
//            }
//        }
//
        final BigDecimal maxVolume = offlineCoin.getMaxVolume();
        final BigDecimal minVolume = offlineCoin.getMinVolume();
        if (isCheckVolume) {
            if (volume.compareTo(maxVolume) > 0 || volume.compareTo(minVolume) < 0) {
                return GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR, "当前输入的数量不在限制范围，请您重新输入！");
            }
        }
        //判断位数
        final Integer pointVolume = offlineCoin.getPointVolume();
        final int digitsV = getNumberDecimalDigits(volume);
        if (digitsV > pointVolume) {
            return GlobalMessageResponseVo.newInstance(Constants.OPERRATION_ERROR, "当前输入的数量精度超过限制位数，请您重新输入！");
        }
        return GlobalMessageResponseVo.newSuccessInstance("成功！");
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
}
