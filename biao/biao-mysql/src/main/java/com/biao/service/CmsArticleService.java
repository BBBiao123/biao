package com.biao.service;

import com.biao.entity.CmsArticle;
import com.biao.pojo.ResponsePage;
import com.biao.vo.CmsArticleVO;

import java.util.List;

public interface CmsArticleService {

    CmsArticle findById(String id);

    List<CmsArticle> findAll(String catetoryId, String language);

    ResponsePage<CmsArticle> findPageByCatetoryId(CmsArticleVO cmsArticleVO);

    ResponsePage<CmsArticle> findPageByModuleAndKeyword(CmsArticleVO cmsArticleVO);
}
