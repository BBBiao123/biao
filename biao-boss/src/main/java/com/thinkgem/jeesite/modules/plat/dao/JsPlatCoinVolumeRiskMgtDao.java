/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatCoinVolumeRiskMgt;

/**
 * 币种资产风控管理DAO接口
 * @author zzj
 * @version 2019-01-18
 */
@MyBatisDao
public interface JsPlatCoinVolumeRiskMgtDao extends CrudDao<JsPlatCoinVolumeRiskMgt> {
	
}