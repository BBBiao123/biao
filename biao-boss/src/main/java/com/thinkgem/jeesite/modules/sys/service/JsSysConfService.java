/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.JsSysConf;
import com.thinkgem.jeesite.modules.sys.dao.JsSysConfDao;

/**
 * Plat系统配置Service
 * @author zzj
 * @version 2019-03-05
 */
@Service
@Transactional(readOnly = true)
public class JsSysConfService extends CrudService<JsSysConfDao, JsSysConf> {

	public JsSysConf get(String id) {
		return super.get(id);
	}
	
	public List<JsSysConf> findList(JsSysConf jsSysConf) {
		return super.findList(jsSysConf);
	}
	
	public Page<JsSysConf> findPage(Page<JsSysConf> page, JsSysConf jsSysConf) {
		return super.findPage(page, jsSysConf);
	}
	
	@Transactional(readOnly = false)
	public void save(JsSysConf jsSysConf) {
		super.save(jsSysConf);
	}
	
	@Transactional(readOnly = false)
	public void delete(JsSysConf jsSysConf) {
		super.delete(jsSysConf);
	}
	
}