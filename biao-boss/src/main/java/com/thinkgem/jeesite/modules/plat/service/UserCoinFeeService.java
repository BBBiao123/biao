/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.UserCoinFee;
import com.thinkgem.jeesite.modules.plat.dao.UserCoinFeeDao;

/**
 * 用户交易对手续费设置Service
 * @author dapao
 * @version 2018-08-31
 */
@Service
@Transactional(readOnly = true)
public class UserCoinFeeService extends CrudService<UserCoinFeeDao, UserCoinFee> {

	public UserCoinFee get(String id) {
		return super.get(id);
	}
	
	public List<UserCoinFee> findList(UserCoinFee userCoinFee) {
		return super.findList(userCoinFee);
	}
	
	public Page<UserCoinFee> findPage(Page<UserCoinFee> page, UserCoinFee userCoinFee) {
		return super.findPage(page, userCoinFee);
	}
	
	@Transactional(readOnly = false)
	public void save(UserCoinFee userCoinFee) {
		super.save(userCoinFee);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserCoinFee userCoinFee) {
		super.delete(userCoinFee);
	}
	
}