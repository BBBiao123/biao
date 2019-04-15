/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.entity.ReportTradeDay;
import com.thinkgem.jeesite.modules.report.dao.ReportTradeDayDao;

/**
 * 主区各币种交易量Service
 * @author dazi
 * @version 2018-05-12
 */
@Service
@Transactional(readOnly = true)
public class ReportTradeDayService extends CrudService<ReportTradeDayDao, ReportTradeDay> {

	public ReportTradeDay get(String id) {
		return super.get(id);
	}
	
	public List<ReportTradeDay> findList(ReportTradeDay reportTradeDay) {
		return super.findList(reportTradeDay);
	}
	
	public Page<ReportTradeDay> findPage(Page<ReportTradeDay> page, ReportTradeDay reportTradeDay) {
		return super.findPage(page, reportTradeDay);
	}
	
	@Transactional(readOnly = false)
	public void save(ReportTradeDay reportTradeDay) {
		super.save(reportTradeDay);
	}
	
	@Transactional(readOnly = false)
	public void delete(ReportTradeDay reportTradeDay) {
		super.delete(reportTradeDay);
	}
	
}