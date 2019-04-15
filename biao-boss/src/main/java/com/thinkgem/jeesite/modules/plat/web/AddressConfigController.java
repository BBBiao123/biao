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
import com.thinkgem.jeesite.modules.plat.entity.AddressConfig;
import com.thinkgem.jeesite.modules.plat.service.AddressConfigService;

/**
 * eth_token的volumeController
 * @author ruoyu
 * @version 2018-07-03
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/addressConfig")
public class AddressConfigController extends BaseController {

	@Autowired
	private AddressConfigService addressConfigService;
	
	@ModelAttribute
	public AddressConfig get(@RequestParam(required=false) String id) {
		AddressConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = addressConfigService.get(id);
		}
		if (entity == null){
			entity = new AddressConfig();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:addressConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(AddressConfig addressConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AddressConfig> page = addressConfigService.findPage(new Page<AddressConfig>(request, response), addressConfig); 
		model.addAttribute("page", page);
		return "modules/plat/addressConfigList";
	}

	@RequiresPermissions("plat:addressConfig:view")
	@RequestMapping(value = "form")
	public String form(AddressConfig addressConfig, Model model) {
		model.addAttribute("addressConfig", addressConfig);
		return "modules/plat/addressConfigForm";
	}

	@RequiresPermissions("plat:addressConfig:edit")
	@RequestMapping(value = "save")
	public String save(AddressConfig addressConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, addressConfig)){
			return form(addressConfig, model);
		}
		addressConfigService.save(addressConfig);
		addMessage(redirectAttributes, "保存dd成功");
		return "redirect:"+Global.getAdminPath()+"/plat/addressConfig/?repage";
	}
	
	@RequiresPermissions("plat:addressConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(AddressConfig addressConfig, RedirectAttributes redirectAttributes) {
		addressConfigService.delete(addressConfig);
		addMessage(redirectAttributes, "删除dd成功");
		return "redirect:"+Global.getAdminPath()+"/plat/addressConfig/?repage";
	}

}