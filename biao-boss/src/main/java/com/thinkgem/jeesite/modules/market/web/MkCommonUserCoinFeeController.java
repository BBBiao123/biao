/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

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
import com.thinkgem.jeesite.modules.market.entity.MkCommonUserCoinFee;
import com.thinkgem.jeesite.modules.market.service.MkCommonUserCoinFeeService;

/**
 * 平台收益流水Controller
 * @author dongfeng
 * @version 2018-08-09
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkCommonUserCoinFee")
public class MkCommonUserCoinFeeController extends BaseController {

	@Autowired
	private MkCommonUserCoinFeeService mkCommonUserCoinFeeService;
	
	@ModelAttribute
	public MkCommonUserCoinFee get(@RequestParam(required=false) String id) {
		MkCommonUserCoinFee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkCommonUserCoinFeeService.get(id);
		}
		if (entity == null){
			entity = new MkCommonUserCoinFee();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkCommonUserCoinFee:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkCommonUserCoinFee mkCommonUserCoinFee, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkCommonUserCoinFee> page = mkCommonUserCoinFeeService.findPage(new Page<MkCommonUserCoinFee>(request, response), mkCommonUserCoinFee); 
		model.addAttribute("page", page);
		return "modules/market/mkCommonUserCoinFeeList";
	}

	@RequiresPermissions("market:mkCommonUserCoinFee:view")
	@RequestMapping(value = "form")
	public String form(MkCommonUserCoinFee mkCommonUserCoinFee, Model model) {
		model.addAttribute("mkCommonUserCoinFee", mkCommonUserCoinFee);
		return "modules/market/mkCommonUserCoinFeeForm";
	}

	@RequiresPermissions("market:mkCommonUserCoinFee:edit")
	@RequestMapping(value = "save")
	public String save(MkCommonUserCoinFee mkCommonUserCoinFee, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkCommonUserCoinFee)){
			return form(mkCommonUserCoinFee, model);
		}
		mkCommonUserCoinFeeService.save(mkCommonUserCoinFee);
		addMessage(redirectAttributes, "保存平台收益流水成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkCommonUserCoinFee/?repage";
	}
	
	@RequiresPermissions("market:mkCommonUserCoinFee:edit")
	@RequestMapping(value = "delete")
	public String delete(MkCommonUserCoinFee mkCommonUserCoinFee, RedirectAttributes redirectAttributes) {
		mkCommonUserCoinFeeService.delete(mkCommonUserCoinFee);
		addMessage(redirectAttributes, "删除平台收益流水成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkCommonUserCoinFee/?repage";
	}

}