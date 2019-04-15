/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.report.entity.ReportOfflineFee;
import com.thinkgem.jeesite.modules.report.service.ReportOfflineFeeService;
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
 * 主区各币种交易量Controller
 * @author dazi
 * @version 2018-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/report/reportOfflineFee")
public class ReportOfflineFeeController extends BaseController {

	@Autowired
	private ReportOfflineFeeService reportOfflineFeeService;
	
	@ModelAttribute
	public ReportOfflineFee get(@RequestParam(required=false) String id) {
		ReportOfflineFee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = reportOfflineFeeService.get(id);
		}
		if (entity == null){
			entity = new ReportOfflineFee();
		}
		return entity;
	}
	
	@RequiresPermissions("report:reportOfflineFee:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportOfflineFee reportOfflineFee, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportOfflineFee> page = new Page<ReportOfflineFee>(request, response);
		if(ObjectUtils.isEmpty(reportOfflineFee.getBeginCreateDate()) && ObjectUtils.isEmpty(reportOfflineFee.getEndCreateDate())){
			page.setList(null);
		}else{
			page = reportOfflineFeeService.findPage(page, reportOfflineFee);
		}
		model.addAttribute("page", page);
		return "modules/report/reportOfflineFeeList";
	}

	@RequiresPermissions("report:reportOfflineFee:view")
	@RequestMapping(value = "form")
	public String form(ReportOfflineFee reportOfflineFee, Model model) {
		model.addAttribute("reportOfflineFee", reportOfflineFee);
		return "modules/report/reportOfflineFeeForm";
	}


}