/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.ExPair;

/**
 * 币币交易对DAO接口
 * @author dazi
 * @version 2018-04-26
 */
@MyBatisDao
public interface ExPairDao extends CrudDao<ExPair> {
	
}