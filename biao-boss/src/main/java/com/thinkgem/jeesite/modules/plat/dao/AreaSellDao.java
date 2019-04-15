/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.AreaSell;

/**
 * 区域销售DAO接口
 * @author dong
 * @version 2018-07-06
 */
@MyBatisDao
public interface AreaSellDao extends CrudDao<AreaSell> {
	
}