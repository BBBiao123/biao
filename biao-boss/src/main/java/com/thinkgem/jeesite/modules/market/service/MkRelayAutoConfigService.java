/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkRelayAutoConfig;
import com.thinkgem.jeesite.modules.market.dao.MkRelayAutoConfigDao;

/**
 * 接力自动撞奖配置Service
 * @author zzj
 * @version 2018-09-26
 */
@Service
@Transactional(readOnly = true)
public class MkRelayAutoConfigService extends CrudService<MkRelayAutoConfigDao, MkRelayAutoConfig> {

	public MkRelayAutoConfig get(String id) {
		return super.get(id);
	}
	
	public List<MkRelayAutoConfig> findList(MkRelayAutoConfig mkRelayAutoConfig) {
		return super.findList(mkRelayAutoConfig);
	}
	
	public Page<MkRelayAutoConfig> findPage(Page<MkRelayAutoConfig> page, MkRelayAutoConfig mkRelayAutoConfig) {
		return super.findPage(page, mkRelayAutoConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(MkRelayAutoConfig mkRelayAutoConfig) {
		super.save(mkRelayAutoConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkRelayAutoConfig mkRelayAutoConfig) {
		super.delete(mkRelayAutoConfig);
	}
	
}