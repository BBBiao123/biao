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
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeBonusAccount;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeBonusAccountService;

/**
 * 平台运营分红账户Controller
 * @author dongfeng
 * @version 2018-07-31
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeBonusAccount")
public class Mk2PopularizeBonusAccountController extends BaseController {

	@Autowired
	private Mk2PopularizeBonusAccountService mk2PopularizeBonusAccountService;
	
	@ModelAttribute
	public Mk2PopularizeBonusAccount get(@RequestParam(required=false) String id) {
		Mk2PopularizeBonusAccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeBonusAccountService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeBonusAccount();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mk2PopularizeBonusAccount:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeBonusAccount mk2PopularizeBonusAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeBonusAccount> page = mk2PopularizeBonusAccountService.findPage(new Page<Mk2PopularizeBonusAccount>(request, response), mk2PopularizeBonusAccount); 
		model.addAttribute("page", page);
		return "modules/market/mk2PopularizeBonusAccountList";
	}

	@RequiresPermissions("market:mk2PopularizeBonusAccount:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeBonusAccount mk2PopularizeBonusAccount, Model model) {
		model.addAttribute("mk2PopularizeBonusAccount", mk2PopularizeBonusAccount);
		return "modules/market/mk2PopularizeBonusAccountForm";
	}

	@RequiresPermissions("market:mk2PopularizeBonusAccount:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeBonusAccount mk2PopularizeBonusAccount, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mk2PopularizeBonusAccount)){
			return form(mk2PopularizeBonusAccount, model);
		}
		mk2PopularizeBonusAccountService.save(mk2PopularizeBonusAccount);
		addMessage(redirectAttributes, "保存平台运营分红账户成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeBonusAccount/?repage";
	}
	
	@RequiresPermissions("market:mk2PopularizeBonusAccount:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeBonusAccount mk2PopularizeBonusAccount, RedirectAttributes redirectAttributes) {
		mk2PopularizeBonusAccountService.delete(mk2PopularizeBonusAccount);
		addMessage(redirectAttributes, "删除平台运营分红账户成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeBonusAccount/?repage";
	}

}