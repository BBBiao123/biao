/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.CoinCollection;

/**
 * 币种归集DAO接口
 * @author tt
 * @version 2019-03-18
 */
@MyBatisDao
public interface CoinCollectionDao extends CrudDao<CoinCollection> {
	
}