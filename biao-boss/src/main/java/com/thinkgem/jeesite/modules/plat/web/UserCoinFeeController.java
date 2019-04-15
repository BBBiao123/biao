/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.ExPair;
import com.thinkgem.jeesite.modules.plat.service.ExPairService;
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
import com.thinkgem.jeesite.modules.plat.entity.UserCoinFee;
import com.thinkgem.jeesite.modules.plat.service.UserCoinFeeService;

import java.util.List;

/**
 * 用户交易对手续费设置Controller
 * @author dapao
 * @version 2018-08-31
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/userCoinFee")
public class UserCoinFeeController extends BaseController {

	@Autowired
	private UserCoinFeeService userCoinFeeService;

	@Autowired
	private ExPairService exPairService;
	
	@ModelAttribute
	public UserCoinFee get(@RequestParam(required=false) String id) {
		UserCoinFee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userCoinFeeService.get(id);
		}
		if (entity == null){
			entity = new UserCoinFee();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:userCoinFee:view")
	@RequestMapping(value = {"list", ""})
	public String list(UserCoinFee userCoinFee, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserCoinFee> page = userCoinFeeService.findPage(new Page<UserCoinFee>(request, response), userCoinFee); 
		model.addAttribute("page", page);
		return "modules/plat/userCoinFeeList";
	}

	@RequiresPermissions("plat:userCoinFee:view")
	@RequestMapping(value = "form")
	public String form(UserCoinFee userCoinFee, Model model) {
		model.addAttribute("userCoinFee", userCoinFee);
		ExPair exPair = new ExPair();
		exPair.setStatus("1");
		List<ExPair> exPairList = exPairService.findList(exPair);
		model.addAttribute(exPairList);
		return "modules/plat/userCoinFeeForm";
	}

	@RequiresPermissions("plat:userCoinFee:edit")
	@RequestMapping(value = "save")
	public String save(UserCoinFee userCoinFee, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, userCoinFee)){
			return form(userCoinFee, model);
		}
		userCoinFeeService.save(userCoinFee);
		addMessage(redirectAttributes, "保存用户交易对手续费设置成功");
		return "redirect:"+Global.getAdminPath()+"/plat/userCoinFee/?repage";
	}
	
	@RequiresPermissions("plat:userCoinFee:edit")
	@RequestMapping(value = "delete")
	public String delete(UserCoinFee userCoinFee, RedirectAttributes redirectAttributes) {
		userCoinFeeService.delete(userCoinFee);
		addMessage(redirectAttributes, "删除用户交易对手续费设置成功");
		return "redirect:"+Global.getAdminPath()+"/plat/userCoinFee/?repage";
	}

}