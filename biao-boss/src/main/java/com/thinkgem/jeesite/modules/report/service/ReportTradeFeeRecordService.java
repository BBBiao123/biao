/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.dao.ReportTradeFeeRecordDao;
import com.thinkgem.jeesite.modules.report.entity.ReportTradeFeeRecord;

/**
 * 日手续费按交易对统计Service
 * @author ruoyu
 * @version 2018-06-26
 */
@Service
@Transactional(readOnly = true)
public class ReportTradeFeeRecordService extends CrudService<ReportTradeFeeRecordDao, ReportTradeFeeRecord> {

	public ReportTradeFeeRecord get(String id) {
		return super.get(id);
	}
	
	public List<ReportTradeFeeRecord> findList(ReportTradeFeeRecord reportTradeFeeRecord) {
		return super.findList(reportTradeFeeRecord);
	}
	
	public Page<ReportTradeFeeRecord> findPage(Page<ReportTradeFeeRecord> page, ReportTradeFeeRecord reportTradeFeeRecord) {
		return super.findPage(page, reportTradeFeeRecord);
	}
	
	public List<ReportTradeFeeRecord> findPageCount(Page<ReportTradeFeeRecord> page, ReportTradeFeeRecord reportTradeFeeRecord) {
		return this.dao.findListCount(reportTradeFeeRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(ReportTradeFeeRecord reportTradeFeeRecord) {
		super.save(reportTradeFeeRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(ReportTradeFeeRecord reportTradeFeeRecord) {
		super.delete(reportTradeFeeRecord);
	}
	
}