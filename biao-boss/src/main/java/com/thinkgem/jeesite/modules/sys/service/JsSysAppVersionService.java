/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.JsSysAppVersion;
import com.thinkgem.jeesite.modules.sys.dao.JsSysAppVersionDao;

/**
 * App版本管理Service
 * @author zzj
 * @version 2018-08-27
 */
@Service
@Transactional(readOnly = true)
public class JsSysAppVersionService extends CrudService<JsSysAppVersionDao, JsSysAppVersion> {

	public JsSysAppVersion get(String id) {
		return super.get(id);
	}
	
	public List<JsSysAppVersion> findList(JsSysAppVersion jsSysAppVersion) {
		return super.findList(jsSysAppVersion);
	}
	
	public Page<JsSysAppVersion> findPage(Page<JsSysAppVersion> page, JsSysAppVersion jsSysAppVersion) {
		return super.findPage(page, jsSysAppVersion);
	}
	
	@Transactional(readOnly = false)
	public void save(JsSysAppVersion jsSysAppVersion) {
		super.save(jsSysAppVersion);
	}
	
	@Transactional(readOnly = false)
	public void delete(JsSysAppVersion jsSysAppVersion) {
		super.delete(jsSysAppVersion);
	}
	
}