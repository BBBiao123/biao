package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeMiningConf;
import com.biao.sql.build.mk2.Mk2PopularizeMiningConfBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface Mk2PopularizeMiningConfDao {

    @Select("SELECT " + Mk2PopularizeMiningConfBuild.columns + " FROM mk2_popularize_mining_conf t WHERE t.type = #{type} AND t.status = '3' ORDER BY t.create_date DESC LIMIT 1 ")
    Mk2PopularizeMiningConf findByType(String type);

    @Update("UPDATE mk2_popularize_mining_conf SET grant_volume = #{giveVolume}, show_multiple = delay_show_multiple, show_grant_volume = #{showGrantVolume}  WHERE id = #{id} ")
    long updateMiningConf(@Param("giveVolume") BigDecimal giveVolume, @Param("id") String id, @Param("showGrantVolume") BigDecimal showGrantVolume);
}
