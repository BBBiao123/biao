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
import com.thinkgem.jeesite.modules.report.entity.ReportTradeFeeCoin;
import com.thinkgem.jeesite.modules.report.service.ReportTradeFeeCoinService;

/**
 * 日手续费按币种统计Controller
 * @author ruoyu
 * @version 2018-06-26
 */
@Controller
@RequestMapping(value = "${adminPath}/report/reportTradeFeeCoin")
public class ReportTradeFeeCoinController extends BaseController {

	@Autowired
	private ReportTradeFeeCoinService reportTradeFeeCoinService;
	
	@ModelAttribute
	public ReportTradeFeeCoin get(@RequestParam(required=false) String id) {
		ReportTradeFeeCoin entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = reportTradeFeeCoinService.get(id);
		}
		if (entity == null){
			entity = new ReportTradeFeeCoin();
		}
		return entity;
	}
	
	@RequiresPermissions("report:reportTradeFeeCoin:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportTradeFeeCoin reportTradeFeeCoin, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportTradeFeeCoin> page = reportTradeFeeCoinService.findPage(new Page<ReportTradeFeeCoin>(request, response), reportTradeFeeCoin); 
		model.addAttribute("page", page);
		return "modules/report/reportTradeFeeCoinList";
	}
	
	@RequiresPermissions("report:reportTradeFeeCoin:count")
	@RequestMapping(value = "count")
	public String count(ReportTradeFeeCoin reportTradeFeeCoin, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ReportTradeFeeCoin> pageList = reportTradeFeeCoinService.findPageCount(new Page<ReportTradeFeeCoin>(request, response), reportTradeFeeCoin); 
		model.addAttribute("pageList", pageList);
		return "modules/report/reportTradeFeeCoinCount";
	}

	@RequiresPermissions("report:reportTradeFeeCoin:view")
	@RequestMapping(value = "form")
	public String form(ReportTradeFeeCoin reportTradeFeeCoin, Model model) {
		model.addAttribute("reportTradeFeeCoin", reportTradeFeeCoin);
		return "modules/report/reportTradeFeeCoinForm";
	}

	@RequiresPermissions("report:reportTradeFeeCoin:edit")
	@RequestMapping(value = "save")
	public String save(ReportTradeFeeCoin reportTradeFeeCoin, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportTradeFeeCoin)){
			return form(reportTradeFeeCoin, model);
		}
		reportTradeFeeCoinService.save(reportTradeFeeCoin);
		addMessage(redirectAttributes, "保存日手续费按币种统计成功");
		return "redirect:"+Global.getAdminPath()+"/report/reportTradeFeeCoin/?repage";
	}
	
	@RequiresPermissions("report:reportTradeFeeCoin:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportTradeFeeCoin reportTradeFeeCoin, RedirectAttributes redirectAttributes) {
		reportTradeFeeCoinService.delete(reportTradeFeeCoin);
		addMessage(redirectAttributes, "删除日手续费按币种统计成功");
		return "redirect:"+Global.getAdminPath()+"/report/reportTradeFeeCoin/?repage";
	}

}