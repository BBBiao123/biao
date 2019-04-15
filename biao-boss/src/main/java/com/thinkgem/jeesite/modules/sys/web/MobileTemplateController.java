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
import com.thinkgem.jeesite.modules.sys.entity.MobileTemplate;
import com.thinkgem.jeesite.modules.sys.service.MobileTemplateService;

/**
 * ddController
 * @author ruoyu
 * @version 2018-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/mobileTemplate")
public class MobileTemplateController extends BaseController {

	@Autowired
	private MobileTemplateService mobileTemplateService;
	
	@ModelAttribute
	public MobileTemplate get(@RequestParam(required=false) String id) {
		MobileTemplate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mobileTemplateService.get(id);
		}
		if (entity == null){
			entity = new MobileTemplate();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:mobileTemplate:view")
	@RequestMapping(value = {"list", ""})
	public String list(MobileTemplate mobileTemplate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MobileTemplate> page = mobileTemplateService.findPage(new Page<MobileTemplate>(request, response), mobileTemplate); 
		model.addAttribute("page", page);
		return "modules/sys/mobileTemplateList";
	}

	@RequiresPermissions("sys:mobileTemplate:view")
	@RequestMapping(value = "form")
	public String form(MobileTemplate mobileTemplate, Model model) {
		model.addAttribute("mobileTemplate", mobileTemplate);
		return "modules/sys/mobileTemplateForm";
	}

	@RequiresPermissions("sys:mobileTemplate:edit")
	@RequestMapping(value = "save")
	public String save(MobileTemplate mobileTemplate, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mobileTemplate)){
			return form(mobileTemplate, model);
		}
		mobileTemplateService.save(mobileTemplate);
		addMessage(redirectAttributes, "保存dd成功");
		return "redirect:"+Global.getAdminPath()+"/sys/mobileTemplate/?repage";
	}
	
	@RequiresPermissions("sys:mobileTemplate:edit")
	@RequestMapping(value = "delete")
	public String delete(MobileTemplate mobileTemplate, RedirectAttributes redirectAttributes) {
		mobileTemplateService.delete(mobileTemplate);
		addMessage(redirectAttributes, "删除dd成功");
		return "redirect:"+Global.getAdminPath()+"/sys/mobileTemplate/?repage";
	}

}