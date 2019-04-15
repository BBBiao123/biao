package com.biao.mapper;

import com.biao.entity.BankWithdrawLog;
import com.biao.sql.build.BankWithdrawSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface BankWithdrawDao {

    @InsertProvider(type = BankWithdrawSqlBuild.class, method = "insert")
    void insert(BankWithdrawLog bankWithdrawLog);

    @SelectProvider(type = BankWithdrawSqlBuild.class, method = "findById")
    BankWithdrawLog findById(String id);

    @UpdateProvider(type = BankWithdrawSqlBuild.class, method = "updateById")
    long updateById(BankWithdrawLog bankWithdrawLog);


}
