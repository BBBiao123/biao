/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkDistributeLog;
import com.thinkgem.jeesite.modules.market.dao.MkDistributeLogDao;

/**
 * 营销营销账户资产流水Service
 * @author zhangzijun
 * @version 2018-07-06
 */
@Service
@Transactional(readOnly = true)
public class MkDistributeLogService extends CrudService<MkDistributeLogDao, MkDistributeLog> {

	public MkDistributeLog get(String id) {
		return super.get(id);
	}
	
	public List<MkDistributeLog> findList(MkDistributeLog mkDistributeLog) {
		return super.findList(mkDistributeLog);
	}
	
	public Page<MkDistributeLog> findPage(Page<MkDistributeLog> page, MkDistributeLog mkDistributeLog) {
		return super.findPage(page, mkDistributeLog);
	}
	
	@Transactional(readOnly = false)
	public void save(MkDistributeLog mkDistributeLog) {
		super.save(mkDistributeLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkDistributeLog mkDistributeLog) {
		super.delete(mkDistributeLog);
	}
	
}