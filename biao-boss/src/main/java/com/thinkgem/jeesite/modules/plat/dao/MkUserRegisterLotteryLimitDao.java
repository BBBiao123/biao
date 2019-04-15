/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.MkUserRegisterLotteryLimit;

/**
 * 注册送奖活动限制DAO接口
 * @author xiaoyu
 * @version 2018-11-05
 */
@MyBatisDao
public interface MkUserRegisterLotteryLimitDao extends CrudDao<MkUserRegisterLotteryLimit> {
	
}