/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.report.entity.ReportTradePriceRange;
import com.thinkgem.jeesite.modules.report.service.ReportTradePriceRangeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 币币交易价格区间统计Controller
 * @author zzj
 * @version 2018-12-14
 */
@Controller
@RequestMapping(value = "${adminPath}/report/reportTradePriceRange")
public class ReportTradePriceRangeController extends BaseController {

	@Autowired
	private ReportTradePriceRangeService reportTradePriceRangeService;
	
	@ModelAttribute
	public ReportTradePriceRange get(@RequestParam(required=false) String id) {
		ReportTradePriceRange entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = reportTradePriceRangeService.get(id);
		}
		if (entity == null){
			entity = new ReportTradePriceRange();
		}
		return entity;
	}
	
	@RequiresPermissions("report:reportTradePriceRange:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportTradePriceRange reportTradePriceRange, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportTradePriceRange> page = new Page<ReportTradePriceRange>(request, response);
		page = reportTradePriceRangeService.findPage(page, reportTradePriceRange);
		model.addAttribute("page", page);
		return "modules/report/reportTradePriceRangeList";
	}

	@RequiresPermissions("report:reportTradePriceRange:view")
	@RequestMapping(value = "form")
	public String form(ReportTradePriceRange reportTradePriceRange, Model model) {
		model.addAttribute("reportTradePriceRange", reportTradePriceRange);
		return "modules/report/reportTradePriceRangeForm";
	}


}