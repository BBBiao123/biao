package com.biao.sql.build;

import com.biao.entity.EmailTemplate;
import com.biao.sql.BaseSqlBuild;

public class EmailTemplateBuild extends BaseSqlBuild<EmailTemplate, Integer> {

    public static final String columns = "id,name,code,business_type,template_subject,template_content,remarks,del_flag,create_by,update_by,create_date,update_date";

}
