package com.biao.service;

import com.biao.entity.CmsCategory;

public interface CmsCategoryService {

    CmsCategory findById(String id);

    CmsCategory findByModuleAndKeyword(String module, String keyword);

}
