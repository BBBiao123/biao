/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoin;
import org.apache.ibatis.annotations.Param;

/**
 * c2c_coinDAO接口
 * @author dazi
 * @version 2018-04-29
 */
@MyBatisDao
public interface OfflineCoinDao extends CrudDao<OfflineCoin> {

    void disable(@Param("id") String id, @Param("disable") Integer disable);

    OfflineCoin getByCoinId(String coinId);

    void isVolume(@Param("id") String id, @Param("isVolume") Integer isVolume);
	
}