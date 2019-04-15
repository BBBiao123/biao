/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.Mk2PopularizeRegisterCoin;

/**
 * 注册用户送币DAO接口
 * @author dongfeng
 * @version 2018-07-20
 */
@MyBatisDao
public interface Mk2PopularizeRegisterCoinDao extends CrudDao<Mk2PopularizeRegisterCoin> {
	
}