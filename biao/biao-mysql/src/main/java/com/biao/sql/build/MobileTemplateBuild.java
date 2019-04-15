package com.biao.sql.build;

import com.biao.entity.MobileTemplate;
import com.biao.sql.BaseSqlBuild;

public class MobileTemplateBuild extends BaseSqlBuild<MobileTemplate, String> {

    public static final String columns = "id,code,access_key,access_secret,sign_name,template_param,template_code,work_sign,remark,expand_json,time_out";

}
