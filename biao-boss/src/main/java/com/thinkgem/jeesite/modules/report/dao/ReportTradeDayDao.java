/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.ReportTradeDay;

/**
 * 主区各币种交易量DAO接口
 * @author dazi
 * @version 2018-05-12
 */
@MyBatisDao
public interface ReportTradeDayDao extends CrudDao<ReportTradeDay> {
	
}