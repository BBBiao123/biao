/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.Mk2PopularizeTaskLog;
import com.thinkgem.jeesite.modules.plat.dao.Mk2PopularizeTaskLogDao;

/**
 * mk2营销任务执行结果Service
 * @author dongfeng
 * @version 2018-07-20
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeTaskLogService extends CrudService<Mk2PopularizeTaskLogDao, Mk2PopularizeTaskLog> {

	public Mk2PopularizeTaskLog get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeTaskLog> findList(Mk2PopularizeTaskLog mk2PopularizeTaskLog) {
		return super.findList(mk2PopularizeTaskLog);
	}
	
	public Page<Mk2PopularizeTaskLog> findPage(Page<Mk2PopularizeTaskLog> page, Mk2PopularizeTaskLog mk2PopularizeTaskLog) {
		return super.findPage(page, mk2PopularizeTaskLog);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeTaskLog mk2PopularizeTaskLog) {
		super.save(mk2PopularizeTaskLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeTaskLog mk2PopularizeTaskLog) {
		super.delete(mk2PopularizeTaskLog);
	}
	
}