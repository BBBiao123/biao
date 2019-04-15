package com.biao.sql.build;

import com.biao.entity.CmsComment;
import com.biao.sql.BaseSqlBuild;

public class CmsCommentSqlBuild extends BaseSqlBuild<CmsComment, Integer> {

    public static final String columns = "id,category_id,article_id,name,ip,content,audit_user_id,del_flag,create_date,update_date";

}
