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
import com.thinkgem.jeesite.modules.market.entity.MkCommonPlatIncomeTaskLog;
import com.thinkgem.jeesite.modules.market.service.MkCommonPlatIncomeTaskLogService;

/**
 * 平台收入任务日志Controller
 * @author dongfeng
 * @version 2018-08-09
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkCommonPlatIncomeTaskLog")
public class MkCommonPlatIncomeTaskLogController extends BaseController {

	@Autowired
	private MkCommonPlatIncomeTaskLogService mkCommonPlatIncomeTaskLogService;
	
	@ModelAttribute
	public MkCommonPlatIncomeTaskLog get(@RequestParam(required=false) String id) {
		MkCommonPlatIncomeTaskLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkCommonPlatIncomeTaskLogService.get(id);
		}
		if (entity == null){
			entity = new MkCommonPlatIncomeTaskLog();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkCommonPlatIncomeTaskLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkCommonPlatIncomeTaskLog mkCommonPlatIncomeTaskLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkCommonPlatIncomeTaskLog> page = mkCommonPlatIncomeTaskLogService.findPage(new Page<MkCommonPlatIncomeTaskLog>(request, response), mkCommonPlatIncomeTaskLog); 
		model.addAttribute("page", page);
		return "modules/market/mkCommonPlatIncomeTaskLogList";
	}

	@RequiresPermissions("market:mkCommonPlatIncomeTaskLog:view")
	@RequestMapping(value = "form")
	public String form(MkCommonPlatIncomeTaskLog mkCommonPlatIncomeTaskLog, Model model) {
		model.addAttribute("mkCommonPlatIncomeTaskLog", mkCommonPlatIncomeTaskLog);
		return "modules/market/mkCommonPlatIncomeTaskLogForm";
	}

	@RequiresPermissions("market:mkCommonPlatIncomeTaskLog:edit")
	@RequestMapping(value = "save")
	public String save(MkCommonPlatIncomeTaskLog mkCommonPlatIncomeTaskLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkCommonPlatIncomeTaskLog)){
			return form(mkCommonPlatIncomeTaskLog, model);
		}
		mkCommonPlatIncomeTaskLogService.save(mkCommonPlatIncomeTaskLog);
		addMessage(redirectAttributes, "保存平台收入任务日志成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkCommonPlatIncomeTaskLog/?repage";
	}
	
	@RequiresPermissions("market:mkCommonPlatIncomeTaskLog:edit")
	@RequestMapping(value = "delete")
	public String delete(MkCommonPlatIncomeTaskLog mkCommonPlatIncomeTaskLog, RedirectAttributes redirectAttributes) {
		mkCommonPlatIncomeTaskLogService.delete(mkCommonPlatIncomeTaskLog);
		addMessage(redirectAttributes, "删除平台收入任务日志成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkCommonPlatIncomeTaskLog/?repage";
	}

}