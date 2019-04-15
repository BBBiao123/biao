/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.EmailTemplate;
import com.thinkgem.jeesite.modules.sys.dao.EmailTemplateDao;

/**
 * 邮件模板管理Service
 * @author ruoyu
 * @version 2018-07-10
 */
@Service
@Transactional(readOnly = true)
public class EmailTemplateService extends CrudService<EmailTemplateDao, EmailTemplate> {

	public EmailTemplate get(String id) {
		return super.get(id);
	}
	
	public List<EmailTemplate> findList(EmailTemplate emailTemplate) {
		return super.findList(emailTemplate);
	}
	
	public Page<EmailTemplate> findPage(Page<EmailTemplate> page, EmailTemplate emailTemplate) {
		return super.findPage(page, emailTemplate);
	}
	
	@Transactional(readOnly = false)
	public void save(EmailTemplate emailTemplate) {
		super.save(emailTemplate);
	}
	
	@Transactional(readOnly = false)
	public void delete(EmailTemplate emailTemplate) {
		super.delete(emailTemplate);
	}
	
}