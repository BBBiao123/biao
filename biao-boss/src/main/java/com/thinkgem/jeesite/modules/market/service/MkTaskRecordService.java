/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkTaskRecord;
import com.thinkgem.jeesite.modules.market.dao.MkTaskRecordDao;

/**
 * 营销任务执行记录Service
 * @author zhangzijun
 * @version 2018-07-06
 */
@Service
@Transactional(readOnly = true)
public class MkTaskRecordService extends CrudService<MkTaskRecordDao, MkTaskRecord> {

	public MkTaskRecord get(String id) {
		return super.get(id);
	}
	
	public List<MkTaskRecord> findList(MkTaskRecord mkTaskRecord) {
		return super.findList(mkTaskRecord);
	}
	
	public Page<MkTaskRecord> findPage(Page<MkTaskRecord> page, MkTaskRecord mkTaskRecord) {
		return super.findPage(page, mkTaskRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(MkTaskRecord mkTaskRecord) {
		super.save(mkTaskRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkTaskRecord mkTaskRecord) {
		super.delete(mkTaskRecord);
	}
	
}