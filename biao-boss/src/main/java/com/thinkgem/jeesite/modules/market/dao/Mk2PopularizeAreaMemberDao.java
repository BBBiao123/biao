/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeAreaMember;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 区域合伙人售卖规则DAO接口
 * @author dongfeng
 * @version 2018-07-24
 */
@MyBatisDao
public interface Mk2PopularizeAreaMemberDao extends CrudDao<Mk2PopularizeAreaMember> {

    @Select("SELECT u.user_id from mk_common_user_relation u, mk2_popularize_area_member a WHERE u.user_id = a.user_id AND a.status = '1' AND FIND_IN_SET(#{userId}, u.tree_id) ORDER BY u.deth ASC ")
    List<String> findUserTree(String userId);

    @Update("update mk_common_user_relation set top_parent_id = #{userId} WHERE FIND_IN_SET(#{userId}, tree_id) ")
    long updateUserTopParentId(@Param("userId")String userId);

//    @Update("update js_plat_user_coin_volume t set t.volume = t.volume - #{vol} WHERE t.coin_id = #{coinId} AND t.user_id = #{uerId} AND t.update_date = #{updateDate} AND t.volume >= #{vol}")
//    long updateUserCoinVolume(@Param("vol")BigDecimal vol, @Param("coinId")String coinId, @Param("uerId")String uerId, @Param("updateDate")Date updateDate);

    @Delete("delete from mk2_popularize_area_member where parent_id = #{parentId}")
    long deleteShareholder(String parentId);

    List<Mk2PopularizeAreaMember> findShareholder(@Param("id")String id);

    @Update("UPDATE mk2_popularize_area_member set release_cycle_ratio = #{releaseCycleRatio}, release_begin_date = #{releaseBeginDate} WHERE id = #{id}")
    long updateAreaMemberReleaseVolume(@Param("id")String id, @Param("releaseCycleRatio")BigDecimal releaseCycleRatio, @Param("releaseBeginDate")Date releaseBeginDate);

    @Update("UPDATE mk2_popularize_area_member SET ratio = #{ratio} WHERE id = #{id}")
    long updateShareholder(@Param("id")String id, @Param("ratio")BigDecimal ratio);

    long updateBatchAreaMember(Mk2PopularizeAreaMember mk2PopularizeAreaMember);
}