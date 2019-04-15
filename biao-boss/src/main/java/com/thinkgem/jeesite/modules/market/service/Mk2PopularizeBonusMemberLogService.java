/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeBonusMemberLog;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeBonusMemberLogDao;

/**
 * 会员分红日志Service
 * @author dongfeng
 * @version 2018-08-09
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeBonusMemberLogService extends CrudService<Mk2PopularizeBonusMemberLogDao, Mk2PopularizeBonusMemberLog> {

	public Mk2PopularizeBonusMemberLog get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeBonusMemberLog> findList(Mk2PopularizeBonusMemberLog mk2PopularizeBonusMemberLog) {
		return super.findList(mk2PopularizeBonusMemberLog);
	}
	
	public Page<Mk2PopularizeBonusMemberLog> findPage(Page<Mk2PopularizeBonusMemberLog> page, Mk2PopularizeBonusMemberLog mk2PopularizeBonusMemberLog) {
		return super.findPage(page, mk2PopularizeBonusMemberLog);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeBonusMemberLog mk2PopularizeBonusMemberLog) {
		super.save(mk2PopularizeBonusMemberLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeBonusMemberLog mk2PopularizeBonusMemberLog) {
		super.delete(mk2PopularizeBonusMemberLog);
	}
	
}