package com.biao.service.impl;

import com.biao.entity.PlatLink;
import com.biao.mapper.PlatLinkDao;
import com.biao.service.PlatLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatLinkServiceImpl implements PlatLinkService {

    @Autowired
    private PlatLinkDao platLinkDao;

    @Override
    public List<PlatLink> findAll() {
        return platLinkDao.findAll();
    }
}
