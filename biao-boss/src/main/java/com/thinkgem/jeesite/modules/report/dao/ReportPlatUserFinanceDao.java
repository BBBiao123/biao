/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.ReportPlatUserFinance;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 金额盈收对帐报表DAO接口
 * @author zzj
 * @version 2018-10-10
 */
@MyBatisDao
public interface ReportPlatUserFinanceDao extends CrudDao<ReportPlatUserFinance> {
    public List<ReportPlatUserFinance> addList(Map<String, Object> map);
    public List<ReportPlatUserFinance> findPlatUserFinanceTotal(Map<String, Object> map);
	
}