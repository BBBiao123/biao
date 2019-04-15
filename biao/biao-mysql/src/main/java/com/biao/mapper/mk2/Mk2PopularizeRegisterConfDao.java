package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeRegisterConf;
import com.biao.sql.build.mk2.Mk2PopularizeRegisterConfBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface Mk2PopularizeRegisterConfDao {

    @Select("select " + Mk2PopularizeRegisterConfBuild.columns + " from mk2_popularize_register_conf a where a.status = 3 ")
    List<Mk2PopularizeRegisterConf> findConf();

    @Update("update mk2_popularize_register_conf set give_volume = give_volume + #{volume}  where id = #{confId} AND total_volume >= give_volume + #{volume} ")
    int updateConfVolume(@Param("volume") Long volume, @Param("confId") String confId);

}
