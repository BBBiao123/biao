/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeReleaseLog;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeReleaseLogDao;

/**
 * 冻结数量释放记录Service
 * @author dongfeng
 * @version 2018-08-09
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeReleaseLogService extends CrudService<Mk2PopularizeReleaseLogDao, Mk2PopularizeReleaseLog> {

	public Mk2PopularizeReleaseLog get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeReleaseLog> findList(Mk2PopularizeReleaseLog mk2PopularizeReleaseLog) {
		return super.findList(mk2PopularizeReleaseLog);
	}
	
	public Page<Mk2PopularizeReleaseLog> findPage(Page<Mk2PopularizeReleaseLog> page, Mk2PopularizeReleaseLog mk2PopularizeReleaseLog) {
		return super.findPage(page, mk2PopularizeReleaseLog);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeReleaseLog mk2PopularizeReleaseLog) {
		super.save(mk2PopularizeReleaseLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeReleaseLog mk2PopularizeReleaseLog) {
		super.delete(mk2PopularizeReleaseLog);
	}
	
}