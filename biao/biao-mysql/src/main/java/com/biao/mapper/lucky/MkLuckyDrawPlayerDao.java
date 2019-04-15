package com.biao.mapper.lucky;

import com.biao.entity.lucky.MkLuckyDrawPlayer;
import com.biao.sql.build.lucky.MkLuckyDrawPlayerSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MkLuckyDrawPlayerDao {

    @InsertProvider(type = MkLuckyDrawPlayerSqlBuild.class, method = "insert")
    long insert(MkLuckyDrawPlayer mkLuckyDrawPlayer);

    @UpdateProvider(type = MkLuckyDrawPlayerSqlBuild.class, method = "updateById")
    long update(MkLuckyDrawPlayer mkLuckyDrawPlayer);

    @SelectProvider(type = MkLuckyDrawPlayerSqlBuild.class, method = "findById")
    MkLuckyDrawPlayer findById(@Param("id") String id);

    @Select("select " + MkLuckyDrawPlayerSqlBuild.columns + "from mk_lucky_draw_player where user_id = #{userId} order by create_date desc")
    List<MkLuckyDrawPlayer> findByUserId(@Param("userId") String userId);

    @Select("select " + MkLuckyDrawPlayerSqlBuild.columns + "from mk_lucky_draw_player where status = '1' order by draw_date desc")
    List<MkLuckyDrawPlayer> findWinnerList();

    @InsertProvider(type = MkLuckyDrawPlayerSqlBuild.class, method = "batchInsert")
    long batchInsert(@Param("listValues") List<MkLuckyDrawPlayer> list);

    @Select("select " + MkLuckyDrawPlayerSqlBuild.columns + "from mk_lucky_draw_player where status = '1' order by draw_date desc limit 1")
    MkLuckyDrawPlayer findLastWinner();

    @Select("select " + MkLuckyDrawPlayerSqlBuild.columns + "from mk_lucky_draw_player where status = '0' order by create_date desc limit 4")
    List<MkLuckyDrawPlayer> findLastList();


}
