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
import com.thinkgem.jeesite.modules.plat.entity.DestroyAccountLog;
import com.thinkgem.jeesite.modules.plat.service.DestroyAccountLogService;

/**
 * 销毁账户流水Controller
 * @author zzj
 * @version 2018-12-25
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/destroyAccountLog")
public class DestroyAccountLogController extends BaseController {

	@Autowired
	private DestroyAccountLogService destroyAccountLogService;
	
	@ModelAttribute
	public DestroyAccountLog get(@RequestParam(required=false) String id) {
		DestroyAccountLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = destroyAccountLogService.get(id);
		}
		if (entity == null){
			entity = new DestroyAccountLog();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:destroyAccountLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(DestroyAccountLog destroyAccountLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DestroyAccountLog> page = destroyAccountLogService.findPage(new Page<DestroyAccountLog>(request, response), destroyAccountLog); 
		model.addAttribute("page", page);
		return "modules/plat/destroyAccountLogList";
	}

	@RequiresPermissions("plat:destroyAccountLog:view")
	@RequestMapping(value = "form")
	public String form(DestroyAccountLog destroyAccountLog, Model model) {
		model.addAttribute("destroyAccountLog", destroyAccountLog);
		return "modules/plat/destroyAccountLogForm";
	}

	@RequiresPermissions("plat:destroyAccountLog:edit")
	@RequestMapping(value = "save")
	public String save(DestroyAccountLog destroyAccountLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, destroyAccountLog)){
			return form(destroyAccountLog, model);
		}
		destroyAccountLogService.save(destroyAccountLog);
		addMessage(redirectAttributes, "保存销毁账户流水成功");
		return "redirect:"+Global.getAdminPath()+"/plat/destroyAccountLog/?repage";
	}
	
	@RequiresPermissions("plat:destroyAccountLog:edit")
	@RequestMapping(value = "delete")
	public String delete(DestroyAccountLog destroyAccountLog, RedirectAttributes redirectAttributes) {
		destroyAccountLogService.delete(destroyAccountLog);
		addMessage(redirectAttributes, "删除销毁账户流水成功");
		return "redirect:"+Global.getAdminPath()+"/plat/destroyAccountLog/?repage";
	}

}