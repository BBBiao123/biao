/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.report.entity.ReportPlatUserReconciliation;
import com.thinkgem.jeesite.modules.report.service.ReportPlatUserReconciliationService;
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
 * @version 2018-10-10
 */
@Controller
@RequestMapping(value = "${adminPath}/report/reportPlatUserReconciliation")
public class ReportPlatUserReconciliationController extends BaseController {

	@Autowired
	private ReportPlatUserReconciliationService reportPlatUserReconciliationService;
	
	@ModelAttribute
	public ReportPlatUserReconciliation get(@RequestParam(required=false) String id) {
		ReportPlatUserReconciliation entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = reportPlatUserReconciliationService.get(id);
		}
		if (entity == null){
			entity = new ReportPlatUserReconciliation();
		}
		return entity;
	}

	@RequiresPermissions("report:reportPlatUserReconciliation:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportPlatUserReconciliation reportPlatUserReconciliation, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<ReportPlatUserReconciliation> page = new Page<ReportPlatUserReconciliation>(request, response);
		String userId = reportPlatUserReconciliation.getUserId();
		if(StringUtils.isNotEmpty(reportPlatUserReconciliation.getUserId())
				|| StringUtils.isNotEmpty(reportPlatUserReconciliation.getMobile())
				|| StringUtils.isNotEmpty(reportPlatUserReconciliation.getMail())){
			page = reportPlatUserReconciliationService.findPage(page, reportPlatUserReconciliation);
		}else{
			page.setList(null);
		}
		reportPlatUserReconciliation.setUserId(userId);
		model.addAttribute("page", page);
		return "modules/report/reportPlatUserReconciliationList";
	}

	@RequiresPermissions("report:reportPlatUserReconciliation:view")
	@RequestMapping(value = "form")
	public String form(ReportPlatUserReconciliation reportPlatUserReconciliation, Model model) {
		model.addAttribute("reportPlatUserReconciliation", reportPlatUserReconciliation);
		return "modules/report/reportPlatUserReconciliationForm";
	}


}