/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.MkCoinDestroyRecord;

/**
 * 币种销毁记录DAO接口
 * @author zzj
 * @version 2018-10-09
 */
@MyBatisDao
public interface MkCoinDestroyRecordDao extends CrudDao<MkCoinDestroyRecord> {
	
}