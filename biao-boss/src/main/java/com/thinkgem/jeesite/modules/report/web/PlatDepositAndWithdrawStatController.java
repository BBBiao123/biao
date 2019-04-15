/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.report.entity.PlatDepositAndWithdrawStat;
import com.thinkgem.jeesite.modules.report.service.PlatDepositAndWithdrawStatService;
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
import java.util.Objects;

/**
 * 操盘手资产快照Controller
 * @author zzj
 * @version 2018-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/report/platDepositAndWithdrawStat")
public class PlatDepositAndWithdrawStatController extends BaseController {

	@Autowired
	private PlatDepositAndWithdrawStatService platDepositAndWithdrawStatService;
	
	@ModelAttribute
	public PlatDepositAndWithdrawStat get(@RequestParam(required=false) String id) {
		PlatDepositAndWithdrawStat entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = platDepositAndWithdrawStatService.get(id);
		}
		if (entity == null){
			entity = new PlatDepositAndWithdrawStat();
		}
		return entity;
	}
	
	@RequiresPermissions("report:platDepositAndWithdrawStat:view")
	@RequestMapping(value = {"list", ""})
	public String list(PlatDepositAndWithdrawStat platDepositAndWithdrawStat, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PlatDepositAndWithdrawStat> page = new Page<>();
		if(Objects.nonNull(platDepositAndWithdrawStat.getBeginCreateDate()) && Objects.nonNull(platDepositAndWithdrawStat.getEndCreateDate())){
			page = platDepositAndWithdrawStatService.findPage(new Page<PlatDepositAndWithdrawStat>(request, response), platDepositAndWithdrawStat);
		}
		model.addAttribute("page", page);
		return "modules/report/platDepositAndWithdrawStatList";
	}

	@RequiresPermissions("report:platDepositAndWithdrawStat:view")
	@RequestMapping(value = "form")
	public String form(PlatDepositAndWithdrawStat platDepositAndWithdrawStat, Model model) {
		model.addAttribute("platDepositAndWithdrawStat", platDepositAndWithdrawStat);
		return "modules/report/platDepositAndWithdrawStatForm";
	}

	@RequiresPermissions("report:platDepositAndWithdrawStat:edit")
	@RequestMapping(value = "save")
	public String save(PlatDepositAndWithdrawStat platDepositAndWithdrawStat, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, platDepositAndWithdrawStat)){
			return form(platDepositAndWithdrawStat, model);
		}
		platDepositAndWithdrawStatService.save(platDepositAndWithdrawStat);
		addMessage(redirectAttributes, "保存操盘手资产快照成功");
		return "redirect:"+Global.getAdminPath()+"/report/platDepositAndWithdrawStat/?repage";
	}
	
	@RequiresPermissions("report:platDepositAndWithdrawStat:edit")
	@RequestMapping(value = "delete")
	public String delete(PlatDepositAndWithdrawStat platDepositAndWithdrawStat, RedirectAttributes redirectAttributes) {
		platDepositAndWithdrawStatService.delete(platDepositAndWithdrawStat);
		addMessage(redirectAttributes, "删除操盘手资产快照成功");
		return "redirect:"+Global.getAdminPath()+"/report/platDepositAndWithdrawStat/?repage";
	}

}