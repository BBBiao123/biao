/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.JsSysAppVersion;

/**
 * App版本管理DAO接口
 * @author zzj
 * @version 2018-08-27
 */
@MyBatisDao
public interface JsSysAppVersionDao extends CrudDao<JsSysAppVersion> {
	
}