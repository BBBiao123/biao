/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineChangeLog;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatOfflineChangeLogDao;

/**
 * C2C转账记录Service
 * @author zzj
 * @version 2018-10-26
 */
@Service
@Transactional(readOnly = true)
public class JsPlatOfflineChangeLogService extends CrudService<JsPlatOfflineChangeLogDao, JsPlatOfflineChangeLog> {

	public JsPlatOfflineChangeLog get(String id) {
		return super.get(id);
	}
	
	public List<JsPlatOfflineChangeLog> findList(JsPlatOfflineChangeLog jsPlatOfflineChangeLog) {
		return super.findList(jsPlatOfflineChangeLog);
	}
	
	public Page<JsPlatOfflineChangeLog> findPage(Page<JsPlatOfflineChangeLog> page, JsPlatOfflineChangeLog jsPlatOfflineChangeLog) {
		return super.findPage(page, jsPlatOfflineChangeLog);
	}
	
	@Transactional(readOnly = false)
	public void save(JsPlatOfflineChangeLog jsPlatOfflineChangeLog) {
		super.save(jsPlatOfflineChangeLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(JsPlatOfflineChangeLog jsPlatOfflineChangeLog) {
		super.delete(jsPlatOfflineChangeLog);
	}
	
}