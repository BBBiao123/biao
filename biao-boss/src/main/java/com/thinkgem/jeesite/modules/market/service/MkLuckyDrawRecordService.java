/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkLuckyDrawRecord;
import com.thinkgem.jeesite.modules.market.dao.MkLuckyDrawRecordDao;

/**
 * 抽奖活动开奖记录Service
 * @author zzj
 * @version 2018-11-01
 */
@Service
@Transactional(readOnly = true)
public class MkLuckyDrawRecordService extends CrudService<MkLuckyDrawRecordDao, MkLuckyDrawRecord> {

	public MkLuckyDrawRecord get(String id) {
		return super.get(id);
	}
	
	public List<MkLuckyDrawRecord> findList(MkLuckyDrawRecord mkLuckyDrawRecord) {
		return super.findList(mkLuckyDrawRecord);
	}
	
	public Page<MkLuckyDrawRecord> findPage(Page<MkLuckyDrawRecord> page, MkLuckyDrawRecord mkLuckyDrawRecord) {
		return super.findPage(page, mkLuckyDrawRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(MkLuckyDrawRecord mkLuckyDrawRecord) {
		super.save(mkLuckyDrawRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkLuckyDrawRecord mkLuckyDrawRecord) {
		super.delete(mkLuckyDrawRecord);
	}
	
}