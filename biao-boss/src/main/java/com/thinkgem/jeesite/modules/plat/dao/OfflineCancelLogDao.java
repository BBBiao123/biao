/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCancelLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * C2C取消记录DAO接口
 * @author zzj
 * @version 2018-11-06
 */
@MyBatisDao
public interface OfflineCancelLogDao extends CrudDao<OfflineCancelLog> {

    @Delete("delete from offline_cancel_log where user_id = #{userId}")
    long deleteAdCancelLogByUserId(@Param("userId") String userId);

}