package com.biao.mapper;

import com.biao.entity.PlatUserSyna;
import com.biao.sql.build.PlatUserSynaBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlatUserSynaDao {

    @InsertProvider(type = PlatUserSynaBuild.class, method = "insert")
    void insert(PlatUserSyna platUserSyna);

    @InsertProvider(type = PlatUserSynaBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<PlatUserSyna> platUserSynas);

    @UpdateProvider(type = PlatUserSynaBuild.class, method = "updateById")
    void updateById(PlatUserSyna platUserSyna);

    @Select("select id,status,source_parent_id,source_id,mobile,username,source from js_plat_user_auto where status = #{status} and source = #{source} and source_parent_id = #{parentId}")
    List<PlatUserSyna> findListByParent(@Param("source") String source, @Param("parentId") String parentId, @Param("status") String status);

    @Select("select id,status,source_parent_id,source_id,mobile,username,source from js_plat_user_auto where status = #{status} and (source_parent_id = 0 or source_parent_id is null) order by create_date")
    List<PlatUserSyna> findListByStatusAndParent(Integer status);

    @Select("select id,status,source_parent_id,source_id,mobile,username,source from js_plat_user_auto where status = #{status} order by create_date")
    List<PlatUserSyna> findListByStatus(Integer status);

    @Select("select id,status,source_parent_id,source_id,mobile,username,source from js_plat_user_auto where source_id = #{sourceId} and source = #{source} limit 1")
    PlatUserSyna findByIdAndSource(@Param("sourceId") String sourceId, @Param("source") String source);

    @Select("select id,status,source_parent_id,source_id,mobile,username,source from js_plat_user_auto where source_id = #{sourceId} limit 1")
    PlatUserSyna findById(@Param("sourceId") String sourceId);

    @Select("select id,status,source_parent_id,source_id,mobile,username,source from js_plat_user_auto where mobile = #{mobile} limit 1")
    PlatUserSyna findByIdMobile(String mobile);

    @Select("select id,status,source_parent_id,source_id,mobile,username,source,pass,is_send_msg from js_plat_user_auto where is_send_msg = #{status} and status = '1' order by syn_date desc")
    List<PlatUserSyna> findListBySendMsgStatus(String status);
}
