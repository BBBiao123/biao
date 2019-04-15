package com.biao.mapper;

import com.biao.entity.SuperBookConf;
import com.biao.sql.SuperBookConfSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SuperBookConfDao {

    @Select("SELECT " + SuperBookConfSqlBuild.columns + " FROM js_plat_super_book_conf t WHERE t.symbol = #{symbol} LIMIT 1 ")
    SuperBookConf findBySymbol(@Param("symbol") String symbol);

    @Update("UPDATE js_plat_super_book_conf SET area_height = #{areaHeight} WHERE symbol = #{symbol} ")
    long updateAreaHeight(SuperBookConf superBookConf);
}
