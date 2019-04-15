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
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMiningTaskLog;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeMiningTaskLogService;

/**
 * 挖矿任务日志Controller
 * @author dongfeng
 * @version 2018-08-07
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeMiningTaskLog")
public class Mk2PopularizeMiningTaskLogController extends BaseController {

	@Autowired
	private Mk2PopularizeMiningTaskLogService mk2PopularizeMiningTaskLogService;
	
	@ModelAttribute
	public Mk2PopularizeMiningTaskLog get(@RequestParam(required=false) String id) {
		Mk2PopularizeMiningTaskLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeMiningTaskLogService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeMiningTaskLog();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mk2PopularizeMiningTaskLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeMiningTaskLog mk2PopularizeMiningTaskLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeMiningTaskLog> page = mk2PopularizeMiningTaskLogService.findPage(new Page<Mk2PopularizeMiningTaskLog>(request, response), mk2PopularizeMiningTaskLog); 
		model.addAttribute("page", page);
		return "modules/market/mk2PopularizeMiningTaskLogList";
	}

	@RequiresPermissions("market:mk2PopularizeMiningTaskLog:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeMiningTaskLog mk2PopularizeMiningTaskLog, Model model) {
		model.addAttribute("mk2PopularizeMiningTaskLog", mk2PopularizeMiningTaskLog);
		return "modules/market/mk2PopularizeMiningTaskLogForm";
	}

	@RequiresPermissions("market:mk2PopularizeMiningTaskLog:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeMiningTaskLog mk2PopularizeMiningTaskLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mk2PopularizeMiningTaskLog)){
			return form(mk2PopularizeMiningTaskLog, model);
		}
		mk2PopularizeMiningTaskLogService.save(mk2PopularizeMiningTaskLog);
		addMessage(redirectAttributes, "保存挖矿任务日志成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeMiningTaskLog/?repage";
	}
	
	@RequiresPermissions("market:mk2PopularizeMiningTaskLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeMiningTaskLog mk2PopularizeMiningTaskLog, RedirectAttributes redirectAttributes) {
		mk2PopularizeMiningTaskLogService.delete(mk2PopularizeMiningTaskLog);
		addMessage(redirectAttributes, "删除挖矿任务日志成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeMiningTaskLog/?repage";
	}

}