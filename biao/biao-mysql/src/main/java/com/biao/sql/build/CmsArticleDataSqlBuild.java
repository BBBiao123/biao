package com.biao.sql.build;

import com.biao.entity.CmsArticleData;
import com.biao.sql.BaseSqlBuild;

public class CmsArticleDataSqlBuild extends BaseSqlBuild<CmsArticleData, Integer> {

    public static final String columns = "id,content,copyfrom,relation,allow_comment";

}
