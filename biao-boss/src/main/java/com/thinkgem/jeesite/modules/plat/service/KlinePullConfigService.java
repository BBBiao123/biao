/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.KlinePullConfig;
import com.thinkgem.jeesite.modules.plat.dao.KlinePullConfigDao;

/**
 * 币安k线配置Service
 * @author xiaoyu
 * @version 2018-12-25
 */
@Service
@Transactional(readOnly = true)
public class KlinePullConfigService extends CrudService<KlinePullConfigDao, KlinePullConfig> {

	public KlinePullConfig get(String id) {
		return super.get(id);
	}
	
	public List<KlinePullConfig> findList(KlinePullConfig klinePullConfig) {
		return super.findList(klinePullConfig);
	}
	
	public Page<KlinePullConfig> findPage(Page<KlinePullConfig> page, KlinePullConfig klinePullConfig) {
		return super.findPage(page, klinePullConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(KlinePullConfig klinePullConfig) {
		super.save(klinePullConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(KlinePullConfig klinePullConfig) {
		super.delete(klinePullConfig);
	}
	
}