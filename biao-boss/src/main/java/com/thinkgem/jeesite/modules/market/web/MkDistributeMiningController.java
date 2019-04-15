/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

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
import com.thinkgem.jeesite.modules.market.entity.MkDistributeMining;
import com.thinkgem.jeesite.modules.market.service.MkDistributeMiningService;

import java.util.List;

/**
 * 挖矿规则Controller
 * @author zhangzijun
 * @version 2018-07-05
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkDistributeMining")
public class MkDistributeMiningController extends BaseController {

	@Autowired
	private MkDistributeMiningService mkDistributeMiningService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public MkDistributeMining get(@RequestParam(required=false) String id) {
		MkDistributeMining entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkDistributeMiningService.get(id);
		}
		if (entity == null){
			entity = new MkDistributeMining();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mining:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkDistributeMining mkDistributeMining, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkDistributeMining> page = mkDistributeMiningService.findPage(new Page<MkDistributeMining>(request, response), mkDistributeMining); 
		model.addAttribute("page", page);
		return "modules/market/mkDistributeMiningList";
	}

	@RequiresPermissions("market:mining:view")
	@RequestMapping(value = "form")
	public String form(MkDistributeMining mkDistributeMining, Model model) {
		model.addAttribute("mkDistributeMining", mkDistributeMining);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/market/mkDistributeMiningForm";
	}

	@RequiresPermissions("market:mining:edit")
	@RequestMapping(value = "save")
	public String save(MkDistributeMining mkDistributeMining, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkDistributeMining)){
			return form(mkDistributeMining, model);
		}
		if(mkDistributeMiningService.isRepeatEnable(mkDistributeMining)){
			addMessage(redirectAttributes, "只能启动一条规则");
		}else{
			mkDistributeMiningService.save(mkDistributeMining);
			addMessage(redirectAttributes, "保存挖矿规则成功");
		}
		return "redirect:"+Global.getAdminPath()+"/market/mkDistributeMining/?repage";
	}
	
	@RequiresPermissions("market:mining:edit")
	@RequestMapping(value = "delete")
	public String delete(MkDistributeMining mkDistributeMining, RedirectAttributes redirectAttributes) {
		mkDistributeMiningService.delete(mkDistributeMining);
		addMessage(redirectAttributes, "删除挖矿规则成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDistributeMining/?repage";
	}

	@RequiresPermissions("market:mining:edit")
	@RequestMapping(value = "enable")
	public String enable(MkDistributeMining mkDistributeMining, RedirectAttributes redirectAttributes) {
		mkDistributeMining.setStatus("1");

		if(mkDistributeMiningService.isRepeatEnable(mkDistributeMining)){
			addMessage(redirectAttributes, "只能启动一条规则");
		}else{
			mkDistributeMiningService.save(mkDistributeMining);
			addMessage(redirectAttributes, "启用成功");
		}
		return "redirect:"+Global.getAdminPath()+"/market/mkDistributeMining/?repage";
	}

	@RequiresPermissions("market:mining:edit")
	@RequestMapping(value = "forbidden")
	public String forbidden(MkDistributeMining mkDistributeMining, RedirectAttributes redirectAttributes) {
		mkDistributeMining.setStatus("0");
		mkDistributeMiningService.save(mkDistributeMining);
		addMessage(redirectAttributes, "禁用成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDistributeMining/?repage";
	}

}