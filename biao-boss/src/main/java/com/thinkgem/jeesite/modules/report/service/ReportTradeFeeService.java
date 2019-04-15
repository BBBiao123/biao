/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.entity.ReportTradeFee;
import com.thinkgem.jeesite.modules.report.dao.ReportTradeFeeDao;

/**
 * ddService
 * @author ruoyu
 * @version 2018-06-26
 */
@Service
@Transactional(readOnly = true)
public class ReportTradeFeeService extends CrudService<ReportTradeFeeDao, ReportTradeFee> {

	public ReportTradeFee get(String id) {
		return super.get(id);
	}
	
	public List<ReportTradeFee> findList(ReportTradeFee reportTradeFee) {
		return super.findList(reportTradeFee);
	}
	
	public Page<ReportTradeFee> findPage(Page<ReportTradeFee> page, ReportTradeFee reportTradeFee) {
		return super.findPage(page, reportTradeFee);
	}
	
	@Transactional(readOnly = false)
	public void save(ReportTradeFee reportTradeFee) {
		super.save(reportTradeFee);
	}
	
	@Transactional(readOnly = false)
	public void delete(ReportTradeFee reportTradeFee) {
		super.delete(reportTradeFee);
	}
	
}