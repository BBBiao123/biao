
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.Coin;

/**
 * 币种资料DAO接口
 * @author dazi
 * @version 2018-04-253
 */
@MyBatisDao
public interface CoinDao extends CrudDao<Coin> {
	
}