/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

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
import com.thinkgem.jeesite.modules.plat.entity.JsPlatCardStatusConfig;
import com.thinkgem.jeesite.modules.plat.service.JsPlatCardStatusConfigService;

/**
 * 实名认证限制Controller
 * @author ruoyu
 * @version 2018-11-27
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/jsPlatCardStatusConfig")
public class JsPlatCardStatusConfigController extends BaseController {

	@Autowired
	private JsPlatCardStatusConfigService jsPlatCardStatusConfigService;
	
	@ModelAttribute
	public JsPlatCardStatusConfig get(@RequestParam(required=false) String id) {
		JsPlatCardStatusConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsPlatCardStatusConfigService.get(id);
		}
		if (entity == null){
			entity = new JsPlatCardStatusConfig();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:jsPlatCardStatusConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsPlatCardStatusConfig jsPlatCardStatusConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JsPlatCardStatusConfig> page = jsPlatCardStatusConfigService.findPage(new Page<JsPlatCardStatusConfig>(request, response), jsPlatCardStatusConfig); 
		model.addAttribute("page", page);
		return "modules/plat/jsPlatCardStatusConfigList";
	}

	@RequiresPermissions("plat:jsPlatCardStatusConfig:view")
	@RequestMapping(value = "form")
	public String form(JsPlatCardStatusConfig jsPlatCardStatusConfig, Model model) {
		model.addAttribute("jsPlatCardStatusConfig", jsPlatCardStatusConfig);
		return "modules/plat/jsPlatCardStatusConfigForm";
	}

	@RequiresPermissions("plat:jsPlatCardStatusConfig:edit")
	@RequestMapping(value = "save")
	public String save(JsPlatCardStatusConfig jsPlatCardStatusConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsPlatCardStatusConfig)){
			return form(jsPlatCardStatusConfig, model);
		}
		jsPlatCardStatusConfigService.save(jsPlatCardStatusConfig);
		addMessage(redirectAttributes, "保存实名认证限制成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatCardStatusConfig/?repage";
	}
	
	@RequiresPermissions("plat:jsPlatCardStatusConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(JsPlatCardStatusConfig jsPlatCardStatusConfig, RedirectAttributes redirectAttributes) {
		jsPlatCardStatusConfigService.delete(jsPlatCardStatusConfig);
		addMessage(redirectAttributes, "删除实名认证限制成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatCardStatusConfig/?repage";
	}

}