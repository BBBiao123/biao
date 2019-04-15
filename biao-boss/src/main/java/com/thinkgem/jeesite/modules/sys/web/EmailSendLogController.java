/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.EmailSendLog;
import com.thinkgem.jeesite.modules.sys.service.EmailSendLogService;

/**
 * 邮件发送日志管理Controller
 * @author ruoyu
 * @version 2018-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/emailSendLog")
public class EmailSendLogController extends BaseController {

	@Autowired
	private EmailSendLogService emailSendLogService;
	
	@ModelAttribute
	public EmailSendLog get(@RequestParam(required=false) String id) {
		EmailSendLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = emailSendLogService.get(id);
		}
		if (entity == null){
			entity = new EmailSendLog();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:emailSendLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(EmailSendLog emailSendLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EmailSendLog> page = emailSendLogService.findPage(new Page<EmailSendLog>(request, response), emailSendLog); 
		model.addAttribute("page", page);
		return "modules/sys/emailSendLogList";
	}

	@RequiresPermissions("sys:emailSendLog:view")
	@RequestMapping(value = "form")
	public String form(EmailSendLog emailSendLog, Model model) {
		model.addAttribute("emailSendLog", emailSendLog);
		return "modules/sys/emailSendLogForm";
	}

	@RequiresPermissions("sys:emailSendLog:edit")
	@RequestMapping(value = "save")
	public String save(EmailSendLog emailSendLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, emailSendLog)){
			return form(emailSendLog, model);
		}
		emailSendLogService.save(emailSendLog);
		addMessage(redirectAttributes, "保存邮件发送日志管理成功");
		return "redirect:"+Global.getAdminPath()+"/sys/emailSendLog/?repage";
	}
	
	@RequiresPermissions("sys:emailSendLog:edit")
	@RequestMapping(value = "delete")
	public String delete(EmailSendLog emailSendLog, RedirectAttributes redirectAttributes) {
		emailSendLogService.delete(emailSendLog);
		addMessage(redirectAttributes, "删除邮件发送日志管理成功");
		return "redirect:"+Global.getAdminPath()+"/sys/emailSendLog/?repage";
	}

}