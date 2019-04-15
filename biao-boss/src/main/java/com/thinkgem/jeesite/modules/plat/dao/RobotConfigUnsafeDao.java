/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.RobotConfigUnsafe;

/**
 * 币安自动化管理DAO接口
 * @author xiaoyu
 * @version 2018-12-25
 */
@MyBatisDao
public interface RobotConfigUnsafeDao extends CrudDao<RobotConfigUnsafe> {
	
}