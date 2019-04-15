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
import com.thinkgem.jeesite.modules.plat.entity.CoinCollection;
import com.thinkgem.jeesite.modules.plat.service.CoinCollectionService;

/**
 * 币种归集Controller
 * @author tt
 * @version 2019-03-18
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/coinCollection")
public class CoinCollectionController extends BaseController {

	@Autowired
	private CoinCollectionService coinCollectionService;
	
	@ModelAttribute
	public CoinCollection get(@RequestParam(required=false) String id) {
		CoinCollection entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coinCollectionService.get(id);
		}
		if (entity == null){
			entity = new CoinCollection();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:coinCollection:view")
	@RequestMapping(value = {"list", ""})
	public String list(CoinCollection coinCollection, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoinCollection> page = coinCollectionService.findPage(new Page<CoinCollection>(request, response), coinCollection); 
		model.addAttribute("page", page);
		return "modules/plat/coinCollectionList";
	}

	@RequiresPermissions("plat:coinCollection:view")
	@RequestMapping(value = "form")
	public String form(CoinCollection coinCollection, Model model) {
		model.addAttribute("coinCollection", coinCollection);
		return "modules/plat/coinCollectionForm";
	}

	@RequiresPermissions("plat:coinCollection:edit")
	@RequestMapping(value = "save")
	public String save(CoinCollection coinCollection, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, coinCollection)){
			return form(coinCollection, model);
		}
		coinCollectionService.save(coinCollection);
		addMessage(redirectAttributes, "保存币种归集成功");
		return "redirect:"+Global.getAdminPath()+"/plat/coinCollection/?repage";
	}
	
	@RequiresPermissions("plat:coinCollection:edit")
	@RequestMapping(value = "delete")
	public String delete(CoinCollection coinCollection, RedirectAttributes redirectAttributes) {
		coinCollectionService.delete(coinCollection);
		addMessage(redirectAttributes, "删除币种归集成功");
		return "redirect:"+Global.getAdminPath()+"/plat/coinCollection/?repage";
	}

}