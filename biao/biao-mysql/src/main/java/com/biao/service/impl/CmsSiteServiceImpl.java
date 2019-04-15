package com.biao.service.impl;

import com.biao.entity.CmsSite;
import com.biao.mapper.CmsSiteDao;
import com.biao.service.CmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cmsSiteService")
public class CmsSiteServiceImpl implements CmsSiteService {

    @Autowired
    private CmsSiteDao cmsSiteDao;

    public CmsSite findCmsSite() {
        return cmsSiteDao.findCmsSite();
    }
}
