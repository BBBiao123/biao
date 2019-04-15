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
import com.thinkgem.jeesite.modules.report.entity.BalanceSheetSnapshot;
import com.thinkgem.jeesite.modules.report.service.BalanceSheetSnapshotService;

/**
 * 资产负债表Controller
 * @author zzj
 * @version 2019-01-08
 */
@Controller
@RequestMapping(value = "${adminPath}/report/balanceSheetSnapshot")
public class BalanceSheetSnapshotController extends BaseController {

	@Autowired
	private BalanceSheetSnapshotService balanceSheetSnapshotService;
	
	@ModelAttribute
	public BalanceSheetSnapshot get(@RequestParam(required=false) String id) {
		BalanceSheetSnapshot entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = balanceSheetSnapshotService.get(id);
		}
		if (entity == null){
			entity = new BalanceSheetSnapshot();
		}
		return entity;
	}
	
	@RequiresPermissions("report:balanceSheetSnapshot:view")
	@RequestMapping(value = {"list", ""})
	public String list(BalanceSheetSnapshot balanceSheetSnapshot, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BalanceSheetSnapshot> page = balanceSheetSnapshotService.findPage(new Page<BalanceSheetSnapshot>(request, response), balanceSheetSnapshot); 
		model.addAttribute("page", page);
		return "modules/report/balanceSheetSnapshotList";
	}

	@RequiresPermissions("report:balanceSheetSnapshot:view")
	@RequestMapping(value = "form")
	public String form(BalanceSheetSnapshot balanceSheetSnapshot, Model model) {
		model.addAttribute("balanceSheetSnapshot", balanceSheetSnapshot);
		return "modules/report/balanceSheetSnapshotForm";
	}

	@RequiresPermissions("report:balanceSheetSnapshot:edit")
	@RequestMapping(value = "save")
	public String save(BalanceSheetSnapshot balanceSheetSnapshot, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, balanceSheetSnapshot)){
			return form(balanceSheetSnapshot, model);
		}
		balanceSheetSnapshotService.save(balanceSheetSnapshot);
		addMessage(redirectAttributes, "保存资产负债表成功");
		return "redirect:"+Global.getAdminPath()+"/report/balanceSheetSnapshot/?repage";
	}
	
	@RequiresPermissions("report:balanceSheetSnapshot:edit")
	@RequestMapping(value = "delete")
	public String delete(BalanceSheetSnapshot balanceSheetSnapshot, RedirectAttributes redirectAttributes) {
		balanceSheetSnapshotService.delete(balanceSheetSnapshot);
		addMessage(redirectAttributes, "删除资产负债表成功");
		return "redirect:"+Global.getAdminPath()+"/report/balanceSheetSnapshot/?repage";
	}

}