/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.OfflineOrderDetail;
import com.thinkgem.jeesite.modules.plat.dao.OfflineOrderDetailDao;

/**
 * c2c广告详情Service
 * @author dazi
 * @version 2018-04-30
 */
@Service
@Transactional(readOnly = true)
public class OfflineOrderDetailService extends CrudService<OfflineOrderDetailDao, OfflineOrderDetail> {

	public OfflineOrderDetail get(String id) {
		return super.get(id);
	}
	
	public List<OfflineOrderDetail> findList(OfflineOrderDetail offlineOrderDetail) {
		return super.findList(offlineOrderDetail);
	}
	
	public Page<OfflineOrderDetail> findPage(Page<OfflineOrderDetail> page, OfflineOrderDetail offlineOrderDetail) {
		return super.findPage(page, offlineOrderDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(OfflineOrderDetail offlineOrderDetail) {
		super.save(offlineOrderDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(OfflineOrderDetail offlineOrderDetail) {
		super.delete(offlineOrderDetail);
	}
	
}