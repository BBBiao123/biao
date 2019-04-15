/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkRelayAutoRecord;
import com.thinkgem.jeesite.modules.market.dao.MkRelayAutoRecordDao;

/**
 * 接力自动撞奖配置Service
 * @author zzj
 * @version 2018-09-26
 */
@Service
@Transactional(readOnly = true)
public class MkRelayAutoRecordService extends CrudService<MkRelayAutoRecordDao, MkRelayAutoRecord> {

	public MkRelayAutoRecord get(String id) {
		return super.get(id);
	}
	
	public List<MkRelayAutoRecord> findList(MkRelayAutoRecord mkRelayAutoRecord) {
		return super.findList(mkRelayAutoRecord);
	}
	
	public Page<MkRelayAutoRecord> findPage(Page<MkRelayAutoRecord> page, MkRelayAutoRecord mkRelayAutoRecord) {
		return super.findPage(page, mkRelayAutoRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(MkRelayAutoRecord mkRelayAutoRecord) {
		super.save(mkRelayAutoRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkRelayAutoRecord mkRelayAutoRecord) {
		super.delete(mkRelayAutoRecord);
	}
	
}