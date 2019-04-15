/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
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
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMiningConf;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeMiningConfService;

import java.util.List;

/**
 * 挖矿规则Controller
 * @author dongfeng
 * @version 2018-08-07
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeMiningConf")
public class Mk2PopularizeMiningConfController extends BaseController {

	@Autowired
	private CoinService coinService;

	@Autowired
	private Mk2PopularizeMiningConfService mk2PopularizeMiningConfService;
	
	@ModelAttribute
	public Mk2PopularizeMiningConf get(@RequestParam(required=false) String id) {
		Mk2PopularizeMiningConf entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeMiningConfService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeMiningConf();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mk2PopularizeMiningConf:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeMiningConf mk2PopularizeMiningConf, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeMiningConf> page = mk2PopularizeMiningConfService.findPage(new Page<Mk2PopularizeMiningConf>(request, response), mk2PopularizeMiningConf); 
		model.addAttribute("page", page);
		return "modules/market/mk2PopularizeMiningConfList";
	}

	@RequiresPermissions("market:mk2PopularizeMiningConf:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeMiningConf mk2PopularizeMiningConf, Model model) {
		model.addAttribute("mk2PopularizeMiningConf", mk2PopularizeMiningConf);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/market/mk2PopularizeMiningConfForm";
	}

	@RequiresPermissions("market:mk2PopularizeMiningConf:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeMiningConf mk2PopularizeMiningConf, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mk2PopularizeMiningConf)){
			return form(mk2PopularizeMiningConf, model);
		}
		mk2PopularizeMiningConfService.save(mk2PopularizeMiningConf);
		addMessage(redirectAttributes, "保存挖矿规则成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeMiningConf/?repage";
	}
	
	@RequiresPermissions("market:mk2PopularizeMiningConf:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeMiningConf mk2PopularizeMiningConf, RedirectAttributes redirectAttributes) {
		mk2PopularizeMiningConfService.delete(mk2PopularizeMiningConf);
		addMessage(redirectAttributes, "删除挖矿规则成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeMiningConf/?repage";
	}

}