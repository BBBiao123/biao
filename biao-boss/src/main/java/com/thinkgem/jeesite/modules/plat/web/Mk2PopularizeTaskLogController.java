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
import com.thinkgem.jeesite.modules.plat.entity.Mk2PopularizeTaskLog;
import com.thinkgem.jeesite.modules.plat.service.Mk2PopularizeTaskLogService;

/**
 * mk2营销任务执行结果Controller
 * @author dongfeng
 * @version 2018-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/mk2PopularizeTaskLog")
public class Mk2PopularizeTaskLogController extends BaseController {

	@Autowired
	private Mk2PopularizeTaskLogService mk2PopularizeTaskLogService;
	
	@ModelAttribute
	public Mk2PopularizeTaskLog get(@RequestParam(required=false) String id) {
		Mk2PopularizeTaskLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeTaskLogService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeTaskLog();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:mk2PopularizeTaskLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeTaskLog mk2PopularizeTaskLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeTaskLog> page = mk2PopularizeTaskLogService.findPage(new Page<Mk2PopularizeTaskLog>(request, response), mk2PopularizeTaskLog); 
		model.addAttribute("page", page);
		return "modules/plat/mk2PopularizeTaskLogList";
	}

	@RequiresPermissions("plat:mk2PopularizeTaskLog:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeTaskLog mk2PopularizeTaskLog, Model model) {
		model.addAttribute("mk2PopularizeTaskLog", mk2PopularizeTaskLog);
		return "modules/plat/mk2PopularizeTaskLogForm";
	}

	@RequiresPermissions("plat:mk2PopularizeTaskLog:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeTaskLog mk2PopularizeTaskLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mk2PopularizeTaskLog)){
			return form(mk2PopularizeTaskLog, model);
		}
		mk2PopularizeTaskLogService.save(mk2PopularizeTaskLog);
		addMessage(redirectAttributes, "保存mk2营销任务执行结果成功");
		return "redirect:"+Global.getAdminPath()+"/plat/mk2PopularizeTaskLog/?repage";
	}
	
	@RequiresPermissions("plat:mk2PopularizeTaskLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeTaskLog mk2PopularizeTaskLog, RedirectAttributes redirectAttributes) {
		mk2PopularizeTaskLogService.delete(mk2PopularizeTaskLog);
		addMessage(redirectAttributes, "删除mk2营销任务执行结果成功");
		return "redirect:"+Global.getAdminPath()+"/plat/mk2PopularizeTaskLog/?repage";
	}

}