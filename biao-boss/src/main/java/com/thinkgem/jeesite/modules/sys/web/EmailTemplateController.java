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
import com.thinkgem.jeesite.modules.sys.entity.EmailTemplate;
import com.thinkgem.jeesite.modules.sys.service.EmailTemplateService;

/**
 * 邮件模板管理Controller
 * @author ruoyu
 * @version 2018-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/emailTemplate")
public class EmailTemplateController extends BaseController {

	@Autowired
	private EmailTemplateService emailTemplateService;
	
	@ModelAttribute
	public EmailTemplate get(@RequestParam(required=false) String id) {
		EmailTemplate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = emailTemplateService.get(id);
		}
		if (entity == null){
			entity = new EmailTemplate();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:emailTemplate:view")
	@RequestMapping(value = {"list", ""})
	public String list(EmailTemplate emailTemplate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EmailTemplate> page = emailTemplateService.findPage(new Page<EmailTemplate>(request, response), emailTemplate); 
		model.addAttribute("page", page);
		return "modules/sys/emailTemplateList";
	}

	@RequiresPermissions("sys:emailTemplate:view")
	@RequestMapping(value = "form")
	public String form(EmailTemplate emailTemplate, Model model) {
		model.addAttribute("emailTemplate", emailTemplate);
		return "modules/sys/emailTemplateForm";
	}

	@RequiresPermissions("sys:emailTemplate:edit")
	@RequestMapping(value = "save")
	public String save(EmailTemplate emailTemplate, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, emailTemplate)){
			return form(emailTemplate, model);
		}
		emailTemplateService.save(emailTemplate);
		addMessage(redirectAttributes, "保存邮件模板管理成功");
		return "redirect:"+Global.getAdminPath()+"/sys/emailTemplate/?repage";
	}
	
	@RequiresPermissions("sys:emailTemplate:edit")
	@RequestMapping(value = "delete")
	public String delete(EmailTemplate emailTemplate, RedirectAttributes redirectAttributes) {
		emailTemplateService.delete(emailTemplate);
		addMessage(redirectAttributes, "删除邮件模板管理成功");
		return "redirect:"+Global.getAdminPath()+"/sys/emailTemplate/?repage";
	}

}