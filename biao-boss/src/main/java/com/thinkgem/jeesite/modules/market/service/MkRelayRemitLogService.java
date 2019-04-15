/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkRelayRemitLog;
import com.thinkgem.jeesite.modules.market.dao.MkRelayRemitLogDao;

/**
 * 接力撞奖打款日志Service
 * @author zzj
 * @version 2018-09-05
 */
@Service
@Transactional(readOnly = true)
public class MkRelayRemitLogService extends CrudService<MkRelayRemitLogDao, MkRelayRemitLog> {

	public MkRelayRemitLog get(String id) {
		return super.get(id);
	}
	
	public List<MkRelayRemitLog> findList(MkRelayRemitLog mkRelayRemitLog) {
		return super.findList(mkRelayRemitLog);
	}
	
	public Page<MkRelayRemitLog> findPage(Page<MkRelayRemitLog> page, MkRelayRemitLog mkRelayRemitLog) {
		return super.findPage(page, mkRelayRemitLog);
	}
	
	@Transactional(readOnly = false)
	public void save(MkRelayRemitLog mkRelayRemitLog) {
		super.save(mkRelayRemitLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkRelayRemitLog mkRelayRemitLog) {
		super.delete(mkRelayRemitLog);
	}
	
}