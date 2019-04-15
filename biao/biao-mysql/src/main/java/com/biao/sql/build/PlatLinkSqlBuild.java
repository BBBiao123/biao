package com.biao.sql.build;

import com.biao.entity.PlatLink;
import com.biao.sql.BaseSqlBuild;

public class PlatLinkSqlBuild extends BaseSqlBuild<PlatLink, Integer> {
    public static final String columns = " id, typeId, linkUrl, linkImage, show_order, createBy, create_date, update_date ";

}
