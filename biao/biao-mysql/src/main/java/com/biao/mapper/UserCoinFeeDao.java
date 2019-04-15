package com.biao.mapper;

import com.biao.entity.UserCoinFee;
import com.biao.sql.build.UserCoinFeeSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * UserCoinFeeDao .
 * ctime: 2018/8/31 15:03
 *
 *  "" sixh
 */
@Mapper
public interface UserCoinFeeDao {

    /**
     * 获取所有的用户手续费信息.
     *
     * @return 用户信息.
     */
    @Select("select " + UserCoinFeeSqlBuild.columns + " from js_plat_user_coin_fee order by update_date")
    List<UserCoinFee> list();
}
