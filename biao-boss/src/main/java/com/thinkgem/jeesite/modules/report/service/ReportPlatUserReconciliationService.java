/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.dao.PlatUserDao;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.report.dao.ReportPlatUserReconciliationDao;
import com.thinkgem.jeesite.modules.report.entity.ReportPlatUserReconciliation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 金额盈收对帐报表Service
 * @author zzj
 * @version 2018-10-10
 */
@Service
@Transactional(readOnly = true)
public class ReportPlatUserReconciliationService extends CrudService<ReportPlatUserReconciliationDao, ReportPlatUserReconciliation> {

	@Autowired
	private PlatUserDao platUserDao;

	public ReportPlatUserReconciliation get(String id) {
		return super.get(id);
	}
	
	public List<ReportPlatUserReconciliation> findList(ReportPlatUserReconciliation reportPlatUserReconciliation) {
		return super.findList(reportPlatUserReconciliation);
	}
	
	public Page<ReportPlatUserReconciliation> findPage(Page<ReportPlatUserReconciliation> page, ReportPlatUserReconciliation reportPlatUserReconciliation) {
		if(StringUtils.isEmpty(reportPlatUserReconciliation.getUserId())){
			PlatUser platUser = null;
			if(StringUtils.isNotEmpty(reportPlatUserReconciliation.getMobile())){
				platUser = platUserDao.findByMobile(reportPlatUserReconciliation.getMobile());
			}else{
				platUser = platUserDao.findByMail(reportPlatUserReconciliation.getMail());
			}

			if(Objects.nonNull(platUser)){
				reportPlatUserReconciliation.setUserId(platUser.getId());
			}
		}
		return super.findPage(page, reportPlatUserReconciliation);
	}

}