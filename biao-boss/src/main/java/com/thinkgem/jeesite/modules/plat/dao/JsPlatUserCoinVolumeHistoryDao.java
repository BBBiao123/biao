/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeHistory;

/**
 * 手动转账DAO接口
 * @author ruoyu
 * @version 2018-08-09
 */
@MyBatisDao
public interface JsPlatUserCoinVolumeHistoryDao extends CrudDao<JsPlatUserCoinVolumeHistory> {
	
}