/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

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
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeBonusTaskLog;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeBonusTaskLogService;

/**
 * 分红任务运行记录Controller
 * @author dongfeng
 * @version 2018-08-06
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeBonusTaskLog")
public class Mk2PopularizeBonusTaskLogController extends BaseController {

	@Autowired
	private Mk2PopularizeBonusTaskLogService mk2PopularizeBonusTaskLogService;
	
	@ModelAttribute
	public Mk2PopularizeBonusTaskLog get(@RequestParam(required=false) String id) {
		Mk2PopularizeBonusTaskLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeBonusTaskLogService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeBonusTaskLog();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mk2PopularizeBonusTaskLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeBonusTaskLog mk2PopularizeBonusTaskLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeBonusTaskLog> page = mk2PopularizeBonusTaskLogService.findPage(new Page<Mk2PopularizeBonusTaskLog>(request, response), mk2PopularizeBonusTaskLog); 
		model.addAttribute("page", page);
		return "modules/market/mk2PopularizeBonusTaskLogList";
	}

	@RequiresPermissions("market:mk2PopularizeBonusTaskLog:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeBonusTaskLog mk2PopularizeBonusTaskLog, Model model) {
		model.addAttribute("mk2PopularizeBonusTaskLog", mk2PopularizeBonusTaskLog);
		return "modules/market/mk2PopularizeBonusTaskLogForm";
	}

	@RequiresPermissions("market:mk2PopularizeBonusTaskLog:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeBonusTaskLog mk2PopularizeBonusTaskLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mk2PopularizeBonusTaskLog)){
			return form(mk2PopularizeBonusTaskLog, model);
		}
		mk2PopularizeBonusTaskLogService.save(mk2PopularizeBonusTaskLog);
		addMessage(redirectAttributes, "保存分红任务运行记录成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeBonusTaskLog/?repage";
	}
	
	@RequiresPermissions("market:mk2PopularizeBonusTaskLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeBonusTaskLog mk2PopularizeBonusTaskLog, RedirectAttributes redirectAttributes) {
		mk2PopularizeBonusTaskLogService.delete(mk2PopularizeBonusTaskLog);
		addMessage(redirectAttributes, "删除分红任务运行记录成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeBonusTaskLog/?repage";
	}

}