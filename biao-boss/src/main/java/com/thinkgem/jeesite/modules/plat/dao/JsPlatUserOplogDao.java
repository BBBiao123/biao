/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserOplog;

/**
 * 会员操作日志DAO接口
 * @author zzj
 * @version 2018-11-07
 */
@MyBatisDao
public interface JsPlatUserOplogDao extends CrudDao<JsPlatUserOplog> {
	
}