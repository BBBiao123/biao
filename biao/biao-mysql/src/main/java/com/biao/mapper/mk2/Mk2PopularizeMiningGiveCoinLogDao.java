package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeMiningGiveCoinLog;
import com.biao.sql.build.mk2.Mk2PopularizeMiningGiveCoinLogBuild;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface Mk2PopularizeMiningGiveCoinLogDao {

    @InsertProvider(type = Mk2PopularizeMiningGiveCoinLogBuild.class, method = "insert")
    void insert(Mk2PopularizeMiningGiveCoinLog coinLog);

    @InsertProvider(type = Mk2PopularizeMiningGiveCoinLogBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<Mk2PopularizeMiningGiveCoinLog> coinLogs);

    @Select("select " + Mk2PopularizeMiningGiveCoinLogBuild.columns + " from mk2_popularize_mining_give_coin_log where type = 1 and user_id = #{userId} and DATE(create_date) = DATE(#{createDate}) limit 1")
    Mk2PopularizeMiningGiveCoinLog findByDate(@Param("createDate") LocalDateTime createDate, @Param("userId") String userId);

    @Select("select count(*) from mk2_popularize_mining_give_coin_log where type = 1 and user_id = #{userId} and create_date <= #{endDateTime}")
    long countByUserId(@Param("userId") String userId, @Param("endDateTime") LocalDateTime endDateTime);

    @Select("select id from mk2_popularize_mining_give_coin_log where type = #{type} and user_id = #{userId} and count_date = (#{countDate}) limit 1")
    String findIdByUserId(@Param("userId") String userId, @Param("type") String type, @Param("countDate") LocalDate countDate);

    @Delete("DELETE FROM mk2_popularize_mining_give_coin_log WHERE count_date = #{countDate} AND volume <= 0 ")
    long deleteZeroGiveCoin(@Param("countDate") LocalDateTime countDate);

    @Select("SELECT " + Mk2PopularizeMiningGiveCoinLogBuild.columns + " FROM mk2_popularize_mining_give_coin_log t WHERE t.count_date = #{countDate} AND t.status = '0' AND t.volume > 0 ")
    List<Mk2PopularizeMiningGiveCoinLog> findByCountDate(@Param("countDate") LocalDateTime countDate);

    @Update("UPDATE mk2_popularize_mining_give_coin_log SET status = '1' WHERE id = #{giveLogId} AND status = '0' ")
    long updateGiveMiningStatus(@Param("giveLogId") String giveLogId);

    @Select("SELECT " + Mk2PopularizeMiningGiveCoinLogBuild.columns + " FROM mk2_popularize_mining_give_coin_log t WHERE t.user_id = #{userId} AND t.type = #{type} AND t.count_date = #{countDate} LIMIT 1")
    Mk2PopularizeMiningGiveCoinLog findByUserIdTypeDate(@Param("userId") String userId, @Param("type") String type, @Param("countDate") LocalDateTime countDate);

    @Select("<script>" +
            "SELECT tx_hash,area_height,in_address,out_address,volume,coin_symbol,count_date FROM mk2_popularize_mining_give_coin_log t " +
            " <if test=\"address != null and address != ''\"> " +
            "   WHERE t.in_address = #{address} OR out_address = #{address} " +
            " </if>" +
            " <if test=\"address == null or address == ''\"> " +
            "   WHERE IFNULL(t.in_address,'') != '' AND IFNULL(t.out_address,'') != '' " +
            " </if>" +
            "ORDER BY t.area_height DESC " +
            "</script>")
    List<Mk2PopularizeMiningGiveCoinLog> findByAddress(@Param("address") String address);
}
