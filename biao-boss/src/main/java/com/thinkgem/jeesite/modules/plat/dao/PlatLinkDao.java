/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.PlatLink;

/**
 * 平台链接DAO接口
 * @author dongfeng
 * @version 2018-07-03
 */
@MyBatisDao
public interface PlatLinkDao extends CrudDao<PlatLink> {
	
}