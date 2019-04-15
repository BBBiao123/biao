/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatOfflineOrderDetailLogDao;
import com.thinkgem.jeesite.modules.plat.dao.OfflineOrderDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineOrderDetailLog;

/**
 * c2c流水统计表Service
 * @author ruoyu
 * @version 2018-10-24
 */
@Service
@Transactional(readOnly = true)
public class JsPlatOfflineOrderDetailLogService extends CrudService<JsPlatOfflineOrderDetailLogDao, JsPlatOfflineOrderDetailLog> {

	@Autowired
	private OfflineOrderDao offlineOrderDao ;
	
	public JsPlatOfflineOrderDetailLog get(String id) {
		return super.get(id);
	}
	
	public List<JsPlatOfflineOrderDetailLog> findList(JsPlatOfflineOrderDetailLog jsPlatOfflineOrderDetailLog) {
		return super.findList(jsPlatOfflineOrderDetailLog);
	}
	
	public Page<JsPlatOfflineOrderDetailLog> findPage(Page<JsPlatOfflineOrderDetailLog> page, JsPlatOfflineOrderDetailLog jsPlatOfflineOrderDetailLog) {
		return super.findPage(page, jsPlatOfflineOrderDetailLog);
	}
	
	@Transactional(readOnly = false)
	public void save(JsPlatOfflineOrderDetailLog jsPlatOfflineOrderDetailLog) {
		super.save(jsPlatOfflineOrderDetailLog);
		//删除 2和9的订单详情
		this.dao.batchInsert(jsPlatOfflineOrderDetailLog.getCountDate());
		this.dao.batchDelete(jsPlatOfflineOrderDetailLog.getCountDate());
		
		//删除这段时间的广告  状态1和9
		offlineOrderDao.deleteByDateInsert(jsPlatOfflineOrderDetailLog.getCountDate());
		offlineOrderDao.deleteByDate(jsPlatOfflineOrderDetailLog.getCountDate());
		
		//删除这段时间的广告  状态3且订单详情中不存在的广告
		offlineOrderDao.deleteByDateAndStatusInsert(jsPlatOfflineOrderDetailLog.getCountDate());
		offlineOrderDao.deleteByDateAndStatus(jsPlatOfflineOrderDetailLog.getCountDate());
		
	}
	
	@Transactional(readOnly = false)
	public void delete(JsPlatOfflineOrderDetailLog jsPlatOfflineOrderDetailLog) {
		super.delete(jsPlatOfflineOrderDetailLog);
	}
	
}