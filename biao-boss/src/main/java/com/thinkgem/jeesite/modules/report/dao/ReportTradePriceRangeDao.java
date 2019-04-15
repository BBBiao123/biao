/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.ReportTradePriceRange;

/**
 * 币币交易价格区间统计
 * @author zzj
 * @version 2018-12-14
 */
@MyBatisDao
public interface ReportTradePriceRangeDao extends CrudDao<ReportTradePriceRange> {
	
}