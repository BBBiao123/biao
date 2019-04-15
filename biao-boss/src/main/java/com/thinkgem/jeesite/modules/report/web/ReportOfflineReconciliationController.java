/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.report.entity.ReportOfflineReconciliation;
import com.thinkgem.jeesite.modules.report.service.ReportOfflineReconciliationService;
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

/**
 * 主区各币种交易量Controller
 * @author zzj
 * @version 2018-10-10
 */
@Controller
@RequestMapping(value = "${adminPath}/report/reportOfflineReconciliation")
public class ReportOfflineReconciliationController extends BaseController {

	@Autowired
	private ReportOfflineReconciliationService reportOfflineReconciliationService;
	
	@ModelAttribute
	public ReportOfflineReconciliation get(@RequestParam(required=false) String id) {
		ReportOfflineReconciliation entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = reportOfflineReconciliationService.get(id);
		}
		if (entity == null){
			entity = new ReportOfflineReconciliation();
		}
		return entity;
	}

	@RequiresPermissions("report:reportOfflineReconciliation:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportOfflineReconciliation reportOfflineReconciliation, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<ReportOfflineReconciliation> page = new Page<ReportOfflineReconciliation>(request, response);
		page.setPageSize(1000);
		if(StringUtils.isNotEmpty(reportOfflineReconciliation.getTag())){
			page = reportOfflineReconciliationService.findPage(page, reportOfflineReconciliation);
		}else{
			page.setList(null);
		}
		model.addAttribute("page", page);
		return "modules/report/reportOfflineReconciliationList";
	}

	@RequiresPermissions("report:reportOfflineReconciliation:view")
	@RequestMapping(value = "form")
	public String form(ReportOfflineReconciliation reportOfflineReconciliation, Model model) {
		model.addAttribute("reportOfflineReconciliation", reportOfflineReconciliation);
		return "modules/report/reportOfflineReconciliationForm";
	}


}