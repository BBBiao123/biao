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
import com.thinkgem.jeesite.modules.market.entity.MkCommonPlatIncomeLog;
import com.thinkgem.jeesite.modules.market.service.MkCommonPlatIncomeLogService;

/**
 * 平台收入流水Controller
 * @author dongfeng
 * @version 2018-08-09
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkCommonPlatIncomeLog")
public class MkCommonPlatIncomeLogController extends BaseController {

	@Autowired
	private MkCommonPlatIncomeLogService mkCommonPlatIncomeLogService;
	
	@ModelAttribute
	public MkCommonPlatIncomeLog get(@RequestParam(required=false) String id) {
		MkCommonPlatIncomeLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkCommonPlatIncomeLogService.get(id);
		}
		if (entity == null){
			entity = new MkCommonPlatIncomeLog();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkCommonPlatIncomeLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkCommonPlatIncomeLog mkCommonPlatIncomeLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkCommonPlatIncomeLog> page = mkCommonPlatIncomeLogService.findPage(new Page<MkCommonPlatIncomeLog>(request, response), mkCommonPlatIncomeLog); 
		model.addAttribute("page", page);
		return "modules/market/mkCommonPlatIncomeLogList";
	}

	@RequiresPermissions("market:mkCommonPlatIncomeLog:view")
	@RequestMapping(value = "form")
	public String form(MkCommonPlatIncomeLog mkCommonPlatIncomeLog, Model model) {
		model.addAttribute("mkCommonPlatIncomeLog", mkCommonPlatIncomeLog);
		return "modules/market/mkCommonPlatIncomeLogForm";
	}

	@RequiresPermissions("market:mkCommonPlatIncomeLog:edit")
	@RequestMapping(value = "save")
	public String save(MkCommonPlatIncomeLog mkCommonPlatIncomeLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkCommonPlatIncomeLog)){
			return form(mkCommonPlatIncomeLog, model);
		}
		mkCommonPlatIncomeLogService.save(mkCommonPlatIncomeLog);
		addMessage(redirectAttributes, "保存平台收入流水成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkCommonPlatIncomeLog/?repage";
	}
	
	@RequiresPermissions("market:mkCommonPlatIncomeLog:edit")
	@RequestMapping(value = "delete")
	public String delete(MkCommonPlatIncomeLog mkCommonPlatIncomeLog, RedirectAttributes redirectAttributes) {
		mkCommonPlatIncomeLogService.delete(mkCommonPlatIncomeLog);
		addMessage(redirectAttributes, "删除平台收入流水成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkCommonPlatIncomeLog/?repage";
	}

}