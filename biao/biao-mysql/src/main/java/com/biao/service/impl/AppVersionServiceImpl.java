package com.biao.service.impl;

import com.biao.entity.AppVersion;
import com.biao.mapper.AppVersionDao;
import com.biao.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppVersionServiceImpl implements AppVersionService {

    @Autowired
    private AppVersionDao appVersionDao;

    @Override
    public List<AppVersion> findAll() {
        return appVersionDao.findAll();
    }

    @Override
    public AppVersion getLastestByType(String type) {
        return appVersionDao.getLastestByType(type);
    }
}
