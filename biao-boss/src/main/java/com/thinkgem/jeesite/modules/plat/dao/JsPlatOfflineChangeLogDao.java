/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineChangeLog;

/**
 * C2C转账记录DAO接口
 * @author zzj
 * @version 2018-10-26
 */
@MyBatisDao
public interface JsPlatOfflineChangeLogDao extends CrudDao<JsPlatOfflineChangeLog> {
	
}