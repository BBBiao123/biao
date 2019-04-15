package com.biao.service;

import com.biao.entity.AreaSell;

import java.util.List;

public interface AreaSellService {

    List<AreaSell> findAll();

    List<AreaSell> findSale();

    List<AreaSell> findSold();
}
