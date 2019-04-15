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
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMiningHoldCoinQuery;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeMiningHoldCoinQueryService;

/**
 * 挖矿持币量查询Controller
 * @author dongfeng
 * @version 2018-09-06
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeMiningHoldCoinQuery")
public class Mk2PopularizeMiningHoldCoinQueryController extends BaseController {

	@Autowired
	private Mk2PopularizeMiningHoldCoinQueryService mk2PopularizeMiningHoldCoinQueryService;
	
	@RequiresPermissions("market:mk2PopularizeMiningHoldCoinQuery:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeMiningHoldCoinQuery mk2PopularizeMiningHoldCoinQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("list", mk2PopularizeMiningHoldCoinQueryService.findListByUserId(mk2PopularizeMiningHoldCoinQuery));
		return "modules/market/mk2PopularizeMiningHoldCoinQueryList";
	}

}