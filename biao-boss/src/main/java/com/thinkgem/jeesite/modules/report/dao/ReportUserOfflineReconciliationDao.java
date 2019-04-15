/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.ReportUserOfflineReconciliation;

/**
 * 金额个人盈收对帐报表DAO接口
 * @author zzj
 * @version 2018-11-26
 */
@MyBatisDao
public interface ReportUserOfflineReconciliationDao extends CrudDao<ReportUserOfflineReconciliation> {
	
}