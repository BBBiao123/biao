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
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMiningGiveCoinLog;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeMiningGiveCoinLogService;

/**
 * 挖矿规则送币流水Controller
 * @author dongfeng
 * @version 2018-08-07
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeMiningGiveCoinLog")
public class Mk2PopularizeMiningGiveCoinLogController extends BaseController {

	@Autowired
	private Mk2PopularizeMiningGiveCoinLogService mk2PopularizeMiningGiveCoinLogService;
	
	@ModelAttribute
	public Mk2PopularizeMiningGiveCoinLog get(@RequestParam(required=false) String id) {
		Mk2PopularizeMiningGiveCoinLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeMiningGiveCoinLogService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeMiningGiveCoinLog();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mk2PopularizeMiningGiveCoinLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeMiningGiveCoinLog mk2PopularizeMiningGiveCoinLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeMiningGiveCoinLog> page = mk2PopularizeMiningGiveCoinLogService.findPage(new Page<Mk2PopularizeMiningGiveCoinLog>(request, response), mk2PopularizeMiningGiveCoinLog); 
		model.addAttribute("page", page);
		return "modules/market/mk2PopularizeMiningGiveCoinLogList";
	}

	@RequiresPermissions("market:mk2PopularizeMiningGiveCoinLog:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeMiningGiveCoinLog mk2PopularizeMiningGiveCoinLog, Model model) {
		model.addAttribute("mk2PopularizeMiningGiveCoinLog", mk2PopularizeMiningGiveCoinLog);
		return "modules/market/mk2PopularizeMiningGiveCoinLogForm";
	}

	@RequiresPermissions("market:mk2PopularizeMiningGiveCoinLog:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeMiningGiveCoinLog mk2PopularizeMiningGiveCoinLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mk2PopularizeMiningGiveCoinLog)){
			return form(mk2PopularizeMiningGiveCoinLog, model);
		}
		mk2PopularizeMiningGiveCoinLogService.save(mk2PopularizeMiningGiveCoinLog);
		addMessage(redirectAttributes, "保存挖矿规则送币流水成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeMiningGiveCoinLog/?repage";
	}
	
	@RequiresPermissions("market:mk2PopularizeMiningGiveCoinLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeMiningGiveCoinLog mk2PopularizeMiningGiveCoinLog, RedirectAttributes redirectAttributes) {
		mk2PopularizeMiningGiveCoinLogService.delete(mk2PopularizeMiningGiveCoinLog);
		addMessage(redirectAttributes, "删除挖矿规则送币流水成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeMiningGiveCoinLog/?repage";
	}

}