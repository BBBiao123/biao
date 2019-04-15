package com.biao.mapper.lucky;

import com.biao.entity.lucky.MkLuckyDrawConfig;
import com.biao.sql.build.lucky.MkLuckyDrawConfigSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Mapper
public interface MkLuckyDrawConfigDao {

    @InsertProvider(type = MkLuckyDrawConfigSqlBuild.class, method = "insert")
    long insert(MkLuckyDrawConfig mkLuckyDrawConfig);

    @UpdateProvider(type = MkLuckyDrawConfigSqlBuild.class, method = "updateById")
    long update(MkLuckyDrawConfig mkLuckyDrawConfig);

    @Update("update mk_lucky_draw_config set pool_volume = #{poolVolume},player_number = #{playerNumber}, fee = #{fee} where id = #{id} and update_date = #{updateDate}")
    long updateConfig(@Param("id") String id, @Param("poolVolume") BigDecimal poolVolume, @Param("playerNumber") Integer playerNumber, @Param("fee") BigDecimal fee, @Param("updateDate") Timestamp updateDate);

    @SelectProvider(type = MkLuckyDrawConfigSqlBuild.class, method = "findById")
    MkLuckyDrawConfig findById(@Param("id") String id);

    @Select("select " + MkLuckyDrawConfigSqlBuild.columns + " from mk_lucky_draw_config where status = 1 limit 1")
    MkLuckyDrawConfig findActiveOne();

    @Select("select " + MkLuckyDrawConfigSqlBuild.columns + " from mk_lucky_draw_config limit 1")
    MkLuckyDrawConfig findOne();

}
