/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import java.util.List;

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
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.KlinePullConfig;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.KlinePullConfigService;

/**
 * 币安k线配置Controller
 * @author xiaoyu
 * @version 2018-12-25
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/klinePullConfig")
public class KlinePullConfigController extends BaseController {

	@Autowired
	private KlinePullConfigService klinePullConfigService;
	
	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public KlinePullConfig get(@RequestParam(required=false) String id) {
		KlinePullConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = klinePullConfigService.get(id);
		}
		if (entity == null){
			entity = new KlinePullConfig();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:klinePullConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(KlinePullConfig klinePullConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<KlinePullConfig> page = klinePullConfigService.findPage(new Page<KlinePullConfig>(request, response), klinePullConfig); 
		model.addAttribute("page", page);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/klinePullConfigList";
	}

	@RequiresPermissions("plat:klinePullConfig:view")
	@RequestMapping(value = "form")
	public String form(KlinePullConfig klinePullConfig, Model model) {
		model.addAttribute("klinePullConfig", klinePullConfig);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/klinePullConfigForm";
	}

	@RequiresPermissions("plat:klinePullConfig:edit")
	@RequestMapping(value = "save")
	public String save(KlinePullConfig klinePullConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, klinePullConfig)){
			return form(klinePullConfig, model);
		}
		klinePullConfigService.save(klinePullConfig);
		addMessage(redirectAttributes, "保存币安k线配置成功");
		return "redirect:"+Global.getAdminPath()+"/plat/klinePullConfig/?repage";
	}
	
	@RequiresPermissions("plat:klinePullConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(KlinePullConfig klinePullConfig, RedirectAttributes redirectAttributes) {
		klinePullConfigService.delete(klinePullConfig);
		addMessage(redirectAttributes, "删除币安k线配置成功");
		return "redirect:"+Global.getAdminPath()+"/plat/klinePullConfig/?repage";
	}

}