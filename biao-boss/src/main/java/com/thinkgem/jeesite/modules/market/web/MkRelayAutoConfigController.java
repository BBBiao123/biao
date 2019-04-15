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
import com.thinkgem.jeesite.modules.market.entity.MkRelayAutoConfig;
import com.thinkgem.jeesite.modules.market.service.MkRelayAutoConfigService;

/**
 * 接力自动撞奖配置Controller
 * @author zzj
 * @version 2018-09-26
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkRelayAutoConfig")
public class MkRelayAutoConfigController extends BaseController {

	@Autowired
	private MkRelayAutoConfigService mkRelayAutoConfigService;
	
	@ModelAttribute
	public MkRelayAutoConfig get(@RequestParam(required=false) String id) {
		MkRelayAutoConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkRelayAutoConfigService.get(id);
		}
		if (entity == null){
			entity = new MkRelayAutoConfig();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkRelayAutoConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkRelayAutoConfig mkRelayAutoConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkRelayAutoConfig> page = mkRelayAutoConfigService.findPage(new Page<MkRelayAutoConfig>(request, response), mkRelayAutoConfig); 
		model.addAttribute("page", page);
		return "modules/market/mkRelayAutoConfigList";
	}

	@RequiresPermissions("market:mkRelayAutoConfig:view")
	@RequestMapping(value = "form")
	public String form(MkRelayAutoConfig mkRelayAutoConfig, Model model) {
		model.addAttribute("mkRelayAutoConfig", mkRelayAutoConfig);
		return "modules/market/mkRelayAutoConfigForm";
	}

	@RequiresPermissions("market:mkRelayAutoConfig:edit")
	@RequestMapping(value = "save")
	public String save(MkRelayAutoConfig mkRelayAutoConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkRelayAutoConfig)){
			return form(mkRelayAutoConfig, model);
		}
		mkRelayAutoConfigService.save(mkRelayAutoConfig);
		addMessage(redirectAttributes, "保存接力自动撞奖配置成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayAutoConfig/?repage";
	}
	
	@RequiresPermissions("market:mkRelayAutoConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(MkRelayAutoConfig mkRelayAutoConfig, RedirectAttributes redirectAttributes) {
		mkRelayAutoConfigService.delete(mkRelayAutoConfig);
		addMessage(redirectAttributes, "删除接力自动撞奖配置成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayAutoConfig/?repage";
	}

}