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
import com.thinkgem.jeesite.modules.sys.entity.JsSysConf;
import com.thinkgem.jeesite.modules.sys.service.JsSysConfService;

/**
 * Plat系统配置Controller
 * @author zzj
 * @version 2019-03-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/jsSysConf")
public class JsSysConfController extends BaseController {

	@Autowired
	private JsSysConfService jsSysConfService;
	
	@ModelAttribute
	public JsSysConf get(@RequestParam(required=false) String id) {
		JsSysConf entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsSysConfService.get(id);
		}
		if (entity == null){
			entity = new JsSysConf();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:jsSysConf:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsSysConf jsSysConf, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JsSysConf> page = jsSysConfService.findPage(new Page<JsSysConf>(request, response), jsSysConf); 
		model.addAttribute("page", page);
		return "modules/sys/jsSysConfList";
	}

	@RequiresPermissions("sys:jsSysConf:view")
	@RequestMapping(value = "form")
	public String form(JsSysConf jsSysConf, Model model) {
		model.addAttribute("jsSysConf", jsSysConf);
		return "modules/sys/jsSysConfForm";
	}

	@RequiresPermissions("sys:jsSysConf:edit")
	@RequestMapping(value = "save")
	public String save(JsSysConf jsSysConf, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsSysConf)){
			return form(jsSysConf, model);
		}
		jsSysConfService.save(jsSysConf);
		addMessage(redirectAttributes, "保存Plat系统配置成功");
		return "redirect:"+Global.getAdminPath()+"/sys/jsSysConf/?repage";
	}
	
	@RequiresPermissions("sys:jsSysConf:edit")
	@RequestMapping(value = "delete")
	public String delete(JsSysConf jsSysConf, RedirectAttributes redirectAttributes) {
		jsSysConfService.delete(jsSysConf);
		addMessage(redirectAttributes, "删除Plat系统配置成功");
		return "redirect:"+Global.getAdminPath()+"/sys/jsSysConf/?repage";
	}

}