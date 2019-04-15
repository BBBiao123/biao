/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkDistributeAccount;
import com.thinkgem.jeesite.modules.market.dao.MkDistributeAccountDao;

/**
 * 营销账户Service
 * @author zhangzijun
 * @version 2018-07-05
 */
@Service
@Transactional(readOnly = true)
public class MkDistributeAccountService extends CrudService<MkDistributeAccountDao, MkDistributeAccount> {

	public MkDistributeAccount get(String id) {
		return super.get(id);
	}
	
	public List<MkDistributeAccount> findList(MkDistributeAccount mkDistributeAccount) {
		return super.findList(mkDistributeAccount);
	}
	
	public Page<MkDistributeAccount> findPage(Page<MkDistributeAccount> page, MkDistributeAccount mkDistributeAccount) {
		return super.findPage(page, mkDistributeAccount);
	}
	
	@Transactional(readOnly = false)
	public void save(MkDistributeAccount mkDistributeAccount) {
		if(mkDistributeAccount.getIsNewRecord()){
			mkDistributeAccount.setCreateDate(new Date());
		}
		super.save(mkDistributeAccount);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkDistributeAccount mkDistributeAccount) {
		super.delete(mkDistributeAccount);
	}
	
}