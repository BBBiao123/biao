package com.biao.sql.build;

import com.biao.entity.CmsCategory;
import com.biao.sql.BaseSqlBuild;

public class CmsCategorySqlBuild extends BaseSqlBuild<CmsCategory, Integer> {

    public static final String columns = "id,name,image,keywords,description,remarks,create_by,update_by,create_date,update_date";

}
