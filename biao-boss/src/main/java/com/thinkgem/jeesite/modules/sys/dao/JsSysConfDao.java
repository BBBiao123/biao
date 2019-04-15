/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.JsSysConf;

/**
 * Plat系统配置DAO接口
 * @author zzj
 * @version 2019-03-05
 */
@MyBatisDao
public interface JsSysConfDao extends CrudDao<JsSysConf> {
	
}