package com.biao.service.impl;

import com.biao.entity.AreaSell;
import com.biao.mapper.AreaSellDao;
import com.biao.service.AreaSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AreaSellServiceImpl implements AreaSellService {

    @Autowired
    private AreaSellDao areaSellDao;

    @Override
    public List<AreaSell> findAll() {
        List<AreaSell> result = areaSellDao.findAll().stream().map(this::change4SellPrice).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<AreaSell> findSale() {
        List<AreaSell> result = areaSellDao.findSale().stream().map(this::change4SellPrice).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<AreaSell> findSold() {
        List<AreaSell> result = areaSellDao.findSold().stream().map(this::change4SellPrice).collect(Collectors.toList());
        return result;
    }

    public AreaSell change4SellPrice(AreaSell source) {
        BigDecimal ratio = new BigDecimal(0.26);
        if (source.getSellPrice() != null && source.getSellPrice().compareTo(BigDecimal.ZERO) == 1) {
            source.setSellPrice(source.getSellPrice().multiply(ratio).setScale(8, BigDecimal.ROUND_DOWN));
            source.setSellPriceLong(source.getSellPrice().longValue());
        } else {
            source.setSellPrice(BigDecimal.ZERO);
            source.setSellPriceLong(0L);
        }
//        source.setSellPrice(BigDecimal.ZERO);
//        source.setSellPriceLong(0L);
        return source;
    }
}
