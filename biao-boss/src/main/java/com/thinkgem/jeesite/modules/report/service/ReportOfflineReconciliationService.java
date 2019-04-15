/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.dao.ReportOfflineReconciliationDao;
import com.thinkgem.jeesite.modules.report.dao.ReportTradeDayDao;
import com.thinkgem.jeesite.modules.report.entity.ReportOfflineReconciliation;
import com.thinkgem.jeesite.modules.report.entity.ReportTradeDay;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 金额盈收对帐报表Service
 * @author zzj
 * @version 2018-10-08
 */
@Service
@Transactional(readOnly = true)
public class ReportOfflineReconciliationService extends CrudService<ReportOfflineReconciliationDao, ReportOfflineReconciliation> {

	public ReportOfflineReconciliation get(String id) {
		return super.get(id);
	}
	
	public List<ReportOfflineReconciliation> findList(ReportOfflineReconciliation reportOfflineReconciliation) {
		return super.findList(reportOfflineReconciliation);
	}
	
	public Page<ReportOfflineReconciliation> findPage(Page<ReportOfflineReconciliation> page, ReportOfflineReconciliation reportOfflineReconciliation) {
		return super.findPage(page, reportOfflineReconciliation);
	}

}