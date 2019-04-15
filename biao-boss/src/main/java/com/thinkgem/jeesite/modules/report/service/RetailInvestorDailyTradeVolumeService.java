/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.dao.RetailInvestorDailyTradeVolumeDao;
import com.thinkgem.jeesite.modules.report.entity.RetailInvestorDailyTradeVolume;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 统计散户每天交易量Service
 * @author zzj
 * @version 2018-12-21
 */
@Service
@Transactional(readOnly = true)
public class RetailInvestorDailyTradeVolumeService extends CrudService<RetailInvestorDailyTradeVolumeDao, RetailInvestorDailyTradeVolume> {

	public RetailInvestorDailyTradeVolume get(String id) {
		return super.get(id);
	}
	
	public List<RetailInvestorDailyTradeVolume> findList(RetailInvestorDailyTradeVolume retailInvestorDailyTradeVolume) {
		return super.findList(retailInvestorDailyTradeVolume);
	}
	
	public Page<RetailInvestorDailyTradeVolume> findPage(Page<RetailInvestorDailyTradeVolume> page, RetailInvestorDailyTradeVolume retailInvestorDailyTradeVolume) {
		return super.findPage(page, retailInvestorDailyTradeVolume);
	}

	
}