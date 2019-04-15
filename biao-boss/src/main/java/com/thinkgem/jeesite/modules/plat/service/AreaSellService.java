/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.AreaSell;
import com.thinkgem.jeesite.modules.plat.dao.AreaSellDao;

/**
 * 区域销售Service
 * @author dong
 * @version 2018-07-06
 */
@Service
@Transactional(readOnly = true)
public class AreaSellService extends CrudService<AreaSellDao, AreaSell> {

	public AreaSell get(String id) {
		return super.get(id);
	}
	
	public List<AreaSell> findList(AreaSell areaSell) {
		return super.findList(areaSell);
	}
	
	public Page<AreaSell> findPage(Page<AreaSell> page, AreaSell areaSell) {
		return super.findPage(page, areaSell);
	}
	
	@Transactional(readOnly = false)
	public void save(AreaSell areaSell) {
		super.save(areaSell);
	}
	
	@Transactional(readOnly = false)
	public void delete(AreaSell areaSell) {
		super.delete(areaSell);
	}
	
}