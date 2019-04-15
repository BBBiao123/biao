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
import com.thinkgem.jeesite.modules.plat.entity.EthTokenWithdraw;
import com.thinkgem.jeesite.modules.plat.service.EthTokenWithdrawService;

/**
 * eth token withdrawController
 * @author ruoyu
 * @version 2018-07-03
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/ethTokenWithdraw")
public class EthTokenWithdrawController extends BaseController {

	@Autowired
	private EthTokenWithdrawService ethTokenWithdrawService;
	
	@ModelAttribute
	public EthTokenWithdraw get(@RequestParam(required=false) String id) {
		EthTokenWithdraw entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ethTokenWithdrawService.get(id);
		}
		if (entity == null){
			entity = new EthTokenWithdraw();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:ethTokenWithdraw:view")
	@RequestMapping(value = {"list", ""})
	public String list(EthTokenWithdraw ethTokenWithdraw, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EthTokenWithdraw> page = ethTokenWithdrawService.findPage(new Page<EthTokenWithdraw>(request, response), ethTokenWithdraw); 
		model.addAttribute("page", page);
		return "modules/plat/ethTokenWithdrawList";
	}

	@RequiresPermissions("plat:ethTokenWithdraw:view")
	@RequestMapping(value = "form")
	public String form(EthTokenWithdraw ethTokenWithdraw, Model model) {
		model.addAttribute("ethTokenWithdraw", ethTokenWithdraw);
		return "modules/plat/ethTokenWithdrawForm";
	}

	@RequiresPermissions("plat:ethTokenWithdraw:edit")
	@RequestMapping(value = "save")
	public String save(EthTokenWithdraw ethTokenWithdraw, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ethTokenWithdraw)){
			return form(ethTokenWithdraw, model);
		}
		ethTokenWithdrawService.save(ethTokenWithdraw);
		addMessage(redirectAttributes, "保存eth token withdraw成功");
		return "redirect:"+Global.getAdminPath()+"/plat/ethTokenWithdraw/?repage";
	}
	
	@RequiresPermissions("plat:ethTokenWithdraw:edit")
	@RequestMapping(value = "delete")
	public String delete(EthTokenWithdraw ethTokenWithdraw, RedirectAttributes redirectAttributes) {
		ethTokenWithdrawService.delete(ethTokenWithdraw);
		addMessage(redirectAttributes, "删除eth token withdraw成功");
		return "redirect:"+Global.getAdminPath()+"/plat/ethTokenWithdraw/?repage";
	}

}