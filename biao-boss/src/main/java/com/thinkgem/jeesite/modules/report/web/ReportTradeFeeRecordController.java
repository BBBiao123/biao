/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.web;

import java.util.List;

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
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.report.entity.ReportTradeFeeRecord;
import com.thinkgem.jeesite.modules.report.service.ReportTradeFeeRecordService;

/**
 * 日手续费按交易对统计Controller
 * @author ruoyu
 * @version 2018-06-26
 */
@Controller
@RequestMapping(value = "${adminPath}/report/reportTradeFeeRecord")
public class ReportTradeFeeRecordController extends BaseController {

	@Autowired
	private ReportTradeFeeRecordService reportTradeFeeRecordService;
	
	@ModelAttribute
	public ReportTradeFeeRecord get(@RequestParam(required=false) String id) {
		ReportTradeFeeRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = reportTradeFeeRecordService.get(id);
		}
		if (entity == null){
			entity = new ReportTradeFeeRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("report:reportTradeFeeRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportTradeFeeRecord reportTradeFeeRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportTradeFeeRecord> page = reportTradeFeeRecordService.findPage(new Page<ReportTradeFeeRecord>(request, response), reportTradeFeeRecord); 
		model.addAttribute("page", page);
		return "modules/report/reportTradeFeeRecordList";
	}
	
	@RequiresPermissions("report:reportTradeFeeRecord:count")
	@RequestMapping(value = "count")
	public String count(ReportTradeFeeRecord reportTradeFeeRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ReportTradeFeeRecord> pageList = reportTradeFeeRecordService.findPageCount(new Page<ReportTradeFeeRecord>(request, response), reportTradeFeeRecord); 
		model.addAttribute("pageList", pageList);
		return "modules/report/reportTradeFeeRecordCount";
	}

	@RequiresPermissions("report:reportTradeFeeRecord:view")
	@RequestMapping(value = "form")
	public String form(ReportTradeFeeRecord reportTradeFeeRecord, Model model) {
		model.addAttribute("reportTradeFeeRecord", reportTradeFeeRecord);
		return "modules/report/reportTradeFeeRecordForm";
	}

	@RequiresPermissions("report:reportTradeFeeRecord:edit")
	@RequestMapping(value = "save")
	public String save(ReportTradeFeeRecord reportTradeFeeRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportTradeFeeRecord)){
			return form(reportTradeFeeRecord, model);
		}
		reportTradeFeeRecordService.save(reportTradeFeeRecord);
		addMessage(redirectAttributes, "保存dd成功");
		return "redirect:"+Global.getAdminPath()+"/report/reportTradeFeeRecord/?repage";
	}
	
	@RequiresPermissions("report:reportTradeFeeRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportTradeFeeRecord reportTradeFeeRecord, RedirectAttributes redirectAttributes) {
		reportTradeFeeRecordService.delete(reportTradeFeeRecord);
		addMessage(redirectAttributes, "删除dd成功");
		return "redirect:"+Global.getAdminPath()+"/report/reportTradeFeeRecord/?repage";
	}

}