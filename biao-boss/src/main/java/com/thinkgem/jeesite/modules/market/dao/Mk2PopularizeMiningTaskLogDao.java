/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMiningTaskLog;

/**
 * 挖矿任务日志DAO接口
 * @author dongfeng
 * @version 2018-08-07
 */
@MyBatisDao
public interface Mk2PopularizeMiningTaskLogDao extends CrudDao<Mk2PopularizeMiningTaskLog> {
	
}