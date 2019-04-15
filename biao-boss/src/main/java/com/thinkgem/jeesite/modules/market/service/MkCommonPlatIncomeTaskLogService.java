/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkCommonPlatIncomeTaskLog;
import com.thinkgem.jeesite.modules.market.dao.MkCommonPlatIncomeTaskLogDao;

/**
 * 平台收入任务日志Service
 * @author dongfeng
 * @version 2018-08-09
 */
@Service
@Transactional(readOnly = true)
public class MkCommonPlatIncomeTaskLogService extends CrudService<MkCommonPlatIncomeTaskLogDao, MkCommonPlatIncomeTaskLog> {

	public MkCommonPlatIncomeTaskLog get(String id) {
		return super.get(id);
	}
	
	public List<MkCommonPlatIncomeTaskLog> findList(MkCommonPlatIncomeTaskLog mkCommonPlatIncomeTaskLog) {
		return super.findList(mkCommonPlatIncomeTaskLog);
	}
	
	public Page<MkCommonPlatIncomeTaskLog> findPage(Page<MkCommonPlatIncomeTaskLog> page, MkCommonPlatIncomeTaskLog mkCommonPlatIncomeTaskLog) {
		return super.findPage(page, mkCommonPlatIncomeTaskLog);
	}
	
	@Transactional(readOnly = false)
	public void save(MkCommonPlatIncomeTaskLog mkCommonPlatIncomeTaskLog) {
		super.save(mkCommonPlatIncomeTaskLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkCommonPlatIncomeTaskLog mkCommonPlatIncomeTaskLog) {
		super.delete(mkCommonPlatIncomeTaskLog);
	}
	
}