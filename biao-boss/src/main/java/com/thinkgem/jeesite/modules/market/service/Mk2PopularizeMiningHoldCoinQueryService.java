/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMiningHoldCoinQuery;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeMiningHoldCoinQueryDao;

/**
 * 挖矿持币量查询Service
 * @author dongfeng
 * @version 2018-09-06
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeMiningHoldCoinQueryService extends CrudService<Mk2PopularizeMiningHoldCoinQueryDao, Mk2PopularizeMiningHoldCoinQuery> {

	public List<Mk2PopularizeMiningHoldCoinQuery> findListByUserId(Mk2PopularizeMiningHoldCoinQuery mk2PopularizeMiningHoldCoinQuery) {
		if (StringUtils.isBlank(mk2PopularizeMiningHoldCoinQuery.getUserId())) {
			return null;
		}
		return dao.findListByUserId(mk2PopularizeMiningHoldCoinQuery);
	}
	
}