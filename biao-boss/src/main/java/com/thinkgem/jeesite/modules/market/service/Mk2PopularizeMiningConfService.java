/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMiningConf;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeMiningConfDao;

/**
 * 挖矿规则Service
 * @author dongfeng
 * @version 2018-08-07
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeMiningConfService extends CrudService<Mk2PopularizeMiningConfDao, Mk2PopularizeMiningConf> {

	public Mk2PopularizeMiningConf get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeMiningConf> findList(Mk2PopularizeMiningConf mk2PopularizeMiningConf) {
		return super.findList(mk2PopularizeMiningConf);
	}
	
	public Page<Mk2PopularizeMiningConf> findPage(Page<Mk2PopularizeMiningConf> page, Mk2PopularizeMiningConf mk2PopularizeMiningConf) {
		return super.findPage(page, mk2PopularizeMiningConf);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeMiningConf mk2PopularizeMiningConf) {
		super.save(mk2PopularizeMiningConf);
	}
	
	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeMiningConf mk2PopularizeMiningConf) {
		super.delete(mk2PopularizeMiningConf);
	}
	
}