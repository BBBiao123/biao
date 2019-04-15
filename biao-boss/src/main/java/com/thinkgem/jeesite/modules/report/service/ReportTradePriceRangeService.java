/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.dao.ReportTradePriceRangeDao;
import com.thinkgem.jeesite.modules.report.entity.ReportTradePriceRange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 币币交易价格区间统计报表Service
 * @author zzj
 * @version 2018-12-14
 */
@Service
@Transactional(readOnly = true)
public class ReportTradePriceRangeService extends CrudService<ReportTradePriceRangeDao, ReportTradePriceRange> {

	public ReportTradePriceRange get(String id) {
		return super.get(id);
	}
	
	public List<ReportTradePriceRange> findList(ReportTradePriceRange reportTradePriceRange) {
		return super.findList(reportTradePriceRange);
	}
	
	public Page<ReportTradePriceRange> findPage(Page<ReportTradePriceRange> page, ReportTradePriceRange reportTradePriceRange) {
		return super.findPage(page, reportTradePriceRange);
	}

}