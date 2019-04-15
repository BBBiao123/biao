/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeBill;

/**
 * 用户资产账单DAO接口
 * @author ruoyu
 * @version 2019-01-02
 */
@MyBatisDao
public interface JsPlatUserCoinVolumeBillDao extends CrudDao<JsPlatUserCoinVolumeBill> {
	
}