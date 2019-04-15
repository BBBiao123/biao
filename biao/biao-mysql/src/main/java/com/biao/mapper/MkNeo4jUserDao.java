package com.biao.mapper;

import com.biao.entity.PlatUserNeo4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MkNeo4jUserDao {

    @Select("SELECT u.id AS userId, u.real_name AS realName, u.mail AS mail, u.mobile as mobile, u.refer_id as parentUserId, CONCAT(u.id,'-',u.refer_id) AS fromToKey FROM js_plat_user u ")
    List<PlatUserNeo4j> findAllPlatUser();

    @Select("SELECT t.user_id AS 'userId', t.source_volume AS 'lastBbVolume', t.volume AS 'joinMiningVol' " +
            " FROM mk2_popularize_mining_user_tmp_bak t WHERE t.coin_symbol = #{coinSymbol} AND t.type = '9' ")
    List<PlatUserNeo4j> findAllUserBbVolumes(@Param("coinSymbol") String coinSymbol);

    @Select("SELECT u.id AS userId, u.real_name AS realName, u.mail AS mail, u.mobile as mobile, u.refer_id as parentUserId, CONCAT(u.id,'-',u.refer_id) AS fromToKey " +
            "FROM js_plat_user u WHERE u.create_date > #{beginDate} AND u.create_by is NULL ")
    List<PlatUserNeo4j> findPlatUserByDate(@Param("beginDate") LocalDateTime beginDate);
}
