/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.web;

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
import com.thinkgem.jeesite.modules.report.entity.ReportTradeFee;
import com.thinkgem.jeesite.modules.report.service.ReportTradeFeeService;

/**
 * ddController
 * @author ruoyu
 * @version 2018-06-26
 */
@Controller
@RequestMapping(value = "${adminPath}/report/reportTradeFee")
public class ReportTradeFeeController extends BaseController {

	@Autowired
	private ReportTradeFeeService reportTradeFeeService;
	
	@ModelAttribute
	public ReportTradeFee get(@RequestParam(required=false) String id) {
		ReportTradeFee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = reportTradeFeeService.get(id);
		}
		if (entity == null){
			entity = new ReportTradeFee();
		}
		return entity;
	}
	
	@RequiresPermissions("report:reportTradeFee:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportTradeFee reportTradeFee, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportTradeFee> page = reportTradeFeeService.findPage(new Page<ReportTradeFee>(request, response), reportTradeFee); 
		model.addAttribute("page", page);
		return "modules/report/reportTradeFeeList";
	}

	@RequiresPermissions("report:reportTradeFee:view")
	@RequestMapping(value = "form")
	public String form(ReportTradeFee reportTradeFee, Model model) {
		model.addAttribute("reportTradeFee", reportTradeFee);
		return "modules/report/reportTradeFeeForm";
	}

	@RequiresPermissions("report:reportTradeFee:edit")
	@RequestMapping(value = "save")
	public String save(ReportTradeFee reportTradeFee, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportTradeFee)){
			return form(reportTradeFee, model);
		}
		reportTradeFeeService.save(reportTradeFee);
		addMessage(redirectAttributes, "保存dd成功");
		return "redirect:"+Global.getAdminPath()+"/report/reportTradeFee/?repage";
	}
	
	@RequiresPermissions("report:reportTradeFee:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportTradeFee reportTradeFee, RedirectAttributes redirectAttributes) {
		reportTradeFeeService.delete(reportTradeFee);
		addMessage(redirectAttributes, "删除dd成功");
		return "redirect:"+Global.getAdminPath()+"/report/reportTradeFee/?repage";
	}

}