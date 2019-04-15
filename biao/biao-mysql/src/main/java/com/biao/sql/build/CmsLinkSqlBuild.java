package com.biao.sql.build;

import com.biao.entity.CmsLink;
import com.biao.sql.BaseSqlBuild;

public class CmsLinkSqlBuild extends BaseSqlBuild<CmsLink, Integer> {

    public static final String columns = "id,title,image,href,weight,weight_date,del_flag";

}
