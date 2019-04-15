package com.biao.mapper.mkcommon;

import com.biao.entity.UserRelation;
import com.biao.sql.build.mkcommon.MkCommonUserRelationBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface MkCommonUserRelationDao {

    @InsertProvider(type=MkCommonUserRelationBuild.class, method = "insert")
    void insert(UserRelation userRelation);

    @InsertProvider(type=MkCommonUserRelationBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<UserRelation> userRelationList);

    @Delete("delete from mk_common_user_relation")
    long deleteUserRelation();

    @Select("select ifnull(u.refer_id,'') as refer_id, u.id as id, u.username as username from js_plat_user u where u.id  is not null")
    List<Map<String,Object>> getUserTreeId();

    @Select("select id, user_id, username, parent_id, top_parent_id, tree_id, deth, level from mk_common_user_relation where user_id = #{userId} order by level asc")
    List<UserRelation> findUserRelationById(@Param("userId") String userId);

}
