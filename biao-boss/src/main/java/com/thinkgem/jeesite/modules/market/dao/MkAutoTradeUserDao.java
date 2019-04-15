/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.MkAutoTradeUser;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;

import java.util.List;

/**
 * 自动交易用户DAO接口
 * @author zhangzijun
 * @version 2018-08-17
 */
@MyBatisDao
public interface MkAutoTradeUserDao extends CrudDao<MkAutoTradeUser> {

	long deleteAll();
	long refresh();

	List<PlatUser> getPlatUser(MkAutoTradeUser mkAutoTradeUser);
}