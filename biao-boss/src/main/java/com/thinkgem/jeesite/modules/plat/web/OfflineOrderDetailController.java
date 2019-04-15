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
import com.thinkgem.jeesite.modules.plat.entity.OfflineOrderDetail;
import com.thinkgem.jeesite.modules.plat.service.OfflineOrderDetailService;

/**
 * c2c广告详情Controller
 * @author dazi
 * @version 2018-04-30
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/offlineOrderDetail")
public class OfflineOrderDetailController extends BaseController {

	@Autowired
	private OfflineOrderDetailService offlineOrderDetailService;
	
	@ModelAttribute
	public OfflineOrderDetail get(@RequestParam(required=false) String id) {
		OfflineOrderDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = offlineOrderDetailService.get(id);
		}
		if (entity == null){
			entity = new OfflineOrderDetail();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:offlineOrderDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(OfflineOrderDetail offlineOrderDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OfflineOrderDetail> page = offlineOrderDetailService.findPage(new Page<OfflineOrderDetail>(request, response), offlineOrderDetail); 
		model.addAttribute("page", page);
		return "modules/plat/offlineOrderDetailList";
	}

	@RequiresPermissions("plat:offlineOrderDetail:view")
	@RequestMapping(value = "form")
	public String form(OfflineOrderDetail offlineOrderDetail, Model model) {
		model.addAttribute("offlineOrderDetail", offlineOrderDetail);
		return "modules/plat/offlineOrderDetailForm";
	}

	@RequiresPermissions("plat:offlineOrderDetail:edit")
	@RequestMapping(value = "save")
	public String save(OfflineOrderDetail offlineOrderDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, offlineOrderDetail)){
			return form(offlineOrderDetail, model);
		}
		offlineOrderDetailService.save(offlineOrderDetail);
		addMessage(redirectAttributes, "保存c2c广告详情成功");
		return "redirect:"+Global.getAdminPath()+"/plat/offlineOrderDetail/?repage";
	}
	
	@RequiresPermissions("plat:offlineOrderDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(OfflineOrderDetail offlineOrderDetail, RedirectAttributes redirectAttributes) {
		offlineOrderDetailService.delete(offlineOrderDetail);
		addMessage(redirectAttributes, "删除c2c广告详情成功");
		return "redirect:"+Global.getAdminPath()+"/plat/offlineOrderDetail/?repage";
	}

}