/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.UserWithdrawLog;
import com.thinkgem.jeesite.modules.plat.entity.UserWithdrawLogCount;

/**
 * 用户提现管理DAO接口
 * @author dazi
 * @version 2018-05-04
 */
@MyBatisDao
public interface UserWithdrawLogDao extends CrudDao<UserWithdrawLog> {
	
	public List<UserWithdrawLog> findListCount(UserWithdrawLog userWithdrawLog);
	
	public UserWithdrawLogCount checkUserWithdrawLog(UserWithdrawLog userWithdrawLog);
}