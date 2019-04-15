/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.CoinCollection;
import com.thinkgem.jeesite.modules.plat.dao.CoinCollectionDao;

/**
 * 币种归集Service
 * @author tt
 * @version 2019-03-18
 */
@Service
@Transactional(readOnly = true)
public class CoinCollectionService extends CrudService<CoinCollectionDao, CoinCollection> {

	public CoinCollection get(String id) {
		return super.get(id);
	}
	
	public List<CoinCollection> findList(CoinCollection coinCollection) {
		return super.findList(coinCollection);
	}
	
	public Page<CoinCollection> findPage(Page<CoinCollection> page, CoinCollection coinCollection) {
		return super.findPage(page, coinCollection);
	}
	
	@Transactional(readOnly = false)
	public void save(CoinCollection coinCollection) {
		super.save(coinCollection);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoinCollection coinCollection) {
		super.delete(coinCollection);
	}
	
}