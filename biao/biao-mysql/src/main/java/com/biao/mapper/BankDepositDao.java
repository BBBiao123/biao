package com.biao.mapper;

import com.biao.entity.BankDepositLog;
import com.biao.sql.build.BankDepositSqlBuild;
import com.biao.sql.build.BankWithdrawSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface BankDepositDao {

    @InsertProvider(type = BankDepositSqlBuild.class, method = "insert")
    void insert(BankDepositLog bankDepositLog);

    @SelectProvider(type = BankWithdrawSqlBuild.class, method = "findById")
    BankDepositLog findById(String id);

    @UpdateProvider(type = BankWithdrawSqlBuild.class, method = "updateById")
    long updateById(BankDepositLog bankDepositLog);


}
