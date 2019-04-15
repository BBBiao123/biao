/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.SuperCoinVolume;

/**
 * 超级钱包资产DAO接口
 * @author zzj
 * @version 2018-12-25
 */
@MyBatisDao
public interface SuperCoinVolumeDao extends CrudDao<SuperCoinVolume> {
	
}