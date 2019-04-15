/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.service.PlatUserService;
import com.thinkgem.jeesite.modules.report.entity.ReportPlatUserFinance;
import com.thinkgem.jeesite.modules.report.entity.ReportPlatUserInvite;
import com.thinkgem.jeesite.modules.report.entity.ReportPlatUserReconciliation;
import com.thinkgem.jeesite.modules.report.service.ReportPlatUserFinanceService;
import com.thinkgem.jeesite.modules.report.service.ReportPlatUserInviteService;
import com.thinkgem.jeesite.modules.report.service.ReportPlatUserReconciliationService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.impl.transformer.IntegerToString;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 主区各币种交易量Controller
 * @author zzj
 * @version 2018-10-10
 */
@Controller
@RequestMapping(value = "${adminPath}/report/financeList")
public class ReportPlatUserFinanceController extends BaseController {

	@Autowired
	private ReportPlatUserFinanceService reportPlatUserFinanceService;
	@Autowired
	private ReportPlatUserInviteService reportPlatUserInviteService;
	@Autowired
	private PlatUserService platUserService;
	
	@ModelAttribute
	public ReportPlatUserFinance get(@RequestParam(required=false) String id) {
		ReportPlatUserFinance entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = reportPlatUserFinanceService.get(id);
		}
		if (entity == null){
			entity = new ReportPlatUserFinance();
		}
		return entity;
	}

	@RequiresPermissions("report:reportPlatUserFinance:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportPlatUserFinance reportPlatUserReconciliation, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportPlatUserFinance> page = new Page<ReportPlatUserFinance>(request, response);
		String userId= UserUtils.getUser().getRelPlatUserId();
		PlatUser platUser=platUserService.get(userId);

		ReportPlatUserInvite reportPlatUserInvite =new ReportPlatUserInvite();
		reportPlatUserInvite.setUserId( platUser.getInviteCode() );
		List<ReportPlatUserInvite> xiaji=reportPlatUserInviteService.findList(reportPlatUserInvite);
		int size = xiaji.size();
		List<ReportPlatUserInvite> allList=new ArrayList<ReportPlatUserInvite>();
		allList.addAll(xiaji);
		treePlatUserList(xiaji,allList);
		List<String> userList=new ArrayList<String>();
		for(ReportPlatUserInvite invite  :allList){
			userList.add(invite.getUserId());
		}


		if(StringUtils.isNotEmpty(reportPlatUserReconciliation.getUserId())
				|| StringUtils.isNotEmpty(reportPlatUserReconciliation.getMobile())
				|| StringUtils.isNotEmpty(reportPlatUserReconciliation.getMail())){
		 reportPlatUserFinanceService.findReportPlatUserFinance(reportPlatUserReconciliation);
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put ("page",page);
		map.put("list",userList);
		if (StringUtils.isNotEmpty(reportPlatUserReconciliation.getUserId())){
			if (userList.contains(reportPlatUserReconciliation.getUserId())){
				userList=new ArrayList<String>();
				userList.add(reportPlatUserReconciliation.getUserId());
				map.put("list",userList);
				page = reportPlatUserFinanceService.findPage(page, map);
			}else page.setList(null);

		}else page = reportPlatUserFinanceService.findPage(page, map);

		model.addAttribute("page", page);
		return "modules/report/reportPlatUserFinanceList";
	}

	@RequiresPermissions("report:reportPlatUserReconciliation:view")
	@RequestMapping(value = "form")
	public String form(ReportPlatUserReconciliation reportPlatUserReconciliation, Model model) {
		model.addAttribute("reportPlatUserReconciliation", reportPlatUserReconciliation);
		return "modules/report/reportPlatUserReconciliationForm";
	}
	public static void treePlatUserList(List<ReportPlatUserInvite> userList, List<ReportPlatUserInvite> allList) {
		for (ReportPlatUserInvite user : userList) {
			//遍历出父id等于参数的id，add进子节点集合
			if (!user.getChildList().isEmpty()) {
				//递归遍历下一级
				allList.addAll(user.getChildList());
				treePlatUserList(user.getChildList(),allList);

			}
		}

	}


}