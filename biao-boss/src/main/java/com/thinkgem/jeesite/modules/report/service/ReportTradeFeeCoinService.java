/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.entity.ReportTradeFeeCoin;
import com.thinkgem.jeesite.modules.report.dao.ReportTradeFeeCoinDao;

/**
 * 日手续费按币种统计Service
 * @author ruoyu
 * @version 2018-06-26
 */
@Service
@Transactional(readOnly = true)
public class ReportTradeFeeCoinService extends CrudService<ReportTradeFeeCoinDao, ReportTradeFeeCoin> {

	public ReportTradeFeeCoin get(String id) {
		return super.get(id);
	}
	
	public List<ReportTradeFeeCoin> findList(ReportTradeFeeCoin reportTradeFeeCoin) {
		return super.findList(reportTradeFeeCoin);
	}
	
	public Page<ReportTradeFeeCoin> findPage(Page<ReportTradeFeeCoin> page, ReportTradeFeeCoin reportTradeFeeCoin) {
		reportTradeFeeCoin.setPage(page);
		page.setList(dao.findListCount(reportTradeFeeCoin));
		return page;
	}
	
	public List<ReportTradeFeeCoin> findPageCount(Page<ReportTradeFeeCoin> page, ReportTradeFeeCoin reportTradeFeeCoin) {
		return this.dao.findListCount(reportTradeFeeCoin);
	}
	
	@Transactional(readOnly = false)
	public void save(ReportTradeFeeCoin reportTradeFeeCoin) {
		super.save(reportTradeFeeCoin);
	}
	
	@Transactional(readOnly = false)
	public void delete(ReportTradeFeeCoin reportTradeFeeCoin) {
		super.delete(reportTradeFeeCoin);
	}
	
}