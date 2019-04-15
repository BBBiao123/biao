package com.biao.service.impl;

import com.biao.entity.CmsCategory;
import com.biao.mapper.CmsCategoryDao;
import com.biao.service.CmsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmsCategoryServiceImpl implements CmsCategoryService {

    @Autowired
    private CmsCategoryDao cmsCategoryDao;


    @Override
    public CmsCategory findById(String id) {
        return cmsCategoryDao.findById(id);
    }

    @Override
    public CmsCategory findByModuleAndKeyword(String module, String keyword) {
        return cmsCategoryDao.findByModuleAndKeyword(module, keyword);
    }


}
