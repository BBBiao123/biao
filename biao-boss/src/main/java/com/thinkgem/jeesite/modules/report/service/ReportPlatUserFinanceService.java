/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.dao.PlatUserDao;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.report.dao.ReportPlatUserFinanceDao;
import com.thinkgem.jeesite.modules.report.dao.ReportPlatUserReconciliationDao;
import com.thinkgem.jeesite.modules.report.entity.ReportPlatUserFinance;
import com.thinkgem.jeesite.modules.report.entity.ReportPlatUserReconciliation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 金额盈收对帐报表Service
 * @author zzj
 * @version 2018-10-10
 */
@Service
@Transactional(readOnly = true)
public class ReportPlatUserFinanceService extends CrudService<ReportPlatUserFinanceDao, ReportPlatUserFinance> {

	@Autowired
	private PlatUserDao platUserDao;

	@Autowired
	private ReportPlatUserFinanceDao reportPlatUserFinanceDao;

	public ReportPlatUserFinance get(String id) {
		return super.get(id);
	}
	
	public List<ReportPlatUserFinance> findList(ReportPlatUserFinance reportPlatUserReconciliation) {
		return super.findList(reportPlatUserReconciliation);
	}
	
	public Page<ReportPlatUserFinance> findPage(Page<ReportPlatUserFinance> page, ReportPlatUserFinance reportPlatUserReconciliation) {

		return super.findPage(page, reportPlatUserReconciliation);
	}
	public Page<ReportPlatUserFinance> findPage(Page<ReportPlatUserFinance> page, Map<String, Object> map) {
		page.setList(reportPlatUserFinanceDao.addList(map));
		return page;
	}
	public List<ReportPlatUserFinance> findPlatUserFinanceTotal(Map<String, Object> map) {
		return reportPlatUserFinanceDao.findPlatUserFinanceTotal(map);
	}

	public void findReportPlatUserFinance(ReportPlatUserFinance reportPlatUserFinance) {

		if (StringUtils.isEmpty(reportPlatUserFinance.getUserId())) {
			PlatUser platUser = null;
			if (StringUtils.isNotEmpty(reportPlatUserFinance.getMobile())) {
				platUser = platUserDao.findByMobile(reportPlatUserFinance.getMobile());
			} else {
				platUser = platUserDao.findByMail(reportPlatUserFinance.getMail());
			}

			if (Objects.nonNull(platUser)) {
				reportPlatUserFinance.setUserId(platUser.getId());
			}
		}
	}
}