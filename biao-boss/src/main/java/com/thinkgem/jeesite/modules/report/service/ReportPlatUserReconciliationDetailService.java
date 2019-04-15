/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.dao.PlatUserDao;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.report.dao.ReportPlatUserReconciliationDetailDao;
import com.thinkgem.jeesite.modules.report.entity.ReportPlatUserReconciliationDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 查询会员资产明细核对表Service
 * @author zzj
 * @version 2018-10-10
 */
@Service
@Transactional(readOnly = true)
public class ReportPlatUserReconciliationDetailService extends CrudService<ReportPlatUserReconciliationDetailDao, ReportPlatUserReconciliationDetail> {

	@Autowired
	private PlatUserDao platUserDao;

	public ReportPlatUserReconciliationDetail get(String id) {
		return super.get(id);
	}
	
	public List<ReportPlatUserReconciliationDetail> findList(ReportPlatUserReconciliationDetail reportPlatUserReconciliationDetail) {
		this.findPlatUserByMobileOrMail(reportPlatUserReconciliationDetail);
		return super.findList(reportPlatUserReconciliationDetail);
	}
	
	public Page<ReportPlatUserReconciliationDetail> findPage(Page<ReportPlatUserReconciliationDetail> page, ReportPlatUserReconciliationDetail reportPlatUserReconciliationDetail) {
		this.findPlatUserByMobileOrMail(reportPlatUserReconciliationDetail);
		return super.findPage(page, reportPlatUserReconciliationDetail);
	}

	private void findPlatUserByMobileOrMail(ReportPlatUserReconciliationDetail reportPlatUserReconciliationDetail){
		if(StringUtils.isEmpty(reportPlatUserReconciliationDetail.getUserId())){
			PlatUser platUser = null;
			if(StringUtils.isNotEmpty(reportPlatUserReconciliationDetail.getMobile())){
				platUser = platUserDao.findByMobile(reportPlatUserReconciliationDetail.getMobile());
			}else{
				platUser = platUserDao.findByMail(reportPlatUserReconciliationDetail.getMail());
			}

			if(Objects.nonNull(platUser)){
				reportPlatUserReconciliationDetail.setUserId(platUser.getId());
			}
		}
	}

}