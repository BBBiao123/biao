/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.MkCommonUserCoinFee;

/**
 * 平台收益流水DAO接口
 * @author dongfeng
 * @version 2018-08-09
 */
@MyBatisDao
public interface MkCommonUserCoinFeeDao extends CrudDao<MkCommonUserCoinFee> {
	
}