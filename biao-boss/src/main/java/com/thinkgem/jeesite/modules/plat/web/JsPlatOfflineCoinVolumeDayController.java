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
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineCoinVolumeDay;
import com.thinkgem.jeesite.modules.plat.service.JsPlatOfflineCoinVolumeDayService;

/**
 * c2c银商和广告商对账Controller
 * @author ruoyu
 * @version 2018-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/jsPlatOfflineCoinVolumeDay")
public class JsPlatOfflineCoinVolumeDayController extends BaseController {

	@Autowired
	private JsPlatOfflineCoinVolumeDayService jsPlatOfflineCoinVolumeDayService;
	
	@ModelAttribute
	public JsPlatOfflineCoinVolumeDay get(@RequestParam(required=false) String id) {
		JsPlatOfflineCoinVolumeDay entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsPlatOfflineCoinVolumeDayService.get(id);
		}
		if (entity == null){
			entity = new JsPlatOfflineCoinVolumeDay();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:jsPlatOfflineCoinVolumeDay:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsPlatOfflineCoinVolumeDay jsPlatOfflineCoinVolumeDay, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JsPlatOfflineCoinVolumeDay> page = jsPlatOfflineCoinVolumeDayService.findPage(new Page<JsPlatOfflineCoinVolumeDay>(request, response), jsPlatOfflineCoinVolumeDay); 
		model.addAttribute("page", page);
		return "modules/plat/jsPlatOfflineCoinVolumeDayList";
	}

	@RequiresPermissions("plat:jsPlatOfflineCoinVolumeDay:view")
	@RequestMapping(value = "form")
	public String form(JsPlatOfflineCoinVolumeDay jsPlatOfflineCoinVolumeDay, Model model) {
		model.addAttribute("jsPlatOfflineCoinVolumeDay", jsPlatOfflineCoinVolumeDay);
		return "modules/plat/jsPlatOfflineCoinVolumeDayForm";
	}

	@RequiresPermissions("plat:jsPlatOfflineCoinVolumeDay:edit")
	@RequestMapping(value = "save")
	public String save(JsPlatOfflineCoinVolumeDay jsPlatOfflineCoinVolumeDay, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsPlatOfflineCoinVolumeDay)){
			return form(jsPlatOfflineCoinVolumeDay, model);
		}
		jsPlatOfflineCoinVolumeDayService.save(jsPlatOfflineCoinVolumeDay);
		addMessage(redirectAttributes, "保存c2c银商和广告商对账成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatOfflineCoinVolumeDay/?repage";
	}
	
	@RequiresPermissions("plat:jsPlatOfflineCoinVolumeDay:edit")
	@RequestMapping(value = "delete")
	public String delete(JsPlatOfflineCoinVolumeDay jsPlatOfflineCoinVolumeDay, RedirectAttributes redirectAttributes) {
		jsPlatOfflineCoinVolumeDayService.delete(jsPlatOfflineCoinVolumeDay);
		addMessage(redirectAttributes, "删除c2c银商和广告商对账成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatOfflineCoinVolumeDay/?repage";
	}

}