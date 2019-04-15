/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.MobileTemplate;
import com.thinkgem.jeesite.modules.sys.dao.MobileTemplateDao;

/**
 * ddService
 * @author ruoyu
 * @version 2018-06-28
 */
@Service
@Transactional(readOnly = true)
public class MobileTemplateService extends CrudService<MobileTemplateDao, MobileTemplate> {

	public MobileTemplate get(String id) {
		return super.get(id);
	}
	
	public List<MobileTemplate> findList(MobileTemplate mobileTemplate) {
		return super.findList(mobileTemplate);
	}
	
	public Page<MobileTemplate> findPage(Page<MobileTemplate> page, MobileTemplate mobileTemplate) {
		return super.findPage(page, mobileTemplate);
	}
	
	@Transactional(readOnly = false)
	public void save(MobileTemplate mobileTemplate) {
		super.save(mobileTemplate);
	}
	
	@Transactional(readOnly = false)
	public void delete(MobileTemplate mobileTemplate) {
		super.delete(mobileTemplate);
	}
	
}