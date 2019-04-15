/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.report.entity.ReportUserOfflineReconciliation;
import com.thinkgem.jeesite.modules.report.service.ReportUserOfflineReconciliationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 主区各币种交易量Controller
 * @author zzj
 * @version 2018-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/report/reportUserOfflineReconciliation")
public class ReportUserOfflineReconciliationController extends BaseController {

	@Autowired
	private ReportUserOfflineReconciliationService reportUserOfflineReconciliationService;
	
	@ModelAttribute
	public ReportUserOfflineReconciliation get(@RequestParam(required=false) String id) {
		ReportUserOfflineReconciliation entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = reportUserOfflineReconciliationService.get(id);
		}
		if (entity == null){
			entity = new ReportUserOfflineReconciliation();
		}
		return entity;
	}

	@RequiresPermissions("report:reportUserOfflineReconciliation:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportUserOfflineReconciliation reportUserOfflineReconciliation, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<ReportUserOfflineReconciliation> page = new Page<ReportUserOfflineReconciliation>(request, response);
		page.setPageSize(1000);
		if(StringUtils.isNotEmpty(reportUserOfflineReconciliation.getUserMobile())){
			page = reportUserOfflineReconciliationService.findPage(page, reportUserOfflineReconciliation);
		}else{
			page.setList(null);
		}
		model.addAttribute("page", page);
		return "modules/report/reportUserOfflineReconciliationList";
	}

	@RequiresPermissions("report:reportUserOfflineReconciliation:view")
	@RequestMapping(value = "form")
	public String form(ReportUserOfflineReconciliation reportUserOfflineReconciliation, Model model) {
		model.addAttribute("reportUserOfflineReconciliation", reportUserOfflineReconciliation);
		return "modules/report/reportUserOfflineReconciliationForm";
	}


}