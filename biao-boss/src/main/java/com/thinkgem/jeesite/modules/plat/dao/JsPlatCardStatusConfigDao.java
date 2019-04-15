/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatCardStatusConfig;

/**
 * 实名认证限制DAO接口
 * @author ruoyu
 * @version 2018-11-27
 */
@MyBatisDao
public interface JsPlatCardStatusConfigDao extends CrudDao<JsPlatCardStatusConfig> {
	
}