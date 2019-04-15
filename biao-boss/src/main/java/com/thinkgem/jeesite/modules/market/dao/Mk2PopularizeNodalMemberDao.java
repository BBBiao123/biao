/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeNodalMember;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * 节点人DAO接口
 * @author dongfeng
 * @version 2018-07-24
 */
@MyBatisDao
public interface Mk2PopularizeNodalMemberDao extends CrudDao<Mk2PopularizeNodalMember> {

    List<Mk2PopularizeNodalMember> findByUserId(String userId);

//    @Update("UPDATE mk2_popularize_nodal_member SET release_cycle_ratio = #{releaseCycleRatio} WHERE id = #{id} ")
//    long updateNodalMember(@Param("id")String id, @Param("releaseCycleRatio")BigDecimal releaseCycleRatio);

    long updateNoLockReleaseInfo(Mk2PopularizeNodalMember mk2PopularizeNodalMember);

    long updateHadLockReleaseInfo(Mk2PopularizeNodalMember mk2PopularizeNodalMember);

    List<Mk2PopularizeNodalMember> findLockReleaseInfo(String id);

    @Select("SELECT COUNT(1) FROM mk2_popularize_nodal_member t WHERE t.parent_id = #{parentId} ")
    long countLockReleaseRule(String parentId);

}