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
import com.thinkgem.jeesite.modules.plat.entity.Mk2PopularizeRegisterCoin;
import com.thinkgem.jeesite.modules.plat.service.Mk2PopularizeRegisterCoinService;

/**
 * 注册用户送币Controller
 * @author dongfeng
 * @version 2018-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/mk2PopularizeRegisterCoin")
public class Mk2PopularizeRegisterCoinController extends BaseController {

	@Autowired
	private Mk2PopularizeRegisterCoinService mk2PopularizeRegisterCoinService;
	
	@ModelAttribute
	public Mk2PopularizeRegisterCoin get(@RequestParam(required=false) String id) {
		Mk2PopularizeRegisterCoin entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeRegisterCoinService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeRegisterCoin();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:mk2PopularizeRegisterCoin:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeRegisterCoin mk2PopularizeRegisterCoin, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeRegisterCoin> page = mk2PopularizeRegisterCoinService.findPage(new Page<Mk2PopularizeRegisterCoin>(request, response), mk2PopularizeRegisterCoin); 
		model.addAttribute("page", page);
		return "modules/plat/mk2PopularizeRegisterCoinList";
	}

	@RequiresPermissions("plat:mk2PopularizeRegisterCoin:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeRegisterCoin mk2PopularizeRegisterCoin, Model model) {
		model.addAttribute("mk2PopularizeRegisterCoin", mk2PopularizeRegisterCoin);
		return "modules/plat/mk2PopularizeRegisterCoinForm";
	}

	@RequiresPermissions("plat:mk2PopularizeRegisterCoin:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeRegisterCoin mk2PopularizeRegisterCoin, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mk2PopularizeRegisterCoin)){
			return form(mk2PopularizeRegisterCoin, model);
		}
		mk2PopularizeRegisterCoinService.save(mk2PopularizeRegisterCoin);
		addMessage(redirectAttributes, "保存注册用户送币成功");
		return "redirect:"+Global.getAdminPath()+"/plat/mk2PopularizeRegisterCoin/?repage";
	}
	
	@RequiresPermissions("plat:mk2PopularizeRegisterCoin:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeRegisterCoin mk2PopularizeRegisterCoin, RedirectAttributes redirectAttributes) {
		mk2PopularizeRegisterCoinService.delete(mk2PopularizeRegisterCoin);
		addMessage(redirectAttributes, "删除注册用户送币成功");
		return "redirect:"+Global.getAdminPath()+"/plat/mk2PopularizeRegisterCoin/?repage";
	}

}