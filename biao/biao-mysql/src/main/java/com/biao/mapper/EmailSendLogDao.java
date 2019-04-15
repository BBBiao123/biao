package com.biao.mapper;

import com.biao.entity.EmailSendLog;
import com.biao.sql.build.EmailSendLogBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailSendLogDao {

    @InsertProvider(type = EmailSendLogBuild.class, method = "insert")
    void insert(EmailSendLog emailSendLog);

}
