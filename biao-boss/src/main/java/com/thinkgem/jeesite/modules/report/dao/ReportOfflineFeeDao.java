/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.ReportOfflineFee;
import com.thinkgem.jeesite.modules.report.entity.ReportOfflineReconciliation;

/**
 * 统计C2C手续费报表DAO接口
 * @author zzj
 * @version 2018-10-08
 */
@MyBatisDao
public interface ReportOfflineFeeDao extends CrudDao<ReportOfflineFee> {
	
}