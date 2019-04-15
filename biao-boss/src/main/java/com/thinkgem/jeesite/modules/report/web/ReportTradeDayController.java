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
import com.thinkgem.jeesite.modules.report.entity.ReportTradeDay;
import com.thinkgem.jeesite.modules.report.service.ReportTradeDayService;

/**
 * 主区各币种交易量Controller
 * @author dazi
 * @version 2018-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/report/reportTradeDay")
public class ReportTradeDayController extends BaseController {

	@Autowired
	private ReportTradeDayService reportTradeDayService;
	
	@ModelAttribute
	public ReportTradeDay get(@RequestParam(required=false) String id) {
		ReportTradeDay entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = reportTradeDayService.get(id);
		}
		if (entity == null){
			entity = new ReportTradeDay();
		}
		return entity;
	}
	
	@RequiresPermissions("report:reportTradeDay:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportTradeDay reportTradeDay, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportTradeDay> page = reportTradeDayService.findPage(new Page<ReportTradeDay>(request, response), reportTradeDay); 
		model.addAttribute("page", page);
		return "modules/report/reportTradeDayList";
	}

	@RequiresPermissions("report:reportTradeDay:view")
	@RequestMapping(value = "form")
	public String form(ReportTradeDay reportTradeDay, Model model) {
		model.addAttribute("reportTradeDay", reportTradeDay);
		return "modules/report/reportTradeDayForm";
	}

	@RequiresPermissions("report:reportTradeDay:edit")
	@RequestMapping(value = "save")
	public String save(ReportTradeDay reportTradeDay, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportTradeDay)){
			return form(reportTradeDay, model);
		}
		reportTradeDayService.save(reportTradeDay);
		addMessage(redirectAttributes, "保存主区交易量成功");
		return "redirect:"+Global.getAdminPath()+"/report/reportTradeDay/?repage";
	}
	
	@RequiresPermissions("report:reportTradeDay:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportTradeDay reportTradeDay, RedirectAttributes redirectAttributes) {
		reportTradeDayService.delete(reportTradeDay);
		addMessage(redirectAttributes, "删除主区交易量成功");
		return "redirect:"+Global.getAdminPath()+"/report/reportTradeDay/?repage";
	}

}