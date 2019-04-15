/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.UserCoinFee;

/**
 * 用户交易对手续费设置DAO接口
 * @author dapao
 * @version 2018-08-31
 */
@MyBatisDao
public interface UserCoinFeeDao extends CrudDao<UserCoinFee> {
	
}