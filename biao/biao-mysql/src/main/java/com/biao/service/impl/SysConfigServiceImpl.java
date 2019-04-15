package com.biao.service.impl;

import com.biao.entity.SysConfig;
import com.biao.mapper.SysConfDao;
import com.biao.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfDao sysConfDao;

    @Override
    public List<SysConfig> findAll() {
        return sysConfDao.findAll();
    }

    @Override
    public SysConfig getOne() {
        return sysConfDao.getOne();
    }
}
