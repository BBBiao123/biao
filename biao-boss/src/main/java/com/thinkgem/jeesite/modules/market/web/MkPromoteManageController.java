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
import com.thinkgem.jeesite.modules.market.entity.MkPromoteManage;
import com.thinkgem.jeesite.modules.market.service.MkPromoteManageService;

/**
 * 会员推广规则管理Controller
 * @author zhangzijun
 * @version 2018-07-16
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkPromoteManage")
public class MkPromoteManageController extends BaseController {

	@Autowired
	private MkPromoteManageService mkPromoteManageService;
	
	@ModelAttribute
	public MkPromoteManage get(@RequestParam(required=false) String id) {
		MkPromoteManage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkPromoteManageService.get(id);
		}
		if (entity == null){
			entity = new MkPromoteManage();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkPromoteManage:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkPromoteManage mkPromoteManage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkPromoteManage> page = mkPromoteManageService.findPage(new Page<MkPromoteManage>(request, response), mkPromoteManage); 
		model.addAttribute("page", page);
		return "modules/market/mkPromoteManageList";
	}

	@RequiresPermissions("market:mkPromoteManage:view")
	@RequestMapping(value = "form")
	public String form(MkPromoteManage mkPromoteManage, Model model) {
		model.addAttribute("mkPromoteManage", mkPromoteManage);
		return "modules/market/mkPromoteManageForm";
	}

	@RequiresPermissions("market:mkPromoteManage:edit")
	@RequestMapping(value = "save")
	public String save(MkPromoteManage mkPromoteManage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkPromoteManage)){
			return form(mkPromoteManage, model);
		}
		mkPromoteManageService.save(mkPromoteManage);
		addMessage(redirectAttributes, "保存会员推广规则管理成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkPromoteManage/?repage";
	}
	
	@RequiresPermissions("market:mkPromoteManage:edit")
	@RequestMapping(value = "delete")
	public String delete(MkPromoteManage mkPromoteManage, RedirectAttributes redirectAttributes) {
		mkPromoteManageService.delete(mkPromoteManage);
		addMessage(redirectAttributes, "删除会员推广规则管理成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkPromoteManage/?repage";
	}

}