package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeRegisterCoinBak;
import com.biao.sql.build.mk2.Mk2PopularizeRegisterCoinBakBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface Mk2PopularizeRegisterCoinBakDao {

    @InsertProvider(type = Mk2PopularizeRegisterCoinBakBuild.class, method = "insert")
    void insert(Mk2PopularizeRegisterCoinBak coin);

    @InsertProvider(type = Mk2PopularizeRegisterCoinBakBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<Mk2PopularizeRegisterCoinBak> coins);

    @Select("SELECT " + Mk2PopularizeRegisterCoinBakBuild.columns + " FROM mk2_popularize_register_coin_bak WHERE t.success_status = '0' LIMIT #{count}")
    List<Mk2PopularizeRegisterCoinBak> findBakBatch(int count);

//    @Update("UPDATE mk2_popularize_register_coin_bak SET success_status = '1' WHERE id = #{id} ")
//    long updateFindBakSuccess(String id);

    @Select("SELECT DISTINCT(t.user_id) from mk2_popularize_register_coin_bak t where t.`success_status` = 0 ")
    List<String> distinctUsers();

    @Update("UPDATE mk2_popularize_register_coin_bak SET success_status = '1' WHERE user_id = #{userId} ")
    long updateSuccessByUserId(String userId);

    @Select("SELECT SUM(t.volume) FROM mk2_popularize_register_coin_bak t where t.user_id = #{userId} AND t.success_status = '0' ")
    long sumUserVol(String userId);

    @Delete("DELETE FROM mk2_popularize_register_coin WHERE create_date >= '2018-08-10 05:00:00' AND create_date < '2018-08-10 20:00:00' ")
    long deleteRepairCoin();
}
