/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.PlatUserCoinCountShow;
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
import com.thinkgem.jeesite.modules.plat.entity.PlatUserCoinVolumeCount;
import com.thinkgem.jeesite.modules.plat.service.PlatUserCoinVolumeCountService;

import java.util.List;

/**
 * 持币数量统计Controller
 * @author dongfeng
 * @version 2018-09-04
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/platUserCoinVolumeCount")
public class PlatUserCoinVolumeCountController extends BaseController {

	@Autowired
	private PlatUserCoinVolumeCountService platUserCoinVolumeCountService;
	
	@ModelAttribute
	public PlatUserCoinVolumeCount get(@RequestParam(required=false) String id) {
		PlatUserCoinVolumeCount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = platUserCoinVolumeCountService.get(id);
		}
		if (entity == null){
			entity = new PlatUserCoinVolumeCount();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:platUserCoinVolumeCount:view")
	@RequestMapping(value = {"list", ""})
	public String list(PlatUserCoinVolumeCount platUserCoinVolumeCount, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<PlatUserCoinVolumeCount> countList = platUserCoinVolumeCountService.findCurrentCount( platUserCoinVolumeCount);
		model.addAttribute("countList", countList);
		return "modules/plat/platUserCoinVolumeCountList";
	}

	@RequiresPermissions("plat:platUserCoinVolumeCount:view")
	@RequestMapping(value = "queryByDate")
	public void queryByDate(PlatUserCoinVolumeCount platUserCoinVolumeCount, HttpServletRequest request, HttpServletResponse response) {
		PlatUserCoinCountShow show = platUserCoinVolumeCountService.queryByDate(platUserCoinVolumeCount);
		renderString(response, show);
	}

}