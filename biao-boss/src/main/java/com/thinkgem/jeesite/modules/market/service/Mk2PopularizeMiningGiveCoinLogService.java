/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMiningGiveCoinLog;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeMiningGiveCoinLogDao;

/**
 * 挖矿规则送币流水Service
 * @author dongfeng
 * @version 2018-08-07
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeMiningGiveCoinLogService extends CrudService<Mk2PopularizeMiningGiveCoinLogDao, Mk2PopularizeMiningGiveCoinLog> {

	public Mk2PopularizeMiningGiveCoinLog get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeMiningGiveCoinLog> findList(Mk2PopularizeMiningGiveCoinLog mk2PopularizeMiningGiveCoinLog) {
		return super.findList(mk2PopularizeMiningGiveCoinLog);
	}
	
	public Page<Mk2PopularizeMiningGiveCoinLog> findPage(Page<Mk2PopularizeMiningGiveCoinLog> page, Mk2PopularizeMiningGiveCoinLog mk2PopularizeMiningGiveCoinLog) {
		return super.findPage(page, mk2PopularizeMiningGiveCoinLog);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeMiningGiveCoinLog mk2PopularizeMiningGiveCoinLog) {
		super.save(mk2PopularizeMiningGiveCoinLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeMiningGiveCoinLog mk2PopularizeMiningGiveCoinLog) {
		super.delete(mk2PopularizeMiningGiveCoinLog);
	}
	
}