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
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeBonusAccountLog;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeBonusAccountLogService;

/**
 * 平台运营分红日志Controller
 * @author dongfeng
 * @version 2018-08-06
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeBonusAccountLog")
public class Mk2PopularizeBonusAccountLogController extends BaseController {

	@Autowired
	private Mk2PopularizeBonusAccountLogService mk2PopularizeBonusAccountLogService;
	
	@ModelAttribute
	public Mk2PopularizeBonusAccountLog get(@RequestParam(required=false) String id) {
		Mk2PopularizeBonusAccountLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeBonusAccountLogService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeBonusAccountLog();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mk2PopularizeBonusAccountLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeBonusAccountLog mk2PopularizeBonusAccountLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeBonusAccountLog> page = mk2PopularizeBonusAccountLogService.findPage(new Page<Mk2PopularizeBonusAccountLog>(request, response), mk2PopularizeBonusAccountLog); 
		model.addAttribute("page", page);
		return "modules/market/mk2PopularizeBonusAccountLogList";
	}

	@RequiresPermissions("market:mk2PopularizeBonusAccountLog:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeBonusAccountLog mk2PopularizeBonusAccountLog, Model model) {
		model.addAttribute("mk2PopularizeBonusAccountLog", mk2PopularizeBonusAccountLog);
		return "modules/market/mk2PopularizeBonusAccountLogForm";
	}

	@RequiresPermissions("market:mk2PopularizeBonusAccountLog:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeBonusAccountLog mk2PopularizeBonusAccountLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mk2PopularizeBonusAccountLog)){
			return form(mk2PopularizeBonusAccountLog, model);
		}
		mk2PopularizeBonusAccountLogService.save(mk2PopularizeBonusAccountLog);
		addMessage(redirectAttributes, "保存平台运营分红日志成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeBonusAccountLog/?repage";
	}
	
	@RequiresPermissions("market:mk2PopularizeBonusAccountLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeBonusAccountLog mk2PopularizeBonusAccountLog, RedirectAttributes redirectAttributes) {
		mk2PopularizeBonusAccountLogService.delete(mk2PopularizeBonusAccountLog);
		addMessage(redirectAttributes, "删除平台运营分红日志成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeBonusAccountLog/?repage";
	}

}