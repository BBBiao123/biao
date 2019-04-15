/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.entity.CoinVolumeStat;
import com.thinkgem.jeesite.modules.plat.entity.UserCoinVolume;
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
import java.util.List;

/**
 * 币币资产Controller
 * @author dazi
 * @version 2018-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/coinVolumeStat")
public class CoinVolumeStatController extends BaseController {

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
	public String list(CoinVolumeStat coinVolumeStat, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<CoinVolumeStat> coinVolumeStatList = userCoinVolumeService.findCoinVolumeStat();
		model.addAttribute("statList", coinVolumeStatList);
		return "modules/plat/coinVolumeStatList";
	}
}