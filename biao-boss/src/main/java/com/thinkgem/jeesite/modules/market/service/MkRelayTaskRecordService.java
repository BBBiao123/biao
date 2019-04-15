/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkRelayTaskRecord;
import com.thinkgem.jeesite.modules.market.dao.MkRelayTaskRecordDao;

/**
 * 接力撞奖执行记录Service
 * @author zzj
 * @version 2018-09-05
 */
@Service
@Transactional(readOnly = true)
public class MkRelayTaskRecordService extends CrudService<MkRelayTaskRecordDao, MkRelayTaskRecord> {

	public MkRelayTaskRecord get(String id) {
		return super.get(id);
	}
	
	public List<MkRelayTaskRecord> findList(MkRelayTaskRecord mkRelayTaskRecord) {
		return super.findList(mkRelayTaskRecord);
	}
	
	public Page<MkRelayTaskRecord> findPage(Page<MkRelayTaskRecord> page, MkRelayTaskRecord mkRelayTaskRecord) {
		return super.findPage(page, mkRelayTaskRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(MkRelayTaskRecord mkRelayTaskRecord) {
		super.save(mkRelayTaskRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkRelayTaskRecord mkRelayTaskRecord) {
		super.delete(mkRelayTaskRecord);
	}
	
}