/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.BalanceSheetSnapshot;

/**
 * 资产负债表DAO接口
 * @author zzj
 * @version 2019-01-08
 */
@MyBatisDao
public interface BalanceSheetSnapshotDao extends CrudDao<BalanceSheetSnapshot> {
	
}