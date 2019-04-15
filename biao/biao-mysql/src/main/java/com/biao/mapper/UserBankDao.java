package com.biao.mapper;

import com.biao.entity.UserBank;
import com.biao.sql.build.UserBankSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserBankDao {

    @InsertProvider(type = UserBankSqlBuild.class, method = "insert")
    void insert(UserBank userBank);

    @SelectProvider(type = UserBankSqlBuild.class, method = "findById")
    UserBank findById(String id);

    @UpdateProvider(type = UserBankSqlBuild.class, method = "updateById")
    long updateById(UserBank userBank);

    @Select("select 1 from js_plat_user_bank where card_no = #{cardNo} limit 1")
    Long findExistByCardNo(@Param("cardNo") String cardNo);

    @Select("select 1 from js_plat_user_bank where mobile = #{mobile} limit 1")
    Long findExistByMobile(@Param("mobile") String mobile);

    @Select("select " + UserBankSqlBuild.columns + " from js_plat_user_bank where user_id = #{userId}")
    List<UserBank> findAll(@Param("userId") String userId);

    @Select("select " + UserBankSqlBuild.columns + " from js_plat_user_bank where user_id = #{userId} limit 1")
    UserBank findByUserId(@Param("userId") String userId);

    @Delete("delete from js_plat_user_bank where user_id = #{userId}")
    long deleteByUserId(@Param("userId") String userId);

    long update(UserBank userBank);
}
