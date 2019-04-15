package com.biao.service.impl;

import com.biao.entity.CmsComment;
import com.biao.mapper.CmsCommentDao;
import com.biao.service.CmsCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmsCommentServiceImpl implements CmsCommentService {

    @Autowired
    private CmsCommentDao cmsCommentDao;


    @Override
    public CmsComment findById(String id) {
        return cmsCommentDao.findById(id);
    }


}
