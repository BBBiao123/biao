package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeMemberRule;
import com.biao.sql.build.mk2.Mk2PopularizeMemberRuleBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface Mk2PopularizeMemberRuleDao {

    @Select("SELECT " + Mk2PopularizeMemberRuleBuild.columns + " FROM mk2_popularize_member_rule t WHERE t.type = #{type} LIMIT 1 ")
    Mk2PopularizeMemberRule findByType(String type);

    @Update("UPDATE mk2_popularize_member_rule SET release_version = #{releaseVersion} WHERE type = #{type} ")
    long updateMemberRule(Mk2PopularizeMemberRule rule);
}
