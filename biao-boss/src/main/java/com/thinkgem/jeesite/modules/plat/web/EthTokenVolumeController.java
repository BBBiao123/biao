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
import com.thinkgem.jeesite.modules.plat.entity.EthTokenVolume;
import com.thinkgem.jeesite.modules.plat.service.EthTokenVolumeService;

/**
 * eth_token的volumeController
 * @author ruoyu
 * @version 2018-07-03
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/ethTokenVolume")
public class EthTokenVolumeController extends BaseController {

	@Autowired
	private EthTokenVolumeService ethTokenVolumeService;
	
	@ModelAttribute
	public EthTokenVolume get(@RequestParam(required=false) String id) {
		EthTokenVolume entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ethTokenVolumeService.get(id);
		}
		if (entity == null){
			entity = new EthTokenVolume();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:ethTokenVolume:view")
	@RequestMapping(value = {"list", ""})
	public String list(EthTokenVolume ethTokenVolume, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EthTokenVolume> page = ethTokenVolumeService.findPage(new Page<EthTokenVolume>(request, response), ethTokenVolume); 
		model.addAttribute("page", page);
		return "modules/plat/ethTokenVolumeList";
	}

	@RequiresPermissions("plat:ethTokenVolume:view")
	@RequestMapping(value = "form")
	public String form(EthTokenVolume ethTokenVolume, Model model) {
		model.addAttribute("ethTokenVolume", ethTokenVolume);
		return "modules/plat/ethTokenVolumeForm";
	}

	@RequiresPermissions("plat:ethTokenVolume:edit")
	@RequestMapping(value = "save")
	public String save(EthTokenVolume ethTokenVolume, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ethTokenVolume)){
			return form(ethTokenVolume, model);
		}
		ethTokenVolumeService.save(ethTokenVolume);
		addMessage(redirectAttributes, "保存dd成功");
		return "redirect:"+Global.getAdminPath()+"/plat/ethTokenVolume/?repage";
	}
	
	@RequiresPermissions("plat:ethTokenVolume:edit")
	@RequestMapping(value = "delete")
	public String delete(EthTokenVolume ethTokenVolume, RedirectAttributes redirectAttributes) {
		ethTokenVolumeService.delete(ethTokenVolume);
		addMessage(redirectAttributes, "删除dd成功");
		return "redirect:"+Global.getAdminPath()+"/plat/ethTokenVolume/?repage";
	}

}