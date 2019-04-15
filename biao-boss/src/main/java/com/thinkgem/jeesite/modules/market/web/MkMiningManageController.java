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
import com.thinkgem.jeesite.modules.market.entity.MkMiningManage;
import com.thinkgem.jeesite.modules.market.service.MkMiningManageService;

/**
 * 挖矿规则管理Controller
 * @author zhangzijun
 * @version 2018-07-13
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkMiningManage")
public class MkMiningManageController extends BaseController {

	@Autowired
	private MkMiningManageService mkMiningManageService;
	
	@ModelAttribute
	public MkMiningManage get(@RequestParam(required=false) String id) {
		MkMiningManage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkMiningManageService.get(id);
		}
		if (entity == null){
			entity = new MkMiningManage();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkMiningManage:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkMiningManage mkMiningManage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkMiningManage> page = mkMiningManageService.findPage(new Page<MkMiningManage>(request, response), mkMiningManage); 
		model.addAttribute("page", page);
		return "modules/market/mkMiningManageList";
	}

	@RequiresPermissions("market:mkMiningManage:view")
	@RequestMapping(value = "form")
	public String form(MkMiningManage mkMiningManage, Model model) {
		model.addAttribute("mkMiningManage", mkMiningManage);
		return "modules/market/mkMiningManageForm";
	}

	@RequiresPermissions("market:mkMiningManage:edit")
	@RequestMapping(value = "save")
	public String save(MkMiningManage mkMiningManage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkMiningManage)){
			return form(mkMiningManage, model);
		}
		mkMiningManageService.save(mkMiningManage);
		addMessage(redirectAttributes, "保存挖矿规则管理成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkMiningManage/?repage";
	}
	
	@RequiresPermissions("market:mkMiningManage:edit")
	@RequestMapping(value = "delete")
	public String delete(MkMiningManage mkMiningManage, RedirectAttributes redirectAttributes) {
		mkMiningManageService.delete(mkMiningManage);
		addMessage(redirectAttributes, "删除挖矿规则管理成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkMiningManage/?repage";
	}

}