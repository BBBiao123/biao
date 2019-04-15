/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatCardStatusConfig;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatCardStatusConfigDao;

/**
 * 实名认证限制Service 
 * @author ruoyu
 * @version 2018-11-27
 */
@Service
@Transactional(readOnly = true)
public class JsPlatCardStatusConfigService extends CrudService<JsPlatCardStatusConfigDao, JsPlatCardStatusConfig> {

	public JsPlatCardStatusConfig get(String id) {
		return super.get(id);
	}
	
	public List<JsPlatCardStatusConfig> findList(JsPlatCardStatusConfig jsPlatCardStatusConfig) {
		return super.findList(jsPlatCardStatusConfig);
	}
	
	public Page<JsPlatCardStatusConfig> findPage(Page<JsPlatCardStatusConfig> page, JsPlatCardStatusConfig jsPlatCardStatusConfig) {
		return super.findPage(page, jsPlatCardStatusConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(JsPlatCardStatusConfig jsPlatCardStatusConfig) {
		super.save(jsPlatCardStatusConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(JsPlatCardStatusConfig jsPlatCardStatusConfig) {
		super.delete(jsPlatCardStatusConfig);
	}
	
}