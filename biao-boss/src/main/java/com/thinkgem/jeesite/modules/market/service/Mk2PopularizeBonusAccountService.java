/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeBonusAccount;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeBonusAccountDao;

/**
 * 平台运营分红账户Service
 * @author dongfeng
 * @version 2018-07-31
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeBonusAccountService extends CrudService<Mk2PopularizeBonusAccountDao, Mk2PopularizeBonusAccount> {

	public Mk2PopularizeBonusAccount get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeBonusAccount> findList(Mk2PopularizeBonusAccount mk2PopularizeBonusAccount) {
		return super.findList(mk2PopularizeBonusAccount);
	}
	
	public Page<Mk2PopularizeBonusAccount> findPage(Page<Mk2PopularizeBonusAccount> page, Mk2PopularizeBonusAccount mk2PopularizeBonusAccount) {
		return super.findPage(page, mk2PopularizeBonusAccount);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeBonusAccount mk2PopularizeBonusAccount) {
		super.save(mk2PopularizeBonusAccount);
	}
	
	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeBonusAccount mk2PopularizeBonusAccount) {
		super.delete(mk2PopularizeBonusAccount);
	}
	
}