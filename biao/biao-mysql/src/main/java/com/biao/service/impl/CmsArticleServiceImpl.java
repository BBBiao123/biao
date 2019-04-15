package com.biao.service.impl;

import com.biao.entity.CmsArticle;
import com.biao.entity.CmsCategory;
import com.biao.mapper.CmsArticleDao;
import com.biao.pojo.ResponsePage;
import com.biao.service.CmsArticleService;
import com.biao.service.CmsCategoryService;
import com.biao.vo.CmsArticleVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsArticleServiceImpl implements CmsArticleService {

    @Autowired
    private CmsArticleDao cmsArticleDao;

    @Autowired
    private CmsCategoryService cmsCategoryService;

    @Override
    public CmsArticle findById(String id) {
        return cmsArticleDao.findById(id);
    }

    @Override
    public List<CmsArticle> findAll(String categoryId, String language) {
        return cmsArticleDao.findAll(categoryId, language);
    }

    @Override
    public ResponsePage<CmsArticle> findPageByCatetoryId(CmsArticleVO cmsArticleVO) {
        ResponsePage<CmsArticle> responsePage = new ResponsePage<>();
        Page<CmsArticle> page = PageHelper.startPage(cmsArticleVO.getCurrentPage(), cmsArticleVO.getShowCount());
        List<CmsArticle> data = cmsArticleDao.findAll(cmsArticleVO.getCategoryId(), cmsArticleVO.getLanguage());
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    @Override
    public ResponsePage<CmsArticle> findPageByModuleAndKeyword(CmsArticleVO cmsArticleVO) {
        ResponsePage<CmsArticle> responsePage = new ResponsePage<>();
        CmsCategory cmsCategory = cmsCategoryService.findByModuleAndKeyword(cmsArticleVO.getModule(), cmsArticleVO.getKeyword());
        if (cmsCategory != null) {
            Page<CmsArticle> page = PageHelper.startPage(cmsArticleVO.getCurrentPage(), cmsArticleVO.getShowCount());
            List<CmsArticle> data = null;
            if (StringUtils.isNotBlank(cmsArticleVO.getDescription())) {
                data = cmsArticleDao.findList(cmsCategory.getId(), cmsArticleVO.getLanguage(), cmsArticleVO.getDescription());
            } else {
                data = cmsArticleDao.findAll(cmsCategory.getId(), cmsArticleVO.getLanguage());
            }
            responsePage.setCount(page.getTotal());
            responsePage.setList(data);
        }
        return responsePage;
    }
}
