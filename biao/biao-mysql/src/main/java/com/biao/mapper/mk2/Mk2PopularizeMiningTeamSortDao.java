package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeMiningTeamConf;
import com.biao.entity.mk2.Mk2PopularizeMiningTeamSort;
import com.biao.sql.build.mk2.Mk2PopularizeMiningTeamSortBuild;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface Mk2PopularizeMiningTeamSortDao {

    @InsertProvider(type = Mk2PopularizeMiningTeamSortBuild.class, method = "insert")
    void insert(Mk2PopularizeMiningTeamSort sort);

    @InsertProvider(type = Mk2PopularizeMiningTeamSortBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<Mk2PopularizeMiningTeamSort> sorts);

    @Select("SELECT t.id as id, t.show as 'show', t.sort_begin_date as sortBeginDate, t.sort_end_date as sortEndDate FROM mk2_popularize_mining_team_conf t LIMIT 1")
    Mk2PopularizeMiningTeamConf findConf();

    @Select("SELECT " + Mk2PopularizeMiningTeamSortBuild.columns + " FROM mk2_popularize_mining_team_sort t WHERE t.show_type = '1' AND t.order_no < 1001 ORDER BY order_no ASC ")
    List<Mk2PopularizeMiningTeamSort> findAll();

    @Select("SELECT " + Mk2PopularizeMiningTeamSortBuild.columns + " FROM mk2_popularize_mining_team_sort t WHERE t.user_id = #{userId} AND t.show_type = '1' LIMIT 1 ")
    Mk2PopularizeMiningTeamSort findByUserId(@Param("userId") String userId);

    @Update("INSERT INTO mk2_popularize_mining_team_sort (id, user_id, real_name, mail, mobile, volume, order_no, sort_date, show_type) " +
            "SELECT UUID(), n.user_id,n.real_name,n.mail,n.mobile,n.volume,(@rowNum :=@rowNum + 1) as order_no, #{sortDate}, '0' FROM " +
            "(SELECT t.user_id as user_id, MIN(t.real_name) as real_name, MIN(t.mail) as mail, MIN(t.mobile) as mobile, sum(t.volume) as volume " +
            " FROM mk2_popularize_mining_give_coin_log t where t.type = 2 AND t.count_date > #{sortBeginDate} AND t.count_date < #{sortEndDate} GROUP BY t.user_id " +
            ") n, (SELECT(@rowNum := 0)) b ORDER BY n.volume DESC ")
    long sortTeamMingingTemp(@Param("sortDate") LocalDateTime sortDate, @Param("sortBeginDate") LocalDateTime sortBeginDate, @Param("sortEndDate") LocalDateTime sortEndDate);

    @Update("INSERT INTO mk2_popularize_mining_team_sort (id, user_id, real_name, mail, mobile, volume, sort_date, show_type, order_no) " +
            " SELECT UUID(), t.user_id, t.real_name, t.mail, t.mobile, t.volume, t.sort_date, '1', " +
            " (SELECT min(a.order_no) FROM mk2_popularize_mining_team_sort a WHERE a.volume = t.volume) " +
            " FROM mk2_popularize_mining_team_sort t WHERE t.show_type = '0' ")
    long sortTeamMinging();

    @Update("INSERT INTO mk2_popularize_mining_team_sort (id, user_id, real_name, mail, mobile, volume, sort_date, show_type, order_no) " +
            " SELECT UUID(), t.user_id, t.real_name, t.mail, t.mobile, t.volume, t.sort_date, '2', t.order_no " +
            " FROM mk2_popularize_mining_team_sort t WHERE t.show_type = '1' ORDER BY t.order_no ASC LIMIT 300 ")
    long sortTeamMingingList();

    @Delete("DELETE FROM mk2_popularize_mining_team_sort WHERE show_type = '0' ")
    long deleteTeamTempSort();

    @Delete("DELETE FROM mk2_popularize_mining_team_sort ")
    long deleteTeamSort();
}
