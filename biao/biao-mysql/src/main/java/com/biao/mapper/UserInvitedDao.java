package com.biao.mapper;

import com.biao.entity.UserInvited;
import com.biao.sql.build.UserInvitedSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface UserInvitedDao {

    @InsertProvider(type = UserInvitedSqlBuild.class, method = "insert")
    void insert(UserInvited userInvited);

    @SelectProvider(type = UserInvitedSqlBuild.class, method = "findById")
    UserInvited findById(String id);

    @UpdateProvider(type = UserInvitedSqlBuild.class, method = "updateById")
    long updateById(UserInvited userInvited);


}
