/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCancelLog;
import com.thinkgem.jeesite.modules.plat.dao.OfflineCancelLogDao;

/**
 * C2C取消记录Service
 * @author zzj
 * @version 2018-11-06
 */
@Service
@Transactional(readOnly = true)
public class OfflineCancelLogService extends CrudService<OfflineCancelLogDao, OfflineCancelLog> {

	public OfflineCancelLog get(String id) {
		return super.get(id);
	}
	
	public List<OfflineCancelLog> findList(OfflineCancelLog offlineCancelLog) {
		return super.findList(offlineCancelLog);
	}
	
	public Page<OfflineCancelLog> findPage(Page<OfflineCancelLog> page, OfflineCancelLog offlineCancelLog) {
		return super.findPage(page, offlineCancelLog);
	}
	
	@Transactional(readOnly = false)
	public void save(OfflineCancelLog offlineCancelLog) {
		super.save(offlineCancelLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(OfflineCancelLog offlineCancelLog) {
		super.delete(offlineCancelLog);
	}

	@Transactional(readOnly = false)
	public void deleteAdCancelLogByUserId(String userId) {
		dao.deleteAdCancelLogByUserId(userId);
	}
	
}