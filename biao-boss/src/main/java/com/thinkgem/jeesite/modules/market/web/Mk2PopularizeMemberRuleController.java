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
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMemberRule;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeMemberRuleService;

/**
 * 会员规则设置Controller
 * @author dongfeng
 * @version 2019-02-28
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeMemberRule")
public class Mk2PopularizeMemberRuleController extends BaseController {

	@Autowired
	private Mk2PopularizeMemberRuleService mk2PopularizeMemberRuleService;
	
	@ModelAttribute
	public Mk2PopularizeMemberRule get(@RequestParam(required=false) String id) {
		Mk2PopularizeMemberRule entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeMemberRuleService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeMemberRule();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mk2PopularizeMemberRule:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeMemberRule mk2PopularizeMemberRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeMemberRule> page = mk2PopularizeMemberRuleService.findPage(new Page<Mk2PopularizeMemberRule>(request, response), mk2PopularizeMemberRule); 
		model.addAttribute("page", page);
		return "modules/market/mk2PopularizeMemberRuleList";
	}

	@RequiresPermissions("market:mk2PopularizeMemberRule:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeMemberRule mk2PopularizeMemberRule, Model model) {
		model.addAttribute("mk2PopularizeMemberRule", mk2PopularizeMemberRule);
		return "modules/market/mk2PopularizeMemberRuleForm";
	}

	@RequiresPermissions("market:mk2PopularizeMemberRule:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeMemberRule mk2PopularizeMemberRule, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mk2PopularizeMemberRule)){
			return form(mk2PopularizeMemberRule, model);
		}
		mk2PopularizeMemberRuleService.save(mk2PopularizeMemberRule);
		addMessage(redirectAttributes, "保存会员规则设置成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeMemberRule/?repage";
	}
	
	@RequiresPermissions("market:mk2PopularizeMemberRule:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeMemberRule mk2PopularizeMemberRule, RedirectAttributes redirectAttributes) {
		mk2PopularizeMemberRuleService.delete(mk2PopularizeMemberRule);
		addMessage(redirectAttributes, "删除会员规则设置成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeMemberRule/?repage";
	}

}