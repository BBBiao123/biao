/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMiningHoldCoinQuery;

import java.util.List;

/**
 * 挖矿持币量查询DAO接口
 * @author dongfeng
 * @version 2018-09-06
 */
@MyBatisDao
public interface Mk2PopularizeMiningHoldCoinQueryDao extends CrudDao<Mk2PopularizeMiningHoldCoinQuery> {

    List<Mk2PopularizeMiningHoldCoinQuery> findListByUserId(Mk2PopularizeMiningHoldCoinQuery mk2PopularizeMiningHoldCoinQuery);
}