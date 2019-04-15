/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.dao.UserWithdrawLogDao;
import com.thinkgem.jeesite.modules.plat.entity.UserWithdrawLog;
import com.thinkgem.jeesite.modules.plat.entity.UserWithdrawLogCount;

/**
 * 用户提现管理Service
 * @author dazi
 * @version 2018-05-04
 */
@Service
@Transactional(readOnly = true)
public class UserWithdrawLogService extends CrudService<UserWithdrawLogDao, UserWithdrawLog> {

	public UserWithdrawLog get(String id) {
		return super.get(id);
	}
	
	public List<UserWithdrawLog> findList(UserWithdrawLog userWithdrawLog) {
		return super.findList(userWithdrawLog);
	}
	
	public List<UserWithdrawLog> findListCount(UserWithdrawLog userWithdrawLog) {
		return this.dao.findListCount(userWithdrawLog);
	}
	
	public Page<UserWithdrawLog> findPage(Page<UserWithdrawLog> page, UserWithdrawLog userWithdrawLog) {
		return super.findPage(page, userWithdrawLog);
	}
	
	@Transactional(readOnly = false)
	public void save(UserWithdrawLog userWithdrawLog) {
		super.save(userWithdrawLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserWithdrawLog userWithdrawLog) {
		super.delete(userWithdrawLog);
	}
	
	public UserWithdrawLogCount checkUserWithdrawLog(UserWithdrawLog userWithdrawLog) {
		return this.dao.checkUserWithdrawLog(userWithdrawLog);
	}
	
}