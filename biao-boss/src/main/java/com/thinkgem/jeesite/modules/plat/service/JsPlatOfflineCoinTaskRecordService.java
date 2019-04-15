/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineCoinTaskRecord;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatOfflineCoinTaskRecordDao;

/**
 * C2C币种价格更新记录Service
 * @author zzj
 * @version 2018-10-09
 */
@Service
@Transactional(readOnly = true)
public class JsPlatOfflineCoinTaskRecordService extends CrudService<JsPlatOfflineCoinTaskRecordDao, JsPlatOfflineCoinTaskRecord> {

	public JsPlatOfflineCoinTaskRecord get(String id) {
		return super.get(id);
	}
	
	public List<JsPlatOfflineCoinTaskRecord> findList(JsPlatOfflineCoinTaskRecord jsPlatOfflineCoinTaskRecord) {
		return super.findList(jsPlatOfflineCoinTaskRecord);
	}
	
	public Page<JsPlatOfflineCoinTaskRecord> findPage(Page<JsPlatOfflineCoinTaskRecord> page, JsPlatOfflineCoinTaskRecord jsPlatOfflineCoinTaskRecord) {
		return super.findPage(page, jsPlatOfflineCoinTaskRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(JsPlatOfflineCoinTaskRecord jsPlatOfflineCoinTaskRecord) {
		super.save(jsPlatOfflineCoinTaskRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(JsPlatOfflineCoinTaskRecord jsPlatOfflineCoinTaskRecord) {
		super.delete(jsPlatOfflineCoinTaskRecord);
	}
	
}