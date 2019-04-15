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
import com.thinkgem.jeesite.modules.plat.entity.UserCoinVolume;
import com.thinkgem.jeesite.modules.plat.service.UserCoinVolumeService;

/**
 * 币币资产Controller
 * @author dazi
 * @version 2018-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/userCoinVolume")
public class UserCoinVolumeController extends BaseController {

	@Autowired
	private UserCoinVolumeService userCoinVolumeService;
	
	@ModelAttribute
	public UserCoinVolume get(@RequestParam(required=false) String id) {
		UserCoinVolume entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userCoinVolumeService.get(id);
		}
		if (entity == null){
			entity = new UserCoinVolume();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:userCoinVolume:view")
	@RequestMapping(value = {"list", ""})
	public String list(UserCoinVolume userCoinVolume, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<UserCoinVolume> page = null;
		if(StringUtils.isNotEmpty(userCoinVolume.getUserId()) || StringUtils.isNotEmpty(userCoinVolume.getCoinSymbol()) || StringUtils.isNotEmpty(userCoinVolume.getMail()) || StringUtils.isNotEmpty(userCoinVolume.getMobile())){
			page = userCoinVolumeService.findPage(new Page<UserCoinVolume>(request, response), userCoinVolume);
		}

		model.addAttribute("page", page);
		return "modules/plat/userCoinVolumeList";
	}

	@RequiresPermissions("plat:userCoinVolume:view")
	@RequestMapping(value = "form")
	public String form(UserCoinVolume userCoinVolume, Model model) {
		model.addAttribute("userCoinVolume", userCoinVolume);
		return "modules/plat/userCoinVolumeForm";
	}

	@RequiresPermissions("plat:userCoinVolume:edit")
	@RequestMapping(value = "save")
	public String save(UserCoinVolume userCoinVolume, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, userCoinVolume)){
			return form(userCoinVolume, model);
		}
		userCoinVolumeService.save(userCoinVolume);
		addMessage(redirectAttributes, "保存币币资产成功");
		return "redirect:"+Global.getAdminPath()+"/plat/userCoinVolume/?repage";
	}
	
	@RequiresPermissions("plat:userCoinVolume:edit")
	@RequestMapping(value = "delete")
	public String delete(UserCoinVolume userCoinVolume, RedirectAttributes redirectAttributes) {
		userCoinVolumeService.delete(userCoinVolume);
		addMessage(redirectAttributes, "删除币币资产成功");
		return "redirect:"+Global.getAdminPath()+"/plat/userCoinVolume/?repage";
	}

}