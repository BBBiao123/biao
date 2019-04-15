/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeBonusAccountLog;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeBonusAccountLogDao;

/**
 * 平台运营分红日志Service
 * @author dongfeng
 * @version 2018-08-06
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeBonusAccountLogService extends CrudService<Mk2PopularizeBonusAccountLogDao, Mk2PopularizeBonusAccountLog> {

	public Mk2PopularizeBonusAccountLog get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeBonusAccountLog> findList(Mk2PopularizeBonusAccountLog mk2PopularizeBonusAccountLog) {
		return super.findList(mk2PopularizeBonusAccountLog);
	}
	
	public Page<Mk2PopularizeBonusAccountLog> findPage(Page<Mk2PopularizeBonusAccountLog> page, Mk2PopularizeBonusAccountLog mk2PopularizeBonusAccountLog) {
		return super.findPage(page, mk2PopularizeBonusAccountLog);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeBonusAccountLog mk2PopularizeBonusAccountLog) {
		super.save(mk2PopularizeBonusAccountLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeBonusAccountLog mk2PopularizeBonusAccountLog) {
		super.delete(mk2PopularizeBonusAccountLog);
	}
	
}