/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.ReportTradeFeeCoin;

/**
 * 日手续费按币种统计DAO接口
 * @author ruoyu
 * @version 2018-06-26
 */
@MyBatisDao
public interface ReportTradeFeeCoinDao extends CrudDao<ReportTradeFeeCoin> {
	
	public List<ReportTradeFeeCoin> findListCount(ReportTradeFeeCoin entity);
}