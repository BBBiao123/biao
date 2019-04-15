/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.PlatDepositAndWithdrawStat;

/**
 * 平台充值提现统计DAO接口
 * @author zzj
 * @version 2018-12-21
 */
@MyBatisDao
public interface PlatDepositAndWithdrawStatDao extends CrudDao<PlatDepositAndWithdrawStat> {
	
}