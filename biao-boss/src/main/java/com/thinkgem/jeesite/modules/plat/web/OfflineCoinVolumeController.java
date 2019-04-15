/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.service.ServiceException;
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
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoinVolume;
import com.thinkgem.jeesite.modules.plat.service.OfflineCoinVolumeService;

import java.math.BigDecimal;

/**
 * c2c资产Controller
 * @author dazi
 * @version 2018-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/offlineCoinVolume")
public class OfflineCoinVolumeController extends BaseController {

	@Autowired
	private OfflineCoinVolumeService offlineCoinVolumeService;
	
	@ModelAttribute
	public OfflineCoinVolume get(@RequestParam(required=false) String id) {
		OfflineCoinVolume entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = offlineCoinVolumeService.get(id);
		}
		if (entity == null){
			entity = new OfflineCoinVolume();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:offlineCoinVolume:view")
	@RequestMapping(value = {"list", ""})
	public String list(OfflineCoinVolume offlineCoinVolume, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OfflineCoinVolume> page = offlineCoinVolumeService.findPage(new Page<OfflineCoinVolume>(request, response), offlineCoinVolume); 
		model.addAttribute("page", page);
		return "modules/plat/offlineCoinVolumeList";
	}

	@RequiresPermissions("plat:offlineCoinVolume:view")
	@RequestMapping(value = "form")
	public String form(OfflineCoinVolume offlineCoinVolume, Model model) {
		model.addAttribute("offlineCoinVolume", offlineCoinVolume);
		return "modules/plat/offlineCoinVolumeForm";
	}

	@RequiresPermissions("plat:offlineCoinVolume:edit")
	@RequestMapping(value = "save")
	public String save(OfflineCoinVolume offlineCoinVolume, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, offlineCoinVolume)){
			return form(offlineCoinVolume, model);
		}
		offlineCoinVolumeService.save(offlineCoinVolume);
		addMessage(redirectAttributes, "保存c2c资产成功");
		return "redirect:"+Global.getAdminPath()+"/plat/offlineCoinVolume/?repage";
	}
	
	@RequiresPermissions("plat:offlineCoinVolume:edit")
	@RequestMapping(value = "delete")
	public String delete(OfflineCoinVolume offlineCoinVolume, RedirectAttributes redirectAttributes) {
		offlineCoinVolumeService.delete(offlineCoinVolume);
		addMessage(redirectAttributes, "删除c2c资产成功");
		return "redirect:"+Global.getAdminPath()+"/plat/offlineCoinVolume/?repage";
	}

	@RequiresPermissions("plat:offlineCoinVolume:edit")
	@RequestMapping(value = "transfer")
	public String transfer(OfflineCoinVolume offlineCoinVolume, RedirectAttributes redirectAttributes) {
		if(new BigDecimal(offlineCoinVolume.getVolume()).compareTo(BigDecimal.ZERO) <= 0){
			addMessage(redirectAttributes, "资产不足，划转失败！");
		}else{
			offlineCoinVolumeService.transfer(offlineCoinVolume);
			addMessage(redirectAttributes, "划转c2c资产成功");
		}
		return "redirect:"+Global.getAdminPath()+"/plat/offlineCoinVolume/?repage";
	}
}