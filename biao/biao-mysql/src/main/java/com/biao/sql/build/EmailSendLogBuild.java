package com.biao.sql.build;

import com.biao.entity.EmailSendLog;
import com.biao.sql.BaseSqlBuild;

public class EmailSendLogBuild extends BaseSqlBuild<EmailSendLog, Integer> {

    public static final String columns = "id,email,subject,content,senddata,response_date,business_type,del_flag,status,expire_time,msg,remarks,create_by,update_by,create_date,update_date";

}
