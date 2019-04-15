/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.EmailSendLog;
import com.thinkgem.jeesite.modules.sys.dao.EmailSendLogDao;

/**
 * 邮件发送日志管理Service
 * @author ruoyu
 * @version 2018-07-10
 */
@Service
@Transactional(readOnly = true)
public class EmailSendLogService extends CrudService<EmailSendLogDao, EmailSendLog> {

	public EmailSendLog get(String id) {
		return super.get(id);
	}
	
	public List<EmailSendLog> findList(EmailSendLog emailSendLog) {
		return super.findList(emailSendLog);
	}
	
	public Page<EmailSendLog> findPage(Page<EmailSendLog> page, EmailSendLog emailSendLog) {
		return super.findPage(page, emailSendLog);
	}
	
	@Transactional(readOnly = false)
	public void save(EmailSendLog emailSendLog) {
		super.save(emailSendLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(EmailSendLog emailSendLog) {
		super.delete(emailSendLog);
	}
	
}