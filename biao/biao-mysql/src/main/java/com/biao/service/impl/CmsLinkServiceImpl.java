package com.biao.service.impl;

import com.biao.entity.CmsLink;
import com.biao.mapper.CmsLinkDao;
import com.biao.service.CmsLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsLinkServiceImpl implements CmsLinkService {

    @Autowired
    private CmsLinkDao cmsLinkDao;


    @Override
    public List<CmsLink> findAll() {
        return cmsLinkDao.findAll();
    }


}
