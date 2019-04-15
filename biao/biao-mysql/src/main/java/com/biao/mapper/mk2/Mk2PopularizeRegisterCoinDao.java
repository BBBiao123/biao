package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeRegisterCoin;
import com.biao.sql.build.mk2.Mk2PopularizeRegisterCoinBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface Mk2PopularizeRegisterCoinDao {

    @Select("select " + Mk2PopularizeRegisterCoinBuild.columns + " from mk2_popularize_register_coin a where a.user_id = #{userId} ")
    Mk2PopularizeRegisterCoin findBy(String userId);

    @Select("select " + Mk2PopularizeRegisterCoinBuild.columns + " from mk2_popularize_register_coin a where a.status = 1 AND a.create_date >= CONCAT(#{date},' 00:00:00') AND a.create_date <= CONCAT(#{date},' 23:59:59') ")
    List<Mk2PopularizeRegisterCoin> findTransfer(String date);

    @InsertProvider(type = Mk2PopularizeRegisterCoinBuild.class, method = "insert")
    void insert(Mk2PopularizeRegisterCoin coin);

    @InsertProvider(type = Mk2PopularizeRegisterCoinBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<Mk2PopularizeRegisterCoin> coins);

    @Update("update mk2_popularize_register_coin set status = 2 where id = #{id} AND status = 1 ")
    int updateTransferSuccess(String id);

    @Select("SELECT COUNT(1) from  mk2_popularize_register_coin t where t.user_id = #{userId} and t.for_user_id is null ")
    long findHadRegisterGive(String userId);


    /////////////////////////重复送币补救///////////////////////////////////////////////////

    @Delete("delete from mk2_popularize_register_coin where id = #{id} ")
    long deleteRegisterCoinById(String id);

    @Select("SELECT t.user_id from mk2_popularize_register_coin t where t.for_user_id is null group by t.user_id  HAVING count(t.user_id) > 1 ")
    List<String> findRepeatRegisterGives();

    @Select("SELECT " + Mk2PopularizeRegisterCoinBuild.columns + " FROM mk2_popularize_register_coin t WHERE t.user_id = #{userId} AND t.for_user_id IS NULL order by t.create_date asc ")
    List<Mk2PopularizeRegisterCoin> findRepeatRegisters(String userId);

    @Select("SELECT t.for_user_id FROM mk2_popularize_register_coin t WHERE t.for_user_id IS NOT NULL GROUP BY t.for_user_id HAVING count(t.for_user_id) > 1 ")
    List<String> findRepeatReferGives();

    @Select("SELECT " + Mk2PopularizeRegisterCoinBuild.columns + " FROM mk2_popularize_register_coin t WHERE t.for_user_id = #{forUserId} order by t.create_date asc ")
    List<Mk2PopularizeRegisterCoin> findRepeatRefers(String forUserId);

    @Select("SELECT DISTINCT(t.user_id) from mk2_popularize_register_coin t where t.for_user_id = #{forUserId} ")
    List<String> repeatUserIdByForUserId(String forUserId);

    @Select("SELECT COUNT(t.volume) FROM mk2_popularize_register_coin t where t.status = '2' ")
    long countGiveCoinTotal();

}
