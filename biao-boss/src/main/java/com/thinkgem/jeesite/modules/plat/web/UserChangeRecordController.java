/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.entity.UserChangeRecordVolume;
import com.thinkgem.jeesite.modules.plat.entity.UserCoinVolume;
import com.thinkgem.jeesite.modules.plat.service.UserChangeRecordService;
import com.thinkgem.jeesite.modules.plat.service.UserCoinVolumeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 币币资产Controller
 * @author dazi
 * @version 2018-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/userChangeRecord")
public class UserChangeRecordController extends BaseController {

	@Autowired
	private UserChangeRecordService userCoinVolumeService;
	
	@ModelAttribute
	public UserChangeRecordVolume get(@RequestParam(required=false) String id) {
		UserChangeRecordVolume entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userCoinVolumeService.get(id);
		}
		if (entity == null){
			entity = new UserChangeRecordVolume();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:userCoinVolume:view")
	@RequestMapping(value = {"list", ""})
	public String list(UserChangeRecordVolume userCoinVolume, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<UserChangeRecordVolume> page = null;
		if(StringUtils.isNotEmpty(userCoinVolume.getUserId()) || StringUtils.isNotEmpty(userCoinVolume.getCoinSymbol()) || StringUtils.isNotEmpty(userCoinVolume.getMail()) || StringUtils.isNotEmpty(userCoinVolume.getMobile())){
			page = userCoinVolumeService.findPage(new Page<UserChangeRecordVolume>(request, response), userCoinVolume);
		}

		model.addAttribute("page", page);
		return "modules/plat/userChangeRecordList";
	}
	@RequestMapping(value = "form")
	public String form(UserChangeRecordVolume userCoinVolume, Model model) {
		model.addAttribute("userChangeRecordVolume", userCoinVolume);
		return "modules/plat/userCoinVolumeForm";
	}

	@RequestMapping(value = {"incomeList"})
	public String incomeList(UserChangeRecordVolume userCoinVolume, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<UserChangeRecordVolume> page = null;
		if(StringUtils.isNotEmpty(userCoinVolume.getUserId()) || StringUtils.isNotEmpty(userCoinVolume.getCoinSymbol()) || StringUtils.isNotEmpty(userCoinVolume.getMail()) || StringUtils.isNotEmpty(userCoinVolume.getMobile())){
			page = userCoinVolumeService.findIncomePage(new Page<UserChangeRecordVolume>(request, response), userCoinVolume);
		}

		model.addAttribute("page", page);
		return "modules/plat/userIncomeRecordList";
	}

	@RequestMapping(value = {"assetList"})
	public String assetList(UserChangeRecordVolume userCoinVolume, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<UserChangeRecordVolume> page = null;
		if(StringUtils.isNotEmpty(userCoinVolume.getUserId()) || StringUtils.isNotEmpty(userCoinVolume.getCoinSymbol()) || StringUtils.isNotEmpty(userCoinVolume.getMail()) || StringUtils.isNotEmpty(userCoinVolume.getMobile())){
			page = userCoinVolumeService.findBalanceList(new Page<UserChangeRecordVolume>(request, response), userCoinVolume);
		}

		model.addAttribute("page", page);
		return "modules/plat/userAssetRecordList";
	}

}