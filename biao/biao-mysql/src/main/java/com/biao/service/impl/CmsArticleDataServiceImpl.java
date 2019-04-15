package com.biao.service.impl;

import com.biao.entity.CmsArticleData;
import com.biao.mapper.CmsArticleDataDao;
import com.biao.service.CmsArticleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmsArticleDataServiceImpl implements CmsArticleDataService {

    @Autowired
    private CmsArticleDataDao cmsArticleDataDao;

    @Override
    public CmsArticleData findById(String id) {
        return cmsArticleDataDao.findById(id);
    }


}
