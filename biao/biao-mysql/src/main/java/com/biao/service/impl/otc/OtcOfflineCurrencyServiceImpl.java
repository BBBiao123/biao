package com.biao.service.impl.otc;

import com.biao.entity.otc.OtcOfflineCurrency;
import com.biao.mapper.otc.OtcOfflineCurrencyDao;
import com.biao.service.otc.OtcOfflineCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OtcOfflineCurrencyServiceImpl implements OtcOfflineCurrencyService {

    @Autowired
    private OtcOfflineCurrencyDao otcOfflineCurrencyDao;

    @Override
    public List<OtcOfflineCurrency> findAll() {
        return otcOfflineCurrencyDao.findAll();
    }
}
