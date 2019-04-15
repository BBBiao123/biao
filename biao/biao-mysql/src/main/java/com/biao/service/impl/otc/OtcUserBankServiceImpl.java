package com.biao.service.impl.otc;

import com.biao.constant.Constants;
import com.biao.entity.otc.OtcOfflineCurrency;
import com.biao.entity.otc.OtcUserBank;
import com.biao.exception.PlatException;
import com.biao.mapper.otc.OtcOfflineCurrencyDao;
import com.biao.mapper.otc.OtcUserBankDao;
import com.biao.service.otc.OtcUserBankService;
import com.biao.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OtcUserBankServiceImpl implements OtcUserBankService {

    @Autowired
    private OtcUserBankDao otcUserBankDao;

    @Autowired
    private OtcOfflineCurrencyDao otcOfflineCurrencyDao;

    public List<OtcUserBank> findByParam(OtcUserBank otcUserBank) {
        return otcUserBankDao.findByParam(otcUserBank);
    }

    @Transactional
    public String save(OtcUserBank otcUserBank) {
        checkCurrency(otcUserBank.getSupportCurrencyCode()); // 校验银行卡支持币种是否合法
        if (StringUtils.isNotBlank(otcUserBank.getId())) {
            otcUserBank.setUpdateDate(LocalDateTime.now());
            long count = otcUserBankDao.updateById(otcUserBank);
            if (count != 1) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
        } else {
            otcUserBank.setId(SnowFlake.createSnowFlake().nextIdString());
            otcUserBank.setStatus("1");
            otcUserBank.setCreateDate(LocalDateTime.now());
            otcUserBank.setUpdateDate(LocalDateTime.now());
            otcUserBankDao.insert(otcUserBank);
        }
        return otcUserBank.getId();
    }

    public List<OtcUserBank> findByUserIdAndStatus(String userId, String status) {
        return otcUserBankDao.findByUserIdAndStatus(userId, status);
    }

    private void checkCurrency(String currencyCodes) {
        if (StringUtils.isBlank(currencyCodes)) {
            throw new PlatException(Constants.UPDATE_ERROR, "银行卡支持币种不能为空");
        }
        String[] currencyCodeArray = currencyCodes.split(",");
        OtcOfflineCurrency currency = null;
        List<OtcOfflineCurrency> otcOfflineCurrencies = otcOfflineCurrencyDao.findAll(); // 查询系统支持所有法币币种
        Map<String, OtcOfflineCurrency> mapCurrencys = new HashMap<>();
        otcOfflineCurrencies.forEach(e -> {
            mapCurrencys.put(e.getCode(), e);
        });
        for (String currencyCode : currencyCodeArray) {
            if (Objects.isNull(currencyCode) || !mapCurrencys.containsKey(currencyCode)) {
                throw new PlatException(Constants.UPDATE_ERROR, "银行卡支持币种设置错误");
            }
        }
    }
}
