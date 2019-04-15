/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.robot.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.robot.entity.RobotConfig;

/**
 * 价格机器人DAO接口
 * @author dazi
 * @version 2018-06-06
 */
@MyBatisDao
public interface RobotConfigDao extends CrudDao<RobotConfig> {
	
}