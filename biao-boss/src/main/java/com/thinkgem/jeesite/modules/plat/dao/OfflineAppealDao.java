/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.OfflineAppeal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

/**
 * 申诉DAO接口
 * @author dongfeng
 * @version 2018-06-29
 */
@MyBatisDao
public interface OfflineAppealDao extends CrudDao<OfflineAppeal> {
    @Update("update js_plat_offline_appeal set status = '2', examine_user_id = #{userId}, examine_date = #{examineDate}, " +
            "examine_result_user_id = #{examineResultUserId}, examine_result_reason = #{examineResultReason}, " +
            "examine_result_user_name = #{examineResultUserName} where id = #{appealId} and status = '1' and examine_date is null ")
    int updateAppealDone(@Param("appealId")String appealId,
                         @Param("userId")String userId,
                         @Param("examineDate")Timestamp examineDate,
                         @Param("examineResultUserId")String examineResultUserId,
                         @Param("examineResultReason")String examineResultReason,
                         @Param("examineResultUserName")String examineResultUserName);
}