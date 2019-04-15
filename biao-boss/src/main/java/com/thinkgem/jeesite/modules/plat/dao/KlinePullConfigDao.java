/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.KlinePullConfig;

/**
 * 币安k线配置DAO接口
 * @author xiaoyu
 * @version 2018-12-25
 */
@MyBatisDao
public interface KlinePullConfigDao extends CrudDao<KlinePullConfig> {
	
}