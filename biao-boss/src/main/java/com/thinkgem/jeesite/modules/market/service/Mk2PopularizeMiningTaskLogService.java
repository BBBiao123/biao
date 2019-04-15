/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMiningTaskLog;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeMiningTaskLogDao;

/**
 * 挖矿任务日志Service
 * @author dongfeng
 * @version 2018-08-07
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeMiningTaskLogService extends CrudService<Mk2PopularizeMiningTaskLogDao, Mk2PopularizeMiningTaskLog> {

	public Mk2PopularizeMiningTaskLog get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeMiningTaskLog> findList(Mk2PopularizeMiningTaskLog mk2PopularizeMiningTaskLog) {
		return super.findList(mk2PopularizeMiningTaskLog);
	}
	
	public Page<Mk2PopularizeMiningTaskLog> findPage(Page<Mk2PopularizeMiningTaskLog> page, Mk2PopularizeMiningTaskLog mk2PopularizeMiningTaskLog) {
		return super.findPage(page, mk2PopularizeMiningTaskLog);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeMiningTaskLog mk2PopularizeMiningTaskLog) {
		super.save(mk2PopularizeMiningTaskLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeMiningTaskLog mk2PopularizeMiningTaskLog) {
		super.delete(mk2PopularizeMiningTaskLog);
	}
	
}