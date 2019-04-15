/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
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
import com.thinkgem.jeesite.modules.plat.entity.SuperCoinVolume;
import com.thinkgem.jeesite.modules.plat.service.SuperCoinVolumeService;

import java.util.List;

/**
 * 超级钱包资产Controller
 * @author zzj
 * @version 2018-12-25
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/superCoinVolume")
public class SuperCoinVolumeController extends BaseController {

	@Autowired
	private SuperCoinVolumeService superCoinVolumeService;
	
	@ModelAttribute
	public SuperCoinVolume get(@RequestParam(required=false) String id) {
		SuperCoinVolume entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = superCoinVolumeService.get(id);
		}
		if (entity == null){
			entity = new SuperCoinVolume();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:superCoinVolume:view")
	@RequestMapping(value = {"list", ""})
	public String list(SuperCoinVolume superCoinVolume, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SuperCoinVolume> page = superCoinVolumeService.findPage(new Page<SuperCoinVolume>(request, response), superCoinVolume); 
		model.addAttribute("page", page);
		return "modules/plat/superCoinVolumeList";
	}

	@RequiresPermissions("plat:superCoinVolume:view")
	@RequestMapping(value = "form")
	public String form(SuperCoinVolume superCoinVolume, Model model) {
		model.addAttribute("superCoinVolume", superCoinVolume);
		return "modules/plat/superCoinVolumeForm";
	}

	@RequiresPermissions("plat:superCoinVolume:edit")
	@RequestMapping(value = "save")
	public String save(SuperCoinVolume superCoinVolume, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, superCoinVolume)){
			return form(superCoinVolume, model);
		}
		superCoinVolumeService.save(superCoinVolume);
		addMessage(redirectAttributes, "保存超级钱包资产成功");
		return "redirect:"+Global.getAdminPath()+"/plat/superCoinVolume/?repage";
	}
	
	@RequiresPermissions("plat:superCoinVolume:edit")
	@RequestMapping(value = "delete")
	public String delete(SuperCoinVolume superCoinVolume, RedirectAttributes redirectAttributes) {
		superCoinVolumeService.delete(superCoinVolume);
		addMessage(redirectAttributes, "删除超级钱包资产成功");
		return "redirect:"+Global.getAdminPath()+"/plat/superCoinVolume/?repage";
	}

}