/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.DestroyAccountLog;
import com.thinkgem.jeesite.modules.plat.dao.DestroyAccountLogDao;

/**
 * 销毁账户流水Service
 * @author zzj
 * @version 2018-12-25
 */
@Service
@Transactional(readOnly = true)
public class DestroyAccountLogService extends CrudService<DestroyAccountLogDao, DestroyAccountLog> {

	public DestroyAccountLog get(String id) {
		return super.get(id);
	}
	
	public List<DestroyAccountLog> findList(DestroyAccountLog destroyAccountLog) {
		return super.findList(destroyAccountLog);
	}
	
	public Page<DestroyAccountLog> findPage(Page<DestroyAccountLog> page, DestroyAccountLog destroyAccountLog) {
		return super.findPage(page, destroyAccountLog);
	}
	
	@Transactional(readOnly = false)
	public void save(DestroyAccountLog destroyAccountLog) {
		super.save(destroyAccountLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(DestroyAccountLog destroyAccountLog) {
		super.delete(destroyAccountLog);
	}
	
}