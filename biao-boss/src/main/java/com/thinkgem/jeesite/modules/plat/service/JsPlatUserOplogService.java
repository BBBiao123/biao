/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserOplog;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatUserOplogDao;

/**
 * 会员操作日志Service
 * @author zzj
 * @version 2018-11-07
 */
@Service
@Transactional(readOnly = true)
public class JsPlatUserOplogService extends CrudService<JsPlatUserOplogDao, JsPlatUserOplog> {

	public JsPlatUserOplog get(String id) {
		return super.get(id);
	}
	
	public List<JsPlatUserOplog> findList(JsPlatUserOplog jsPlatUserOplog) {
		return super.findList(jsPlatUserOplog);
	}
	
	public Page<JsPlatUserOplog> findPage(Page<JsPlatUserOplog> page, JsPlatUserOplog jsPlatUserOplog) {
		return super.findPage(page, jsPlatUserOplog);
	}
	
	@Transactional(readOnly = false)
	public void save(JsPlatUserOplog jsPlatUserOplog) {
		super.save(jsPlatUserOplog);
	}
	
	@Transactional(readOnly = false)
	public void delete(JsPlatUserOplog jsPlatUserOplog) {
		super.delete(jsPlatUserOplog);
	}
	
}