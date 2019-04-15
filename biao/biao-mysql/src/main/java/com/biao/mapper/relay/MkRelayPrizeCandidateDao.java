package com.biao.mapper.relay;

import com.biao.entity.relay.MkRelayPrizeCandidate;
import com.biao.sql.build.relay.MkRelayPrizeCandidateSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MkRelayPrizeCandidateDao {

    @InsertProvider(type = MkRelayPrizeCandidateSqlBuild.class, method = "insert")
    long insert(MkRelayPrizeCandidate MkRelayPrizeCandidate);

    @UpdateProvider(type = MkRelayPrizeCandidateSqlBuild.class, method = "updateById")
    long update(MkRelayPrizeCandidate MkRelayPrizeCandidate);

    @SelectProvider(type = MkRelayPrizeCandidateSqlBuild.class, method = "findById")
    MkRelayPrizeCandidate findById(@Param("id") String id);

    @Select("select " + MkRelayPrizeCandidateSqlBuild.columns + " from mk_relay_prize_candidate where status = 0 limit 1")
    MkRelayPrizeCandidate findActiveOne();

    @Select("select " + MkRelayPrizeCandidateSqlBuild.columns + " from mk_relay_prize_candidate where status = 1 order by achieve_date desc limit 1")
    MkRelayPrizeCandidate findAwardOne();

    @Select("select " + MkRelayPrizeCandidateSqlBuild.columns + " from mk_relay_prize_candidate where elector_id = #{electorId} order by achieve_date desc limit 1")
    MkRelayPrizeCandidate findByElectorId(@Param("electorId") String electorId);

    @Select("select " + MkRelayPrizeCandidateSqlBuild.columns + " from mk_relay_prize_candidate where status = 1 order by achieve_date desc limit 2")
    List<MkRelayPrizeCandidate> findLastList();

    @Select("select ifnull(mobile, mail) as mail, ifnull(refer_mobile, refer_mail) as refer_mail, prize_volume, update_date from mk_relay_prize_candidate where status = 1 order by update_date desc")
    List<MkRelayPrizeCandidate> findList();

    @Select("select ifnull(mobile, mail) as mail, is_prize, achieve_date from mk_relay_prize_candidate order by achieve_date desc limit 50")
    List<MkRelayPrizeCandidate> findAllList();

}
