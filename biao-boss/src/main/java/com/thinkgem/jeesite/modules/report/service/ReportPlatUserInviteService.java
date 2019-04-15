/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.dao.PlatUserDao;
import com.thinkgem.jeesite.modules.report.dao.ReportPlatUserFinanceDao;
import com.thinkgem.jeesite.modules.report.dao.ReportPlatUserInviteDao;
import com.thinkgem.jeesite.modules.report.entity.ReportPlatUserFinance;
import com.thinkgem.jeesite.modules.report.entity.ReportPlatUserInvite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 金额盈收对帐报表Service
 * @author zzj
 * @version 2018-10-10
 */
@Service
@Transactional(readOnly = true)
public class ReportPlatUserInviteService extends CrudService<ReportPlatUserInviteDao, ReportPlatUserInvite> {

	@Autowired
	private PlatUserDao platUserDao;

	public ReportPlatUserInvite get(String id) {
		return super.get(id);
	}
	
	public List<ReportPlatUserInvite> findList(ReportPlatUserInvite reportPlatUserReconciliation) {
		return super.findList(reportPlatUserReconciliation);
	}
	
	public Page<ReportPlatUserInvite> findPage(Page<ReportPlatUserInvite> page, ReportPlatUserInvite reportPlatUserReconciliation) {

		return super.findPage(page, reportPlatUserReconciliation);
	}

}