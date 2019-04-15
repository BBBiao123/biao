package com.biao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface OfflineCoinVolumeDayDao {

    @Insert("INSERT INTO js_plat_offline_coin_volume_day(mobile,mail,user_id,count_day,buy_total,sell_total,surplus_total,tag,create_time)" +
            "  SELECT u.mobile AS mobile,u.mail AS mail,u.id as user_id,dayTime,TRUNCATE(SUM(v.in_total),2),TRUNCATE(SUM(v.out_total),2),TRUNCATE(SUM(v.in_total) - SUM(v.out_total),2),u.tag,now()" +
            "  FROM (" +
            "  SELECT user_id,DATE_FORMAT(t.confirm_receipt_date, '%Y-%m-%d') AS dayTime,t.total_price AS in_total,'0' AS out_total" +
            "  FROM js_plat_offline_order_detail t" +
            "  WHERE t.`status` = '2' AND remarks='sell' AND symbol IN (${symbols}) AND t.confirm_receipt_date >#{startTime}  AND t.confirm_receipt_date <#{endTime} AND user_id IN (" +
            "  SELECT id FROM js_plat_user WHERE tag = 'FM' OR tag LIKE 'YS%')" +
            "  UNION ALL" +
            "  SELECT user_id,DATE_FORMAT(t.confirm_receipt_date, '%Y-%m-%d') AS dayTime, 0 AS in_total,t.total_price AS out_total" +
            "  FROM js_plat_offline_order_detail t" +
            "  WHERE t.`status` = '2' AND remarks='buy' AND symbol IN (${symbols}) AND t.confirm_receipt_date > #{startTime}  AND t.confirm_receipt_date <#{endTime} AND user_id IN (" +
            "  SELECT id FROM js_plat_user WHERE tag = 'FM'  OR tag LIKE 'YS%')" +
            " ) v,js_plat_user u" +
            " WHERE v.user_id = u.id" +
            " GROUP BY u.id,dayTime")
    Long batchInsertSelect(@Param("symbols") String symbols, @Param("startTime") LocalDate startTime, @Param("endTime") LocalDate endTime);


    @Select("select count(user_id) from js_plat_offline_coin_volume_day where DATE_FORMAT(create_time, '%Y-%m-%d') = #{countDay}")
    Long selectByDay(String countDay);
}
