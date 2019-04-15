/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineTransferLog;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatOfflineTransferLogDao;

/**
 * bb to c2c转账日志Service
 * @author ruoyu
 * @version 2018-08-28
 */
@Service
@Transactional(readOnly = true)
public class JsPlatOfflineTransferLogService extends CrudService<JsPlatOfflineTransferLogDao, JsPlatOfflineTransferLog> {

	public JsPlatOfflineTransferLog get(String id) {
		return super.get(id);
	}
	
	public List<JsPlatOfflineTransferLog> findList(JsPlatOfflineTransferLog jsPlatOfflineTransferLog) {
		return super.findList(jsPlatOfflineTransferLog);
	}
	
	public Page<JsPlatOfflineTransferLog> findPage(Page<JsPlatOfflineTransferLog> page, JsPlatOfflineTransferLog jsPlatOfflineTransferLog) {
		return super.findPage(page, jsPlatOfflineTransferLog);
	}
	
	@Transactional(readOnly = false)
	public void save(JsPlatOfflineTransferLog jsPlatOfflineTransferLog) {
		super.save(jsPlatOfflineTransferLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(JsPlatOfflineTransferLog jsPlatOfflineTransferLog) {
		super.delete(jsPlatOfflineTransferLog);
	}
	
}