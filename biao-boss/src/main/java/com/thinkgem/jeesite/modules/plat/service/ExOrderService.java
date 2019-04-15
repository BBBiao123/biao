/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.ExOrder;
import com.thinkgem.jeesite.modules.plat.dao.ExOrderDao;

/**
 * 币币订单Service
 * @author dazi
 * @version 2018-04-27
 */
@Service
@Transactional(readOnly = true)
public class ExOrderService extends CrudService<ExOrderDao, ExOrder> {

	public ExOrder get(String id) {
		return super.get(id);
	}
	
	public List<ExOrder> findList(ExOrder exOrder) {
		return super.findList(exOrder);
	}
	
	public Page<ExOrder> findPage(Page<ExOrder> page, ExOrder exOrder) {
		return super.findPage(page, exOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(ExOrder exOrder) {
		super.save(exOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(ExOrder exOrder) {
		super.delete(exOrder);
	}
	
}