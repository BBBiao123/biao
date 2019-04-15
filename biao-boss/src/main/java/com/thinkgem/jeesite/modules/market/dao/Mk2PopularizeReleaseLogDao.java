/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeReleaseLog;

/**
 * 冻结数量释放记录DAO接口
 * @author dongfeng
 * @version 2018-08-09
 */
@MyBatisDao
public interface Mk2PopularizeReleaseLogDao extends CrudDao<Mk2PopularizeReleaseLog> {
	
}