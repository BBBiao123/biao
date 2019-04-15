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
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeBonusMemberLog;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeBonusMemberLogService;

/**
 * 会员分红日志Controller
 * @author dongfeng
 * @version 2018-08-09
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeBonusMemberLog")
public class Mk2PopularizeBonusMemberLogController extends BaseController {

	@Autowired
	private Mk2PopularizeBonusMemberLogService mk2PopularizeBonusMemberLogService;
	
	@ModelAttribute
	public Mk2PopularizeBonusMemberLog get(@RequestParam(required=false) String id) {
		Mk2PopularizeBonusMemberLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeBonusMemberLogService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeBonusMemberLog();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mk2PopularizeBonusMemberLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeBonusMemberLog mk2PopularizeBonusMemberLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeBonusMemberLog> page = mk2PopularizeBonusMemberLogService.findPage(new Page<Mk2PopularizeBonusMemberLog>(request, response), mk2PopularizeBonusMemberLog); 
		model.addAttribute("page", page);
		return "modules/market/mk2PopularizeBonusMemberLogList";
	}

	@RequiresPermissions("market:mk2PopularizeBonusMemberLog:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeBonusMemberLog mk2PopularizeBonusMemberLog, Model model) {
		model.addAttribute("mk2PopularizeBonusMemberLog", mk2PopularizeBonusMemberLog);
		return "modules/market/mk2PopularizeBonusMemberLogForm";
	}

	@RequiresPermissions("market:mk2PopularizeBonusMemberLog:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeBonusMemberLog mk2PopularizeBonusMemberLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mk2PopularizeBonusMemberLog)){
			return form(mk2PopularizeBonusMemberLog, model);
		}
		mk2PopularizeBonusMemberLogService.save(mk2PopularizeBonusMemberLog);
		addMessage(redirectAttributes, "保存会员分红日志成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeBonusMemberLog/?repage";
	}
	
	@RequiresPermissions("market:mk2PopularizeBonusMemberLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeBonusMemberLog mk2PopularizeBonusMemberLog, RedirectAttributes redirectAttributes) {
		mk2PopularizeBonusMemberLogService.delete(mk2PopularizeBonusMemberLog);
		addMessage(redirectAttributes, "删除会员分红日志成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeBonusMemberLog/?repage";
	}

}