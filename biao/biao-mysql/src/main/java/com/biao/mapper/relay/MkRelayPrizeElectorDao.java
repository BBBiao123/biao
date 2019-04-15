package com.biao.mapper.relay;

import com.biao.entity.PlatUser;
import com.biao.entity.UserCoinVolume;
import com.biao.entity.relay.MkRelayPrizeElector;
import com.biao.sql.build.PlatUserSqlBuild;
import com.biao.sql.build.UserCoinVolumeSqlBuild;
import com.biao.sql.build.relay.MkRelayPrizeElectorSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MkRelayPrizeElectorDao {

    @InsertProvider(type = MkRelayPrizeElectorSqlBuild.class, method = "insert")
    long insert(MkRelayPrizeElector MkRelayPrizeElector);

    @UpdateProvider(type = MkRelayPrizeElectorSqlBuild.class, method = "updateById")
    long update(MkRelayPrizeElector MkRelayPrizeElector);

    @SelectProvider(type = MkRelayPrizeElectorSqlBuild.class, method = "findById")
    MkRelayPrizeElector findById(@Param("id") String id);

    @Select("select " + UserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_volume v where v.coin_symbol = #{coinSymbol} and v.update_date > #{beginDate} and v.update_date <= #{endDate} and v.volume >= #{minVolume} and NOT EXISTS (select 1 from mk_relay_prize_elector e where v.user_id = e.user_id) order by v.update_date desc")
    List<UserCoinVolume> findEffectiveUser(@Param("coinSymbol") String coinSymbol, @Param("beginDate") LocalDateTime beginDate, @Param("endDate") LocalDateTime endDate, @Param("minVolume") BigDecimal minVolume);

    @Select("select " + MkRelayPrizeElectorSqlBuild.columns + "from mk_relay_prize_elector e where e.reach_date < #{curReachDate} AND NOT EXISTS (select 1 from mk_relay_prize_candidate c where c.status in (0,1) and c.user_id = e.user_id) order by e.reach_date desc limit #{size}")
    List<MkRelayPrizeElector> findPrizeElector(@Param("size") Integer size, @Param("curReachDate") LocalDateTime curReachDate);

    @InsertProvider(type = MkRelayPrizeElectorSqlBuild.class, method = "batchInsert")
    long batchInsert(@Param("listValues") List<MkRelayPrizeElector> list);

    @Select("select " + MkRelayPrizeElectorSqlBuild.columns + "from mk_relay_prize_elector e where e.reach_date > #{curReachDate} AND NOT EXISTS (select 1 from mk_relay_prize_candidate c where c.status in (1) and c.user_id = e.user_id) order by e.reach_date desc limit 4")
    List<MkRelayPrizeElector> findLastListByDate(@Param("curReachDate") LocalDateTime curReachDate, @Param("curDateTime") LocalDateTime curDateTime, @Param("interval") Long interval);

    @Select("select " + MkRelayPrizeElectorSqlBuild.columns + "from mk_relay_prize_elector e where NOT EXISTS (select 1 from mk_relay_prize_candidate c where c.status in (1) and c.user_id = e.user_id) order by e.reach_date desc limit 4")
    List<MkRelayPrizeElector> findLastList(@Param("curDateTime") LocalDateTime curDateTime, @Param("interval") Long interval);

    @Select("select " + PlatUserSqlBuild.columns + " from js_plat_user v where v.tag = #{tag} and NOT EXISTS (select 1 from mk_relay_prize_elector e where v.id = e.user_id) order by v.update_date desc limit 1")
    PlatUser findOneUserByTag(@Param("tag") String tag);

    @Select("select count(1) from js_plat_user v where v.tag = #{tag} and NOT EXISTS (select 1 from mk_relay_prize_elector e where v.id = e.user_id)")
    long countUserByTag(@Param("tag") String tag);

}
