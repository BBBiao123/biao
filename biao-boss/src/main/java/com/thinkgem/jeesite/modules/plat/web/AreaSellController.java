/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

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
import com.thinkgem.jeesite.modules.plat.entity.AreaSell;
import com.thinkgem.jeesite.modules.plat.service.AreaSellService;

/**
 * 区域销售Controller
 * @author dong
 * @version 2018-07-06
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/areaSell")
public class AreaSellController extends BaseController {

	@Autowired
	private AreaSellService areaSellService;
	
	@ModelAttribute
	public AreaSell get(@RequestParam(required=false) String id) {
		AreaSell entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = areaSellService.get(id);
		}
		if (entity == null){
			entity = new AreaSell();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:areaSell:view")
	@RequestMapping(value = {"list", ""})
	public String list(AreaSell areaSell, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AreaSell> page = areaSellService.findPage(new Page<AreaSell>(request, response), areaSell);
		model.addAttribute("page", page);
		request.getSession().setAttribute("searchAreaParam", areaSell);
		return "modules/plat/areaSellList";
	}

	@RequiresPermissions("plat:areaSell:view")
	@RequestMapping(value = "form")
	public String form(AreaSell areaSell, Model model) {
		model.addAttribute("areaSell", areaSell);
		return "modules/plat/areaSellForm";
	}

//	@RequiresPermissions("plat:areaSell:edit")
//	@RequestMapping(value = "save")
//	public String save(AreaSell areaSell, Model model, RedirectAttributes redirectAttributes) {
//		if (!beanValidator(model, areaSell)){
//			return form(areaSell, model);
//		}
//		areaSellService.save(areaSell);
//		addMessage(redirectAttributes, "保存区域销售成功");
//		return "redirect:"+Global.getAdminPath()+"/plat/areaSell/?repage";
//	}
	
//	@RequiresPermissions("plat:areaSell:edit")
//	@RequestMapping(value = "delete")
//	public String delete(AreaSell areaSell, RedirectAttributes redirectAttributes) {
//		areaSellService.delete(areaSell);
//		addMessage(redirectAttributes, "删除区域销售成功");
//		return "redirect:"+Global.getAdminPath()+"/plat/areaSell/?repage";
//	}

	@RequiresPermissions("plat:areaSell:sold")
	@RequestMapping(value = "sold")
	public String sold(AreaSell areaSell, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		AreaSell area = areaSellService.get(areaSell.getId());
		area.setSold("1");
		areaSellService.save(area);
		addMessage(redirectAttributes, area.getAreaName() + "区域售出成功！");
		redirectAttributes.addFlashAttribute("areaSell", request.getSession().getAttribute("searchAreaParam"));
		return "redirect:"+Global.getAdminPath()+"/plat/areaSell/?repage";
	}

	@RequiresPermissions("plat:areaSell:sold")
	@RequestMapping(value = "sale")
	public String sale(AreaSell areaSell, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		AreaSell area = areaSellService.get(areaSell.getId());
		area.setSold("0");
		areaSellService.save(area);
		addMessage(redirectAttributes, area.getAreaName() + "取消成功！");
		redirectAttributes.addFlashAttribute("areaSell", request.getSession().getAttribute("searchAreaParam"));
		return "redirect:"+Global.getAdminPath()+"/plat/areaSell/?repage";
	}

}