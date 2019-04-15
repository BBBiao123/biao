/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.dao.ReportOfflineFeeDao;
import com.thinkgem.jeesite.modules.report.dao.ReportOfflineReconciliationDao;
import com.thinkgem.jeesite.modules.report.entity.ReportOfflineFee;
import com.thinkgem.jeesite.modules.report.entity.ReportOfflineReconciliation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 统计C2C手续费报表Service
 * @author zzj
 * @version 2018-10-08
 */
@Service
@Transactional(readOnly = true)
public class ReportOfflineFeeService extends CrudService<ReportOfflineFeeDao, ReportOfflineFee> {

	public ReportOfflineFee get(String id) {
		return super.get(id);
	}
	
	public List<ReportOfflineFee> findList(ReportOfflineFee reportOfflineFee) {
		return super.findList(reportOfflineFee);
	}
	
	public Page<ReportOfflineFee> findPage(Page<ReportOfflineFee> page, ReportOfflineFee reportOfflineFee) {
		return super.findPage(page, reportOfflineFee);
	}

}