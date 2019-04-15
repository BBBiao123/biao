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
import com.thinkgem.jeesite.modules.market.entity.MkAutoTradeUser;
import com.thinkgem.jeesite.modules.market.service.MkAutoTradeUserService;

/**
 * 自动交易用户Controller
 * @author zhangzijun
 * @version 2018-08-17
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkAutoTradeUser")
public class MkAutoTradeUserController extends BaseController {

	@Autowired
	private MkAutoTradeUserService mkAutoTradeUserService;
	
	@ModelAttribute
	public MkAutoTradeUser get(@RequestParam(required=false) String id) {
		MkAutoTradeUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkAutoTradeUserService.get(id);
		}
		if (entity == null){
			entity = new MkAutoTradeUser();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkAutoTradeUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkAutoTradeUser mkAutoTradeUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkAutoTradeUser> page = mkAutoTradeUserService.findPage(new Page<MkAutoTradeUser>(request, response), mkAutoTradeUser); 
		model.addAttribute("page", page);
		return "modules/market/mkAutoTradeUserList";
	}

	@RequiresPermissions("market:mkAutoTradeUser:view")
	@RequestMapping(value = "form")
	public String form(MkAutoTradeUser mkAutoTradeUser, Model model) {
		model.addAttribute("mkAutoTradeUser", mkAutoTradeUser);
		return "modules/market/mkAutoTradeUserForm";
	}

	@RequiresPermissions("market:mkAutoTradeUser:edit")
	@RequestMapping(value = "save")
	public String save(MkAutoTradeUser mkAutoTradeUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkAutoTradeUser)){
			return form(mkAutoTradeUser, model);
		}

		if(mkAutoTradeUser.getIsNewRecord()){
			mkAutoTradeUser.setSource("add");
		}

		mkAutoTradeUserService.save(mkAutoTradeUser);
		addMessage(redirectAttributes, "保存自动交易用户成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkAutoTradeUser/?repage";
	}
	
	@RequiresPermissions("market:mkAutoTradeUser:edit")
	@RequestMapping(value = "delete")
	public String delete(MkAutoTradeUser mkAutoTradeUser, RedirectAttributes redirectAttributes) {
		mkAutoTradeUserService.delete(mkAutoTradeUser);
		addMessage(redirectAttributes, "删除自动交易用户成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkAutoTradeUser/?repage";
	}

}