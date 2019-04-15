/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.dao.ReportUserOfflineReconciliationDao;
import com.thinkgem.jeesite.modules.report.entity.ReportUserOfflineReconciliation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 金额个人盈收对帐报表Service
 * @author zzj
 * @version 2018-11-26
 */
@Service
@Transactional(readOnly = true)
public class ReportUserOfflineReconciliationService extends CrudService<ReportUserOfflineReconciliationDao, ReportUserOfflineReconciliation> {

	public ReportUserOfflineReconciliation get(String id) {
		return super.get(id);
	}
	
	public List<ReportUserOfflineReconciliation> findList(ReportUserOfflineReconciliation reportUserOfflineReconciliation) {
		return super.findList(reportUserOfflineReconciliation);
	}
	
	public Page<ReportUserOfflineReconciliation> findPage(Page<ReportUserOfflineReconciliation> page, ReportUserOfflineReconciliation reportUserOfflineReconciliation) {
		return super.findPage(page, reportUserOfflineReconciliation);
	}

}