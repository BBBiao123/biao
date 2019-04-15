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
import com.thinkgem.jeesite.modules.plat.entity.SuperCoinVolumeConf;
import com.thinkgem.jeesite.modules.plat.service.SuperCoinVolumeConfService;

import java.util.List;

/**
 * 超级钱包配置Controller
 * @author zzj
 * @version 2018-12-25
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/superCoinVolumeConf")
public class SuperCoinVolumeConfController extends BaseController {

	@Autowired
	private SuperCoinVolumeConfService superCoinVolumeConfService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public SuperCoinVolumeConf get(@RequestParam(required=false) String id) {
		SuperCoinVolumeConf entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = superCoinVolumeConfService.get(id);
		}
		if (entity == null){
			entity = new SuperCoinVolumeConf();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:superCoinVolumeConf:view")
	@RequestMapping(value = {"list", ""})
	public String list(SuperCoinVolumeConf superCoinVolumeConf, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SuperCoinVolumeConf> page = superCoinVolumeConfService.findPage(new Page<SuperCoinVolumeConf>(request, response), superCoinVolumeConf); 
		model.addAttribute("page", page);
		return "modules/plat/superCoinVolumeConfList";
	}

	@RequiresPermissions("plat:superCoinVolumeConf:view")
	@RequestMapping(value = "form")
	public String form(SuperCoinVolumeConf superCoinVolumeConf, Model model) {
		model.addAttribute("superCoinVolumeConf", superCoinVolumeConf);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/superCoinVolumeConfForm";
	}

	@RequiresPermissions("plat:superCoinVolumeConf:edit")
	@RequestMapping(value = "save")
	public String save(SuperCoinVolumeConf superCoinVolumeConf, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, superCoinVolumeConf)){
			return form(superCoinVolumeConf, model);
		}
		superCoinVolumeConfService.save(superCoinVolumeConf);
		addMessage(redirectAttributes, "保存超级钱包配置成功");
		return "redirect:"+Global.getAdminPath()+"/plat/superCoinVolumeConf/?repage";
	}
	
	@RequiresPermissions("plat:superCoinVolumeConf:edit")
	@RequestMapping(value = "delete")
	public String delete(SuperCoinVolumeConf superCoinVolumeConf, RedirectAttributes redirectAttributes) {
		superCoinVolumeConfService.delete(superCoinVolumeConf);
		addMessage(redirectAttributes, "删除超级钱包配置成功");
		return "redirect:"+Global.getAdminPath()+"/plat/superCoinVolumeConf/?repage";
	}

}