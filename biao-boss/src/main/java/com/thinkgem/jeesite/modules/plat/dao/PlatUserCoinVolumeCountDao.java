/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.PlatUserCoinVolumeCount;

import java.util.List;

/**
 * 持币数量统计DAO接口
 * @author dongfeng
 * @version 2018-09-04
 */
@MyBatisDao
public interface PlatUserCoinVolumeCountDao extends CrudDao<PlatUserCoinVolumeCount> {
    List<PlatUserCoinVolumeCount> queryByDate(PlatUserCoinVolumeCount param);
}