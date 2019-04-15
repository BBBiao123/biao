/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.MkPromoteManage;

/**
 * 会员推广规则管理DAO接口
 * @author zhangzijun
 * @version 2018-07-16
 */
@MyBatisDao
public interface MkPromoteManageDao extends CrudDao<MkPromoteManage> {
	
}