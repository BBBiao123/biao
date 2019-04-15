package com.biao.service.impl;

import com.biao.entity.ExPair;
import com.biao.mapper.ExPairDao;
import com.biao.service.ExPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExPairServiceImpl implements ExPairService {

    @Autowired
    private ExPairDao exPairDao;


    @Override
    public ExPair findById(String id) {
        return exPairDao.findById(id);
    }

    /**
     * 获取所有的交易对
     *
     * @return List<ExPair>
     */
    @Override
    public List<ExPair> findAll() {
        return exPairDao.findAll();
    }

    /**
     * 根据名称过滤
     *
     * @param coinName 名称
     * @return List<ExPair>
     */
    @Override
    public List<ExPair> findByCoinName(String coinName) {
        return null;
    }

    /**
     * 获取所有已经发布的交易对
     *
     * @return List<ExPair>
     */
    @Override
    public List<ExPair> findByList() {
        return exPairDao.findByList();
    }


}
