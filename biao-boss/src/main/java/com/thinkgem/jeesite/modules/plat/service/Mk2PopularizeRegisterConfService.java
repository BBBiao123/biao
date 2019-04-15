/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.Mk2PopularizeRegisterConf;
import com.thinkgem.jeesite.modules.plat.dao.Mk2PopularizeRegisterConfDao;

/**
 * 注册送币规则Service
 * @author dongfeng
 * @version 2018-07-20
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeRegisterConfService extends CrudService<Mk2PopularizeRegisterConfDao, Mk2PopularizeRegisterConf> {

	public Mk2PopularizeRegisterConf get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeRegisterConf> findList(Mk2PopularizeRegisterConf mk2PopularizeRegisterConf) {
		return super.findList(mk2PopularizeRegisterConf);
	}
	
	public Page<Mk2PopularizeRegisterConf> findPage(Page<Mk2PopularizeRegisterConf> page, Mk2PopularizeRegisterConf mk2PopularizeRegisterConf) {
		return super.findPage(page, mk2PopularizeRegisterConf);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeRegisterConf mk2PopularizeRegisterConf) {
		super.save(mk2PopularizeRegisterConf);
	}
	
	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeRegisterConf mk2PopularizeRegisterConf) {
		super.delete(mk2PopularizeRegisterConf);
	}

	public List<Mk2PopularizeRegisterConf> findEffective() {
		return dao.findEffective();
	}
	
}