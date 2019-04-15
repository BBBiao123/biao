package com.biao.service;

import com.biao.entity.ExPair;

import java.util.List;

public interface ExPairService {


    ExPair findById(String id);


    /**
     * 获取所有的交易对
     *
     * @return List<ExPair>
     */
    List<ExPair> findAll();

    /**
     * 根据名称过滤
     *
     * @param coinName 名称
     * @return List<ExPair>
     */
    List<ExPair> findByCoinName(String coinName);


    /**
     * 获取所有已经发布的交易对
     *
     * @return List<ExPair>
     */
    List<ExPair> findByList();


}
