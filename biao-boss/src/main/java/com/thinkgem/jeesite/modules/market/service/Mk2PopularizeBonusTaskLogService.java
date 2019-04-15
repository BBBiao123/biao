/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeBonusTaskLog;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeBonusTaskLogDao;

/**
 * 分红任务运行记录Service
 * @author dongfeng
 * @version 2018-08-06
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeBonusTaskLogService extends CrudService<Mk2PopularizeBonusTaskLogDao, Mk2PopularizeBonusTaskLog> {

	public Mk2PopularizeBonusTaskLog get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeBonusTaskLog> findList(Mk2PopularizeBonusTaskLog mk2PopularizeBonusTaskLog) {
		return super.findList(mk2PopularizeBonusTaskLog);
	}
	
	public Page<Mk2PopularizeBonusTaskLog> findPage(Page<Mk2PopularizeBonusTaskLog> page, Mk2PopularizeBonusTaskLog mk2PopularizeBonusTaskLog) {
		return super.findPage(page, mk2PopularizeBonusTaskLog);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeBonusTaskLog mk2PopularizeBonusTaskLog) {
		super.save(mk2PopularizeBonusTaskLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeBonusTaskLog mk2PopularizeBonusTaskLog) {
		super.delete(mk2PopularizeBonusTaskLog);
	}
	
}