/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkCommonPlatIncomeLog;
import com.thinkgem.jeesite.modules.market.dao.MkCommonPlatIncomeLogDao;

/**
 * 平台收入流水Service
 * @author dongfeng
 * @version 2018-08-09
 */
@Service
@Transactional(readOnly = true)
public class MkCommonPlatIncomeLogService extends CrudService<MkCommonPlatIncomeLogDao, MkCommonPlatIncomeLog> {

	public MkCommonPlatIncomeLog get(String id) {
		return super.get(id);
	}
	
	public List<MkCommonPlatIncomeLog> findList(MkCommonPlatIncomeLog mkCommonPlatIncomeLog) {
		return super.findList(mkCommonPlatIncomeLog);
	}
	
	public Page<MkCommonPlatIncomeLog> findPage(Page<MkCommonPlatIncomeLog> page, MkCommonPlatIncomeLog mkCommonPlatIncomeLog) {
		return super.findPage(page, mkCommonPlatIncomeLog);
	}
	
	@Transactional(readOnly = false)
	public void save(MkCommonPlatIncomeLog mkCommonPlatIncomeLog) {
		super.save(mkCommonPlatIncomeLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkCommonPlatIncomeLog mkCommonPlatIncomeLog) {
		super.delete(mkCommonPlatIncomeLog);
	}
	
}