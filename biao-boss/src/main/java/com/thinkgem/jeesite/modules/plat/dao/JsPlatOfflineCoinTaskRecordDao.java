/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineCoinTaskRecord;

/**
 * C2C币种价格更新记录DAO接口
 * @author zzj
 * @version 2018-10-09
 */
@MyBatisDao
public interface JsPlatOfflineCoinTaskRecordDao extends CrudDao<JsPlatOfflineCoinTaskRecord> {
	
}