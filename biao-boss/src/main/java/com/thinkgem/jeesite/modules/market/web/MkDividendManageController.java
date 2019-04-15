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
import com.thinkgem.jeesite.modules.market.entity.MkDividendManage;
import com.thinkgem.jeesite.modules.market.service.MkDividendManageService;

/**
 * 分红规则管理Controller
 * @author zhangzijun
 * @version 2018-07-13
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkDividendManage")
public class MkDividendManageController extends BaseController {

	@Autowired
	private MkDividendManageService mkDividendManageService;
	
	@ModelAttribute
	public MkDividendManage get(@RequestParam(required=false) String id) {
		MkDividendManage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkDividendManageService.get(id);
		}
		if (entity == null){
			entity = new MkDividendManage();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkDividendManage:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkDividendManage mkDividendManage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkDividendManage> page = mkDividendManageService.findPage(new Page<MkDividendManage>(request, response), mkDividendManage); 
		model.addAttribute("page", page);
		return "modules/market/mkDividendManageList";
	}

	@RequiresPermissions("market:mkDividendManage:view")
	@RequestMapping(value = "form")
	public String form(MkDividendManage mkDividendManage, Model model) {
		model.addAttribute("mkDividendManage", mkDividendManage);
		return "modules/market/mkDividendManageForm";
	}

	@RequiresPermissions("market:mkDividendManage:edit")
	@RequestMapping(value = "save")
	public String save(MkDividendManage mkDividendManage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkDividendManage)){
			return form(mkDividendManage, model);
		}
		mkDividendManageService.save(mkDividendManage);
		addMessage(redirectAttributes, "保存分红规则管理成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDividendManage/?repage";
	}
	
	@RequiresPermissions("market:mkDividendManage:edit")
	@RequestMapping(value = "delete")
	public String delete(MkDividendManage mkDividendManage, RedirectAttributes redirectAttributes) {
		mkDividendManageService.delete(mkDividendManage);
		addMessage(redirectAttributes, "删除分红规则管理成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDividendManage/?repage";
	}

}