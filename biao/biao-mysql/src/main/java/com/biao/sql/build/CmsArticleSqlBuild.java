package com.biao.sql.build;

import com.biao.entity.CmsArticle;
import com.biao.sql.BaseSqlBuild;

public class CmsArticleSqlBuild extends BaseSqlBuild<CmsArticle, Integer> {

    public static final String columns = "id,category_id,title,link,image,keywords,description,hits,remarks,language,create_by,update_by,create_date,update_date";

}
