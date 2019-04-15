/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkCoinDestroyRecord;
import com.thinkgem.jeesite.modules.market.dao.MkCoinDestroyRecordDao;

/**
 * 币种销毁记录Service
 * @author zzj
 * @version 2018-10-09
 */
@Service
@Transactional(readOnly = true)
public class MkCoinDestroyRecordService extends CrudService<MkCoinDestroyRecordDao, MkCoinDestroyRecord> {

	public MkCoinDestroyRecord get(String id) {
		return super.get(id);
	}
	
	public List<MkCoinDestroyRecord> findList(MkCoinDestroyRecord mkCoinDestroyRecord) {
		return super.findList(mkCoinDestroyRecord);
	}
	
	public Page<MkCoinDestroyRecord> findPage(Page<MkCoinDestroyRecord> page, MkCoinDestroyRecord mkCoinDestroyRecord) {
		return super.findPage(page, mkCoinDestroyRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(MkCoinDestroyRecord mkCoinDestroyRecord) {
		super.save(mkCoinDestroyRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkCoinDestroyRecord mkCoinDestroyRecord) {
		super.delete(mkCoinDestroyRecord);
	}
	
}