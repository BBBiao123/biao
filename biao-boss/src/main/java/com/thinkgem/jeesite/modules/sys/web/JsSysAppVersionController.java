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
import com.thinkgem.jeesite.modules.sys.entity.JsSysAppVersion;
import com.thinkgem.jeesite.modules.sys.service.JsSysAppVersionService;

/**
 * App版本管理Controller
 * @author zzj
 * @version 2018-08-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/jsSysAppVersion")
public class JsSysAppVersionController extends BaseController {

	@Autowired
	private JsSysAppVersionService jsSysAppVersionService;
	
	@ModelAttribute
	public JsSysAppVersion get(@RequestParam(required=false) String id) {
		JsSysAppVersion entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsSysAppVersionService.get(id);
		}
		if (entity == null){
			entity = new JsSysAppVersion();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:jsSysAppVersion:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsSysAppVersion jsSysAppVersion, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JsSysAppVersion> page = jsSysAppVersionService.findPage(new Page<JsSysAppVersion>(request, response), jsSysAppVersion); 
		model.addAttribute("page", page);
		return "modules/sys/jsSysAppVersionList";
	}

	@RequiresPermissions("sys:jsSysAppVersion:view")
	@RequestMapping(value = "form")
	public String form(JsSysAppVersion jsSysAppVersion, Model model) {
		model.addAttribute("jsSysAppVersion", jsSysAppVersion);
		return "modules/sys/jsSysAppVersionForm";
	}

	@RequiresPermissions("sys:jsSysAppVersion:edit")
	@RequestMapping(value = "save")
	public String save(JsSysAppVersion jsSysAppVersion, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsSysAppVersion)){
			return form(jsSysAppVersion, model);
		}
		jsSysAppVersionService.save(jsSysAppVersion);
		addMessage(redirectAttributes, "保存App版本管理成功");
		return "redirect:"+Global.getAdminPath()+"/sys/jsSysAppVersion/?repage";
	}
	
	@RequiresPermissions("sys:jsSysAppVersion:edit")
	@RequestMapping(value = "delete")
	public String delete(JsSysAppVersion jsSysAppVersion, RedirectAttributes redirectAttributes) {
		jsSysAppVersionService.delete(jsSysAppVersion);
		addMessage(redirectAttributes, "删除App版本管理成功");
		return "redirect:"+Global.getAdminPath()+"/sys/jsSysAppVersion/?repage";
	}

}