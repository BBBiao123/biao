/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.entity.ExPair;
import com.thinkgem.jeesite.modules.plat.service.ExPairService;
import com.thinkgem.jeesite.modules.report.entity.RetailInvestorDailyTradeVolume;
import com.thinkgem.jeesite.modules.report.service.RetailInvestorDailyTradeVolumeService;
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
import java.util.List;
import java.util.Objects;

/**
 * 操盘手资产快照Controller
 * @author zzj
 * @version 2018-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/report/retailInvestorDailyTradeVolume")
public class RetailInvestorDailyTradeVolumeController extends BaseController {

	@Autowired
	private RetailInvestorDailyTradeVolumeService retailInvestorDailyTradeVolumeService;

	@Autowired
	private ExPairService exPairService;
	
	@ModelAttribute
	public RetailInvestorDailyTradeVolume get(@RequestParam(required=false) String id) {
		RetailInvestorDailyTradeVolume entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = retailInvestorDailyTradeVolumeService.get(id);
		}
		if (entity == null){
			entity = new RetailInvestorDailyTradeVolume();
		}
		return entity;
	}
	
	@RequiresPermissions("report:retailInvestorDailyTradeVolume:view")
	@RequestMapping(value = {"list", ""})
	public String list(RetailInvestorDailyTradeVolume retailInvestorDailyTradeVolume, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RetailInvestorDailyTradeVolume> page = new Page<>();
		if(Objects.nonNull(retailInvestorDailyTradeVolume.getTradeDate()) || StringUtils.isNotEmpty(retailInvestorDailyTradeVolume.getUserTag())
				|| StringUtils.isNotEmpty(retailInvestorDailyTradeVolume.getCoinSymbol()) || StringUtils.isNotEmpty(retailInvestorDailyTradeVolume.getToCoinSymbol())){
			page = retailInvestorDailyTradeVolumeService.findPage(new Page<RetailInvestorDailyTradeVolume>(request, response), retailInvestorDailyTradeVolume);
		}

		ExPair exPair = new ExPair();
		exPair.setStatus("1");
		List<ExPair> exPairList = exPairService.findList(exPair);
		model.addAttribute(exPairList);

		model.addAttribute("page", page);
		return "modules/report/retailInvestorDailyTradeVolumeList";
	}

	@RequiresPermissions("report:retailInvestorDailyTradeVolume:view")
	@RequestMapping(value = "form")
	public String form(RetailInvestorDailyTradeVolume retailInvestorDailyTradeVolume, Model model) {
		model.addAttribute("retailInvestorDailyTradeVolume", retailInvestorDailyTradeVolume);
		return "modules/report/retailInvestorDailyTradeVolumeForm";
	}

	@RequiresPermissions("report:retailInvestorDailyTradeVolume:edit")
	@RequestMapping(value = "save")
	public String save(RetailInvestorDailyTradeVolume retailInvestorDailyTradeVolume, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, retailInvestorDailyTradeVolume)){
			return form(retailInvestorDailyTradeVolume, model);
		}
		retailInvestorDailyTradeVolumeService.save(retailInvestorDailyTradeVolume);
		addMessage(redirectAttributes, "保存操盘手资产快照成功");
		return "redirect:"+Global.getAdminPath()+"/report/retailInvestorDailyTradeVolume/?repage";
	}
	
	@RequiresPermissions("report:retailInvestorDailyTradeVolume:edit")
	@RequestMapping(value = "delete")
	public String delete(RetailInvestorDailyTradeVolume retailInvestorDailyTradeVolume, RedirectAttributes redirectAttributes) {
		retailInvestorDailyTradeVolumeService.delete(retailInvestorDailyTradeVolume);
		addMessage(redirectAttributes, "删除操盘手资产快照成功");
		return "redirect:"+Global.getAdminPath()+"/report/retailInvestorDailyTradeVolume/?repage";
	}

}