/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import java.util.List;

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
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.RobotConfigUnsafe;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.RobotConfigUnsafeService;

/**
 * 币安自动化管理Controller
 * @author xiaoyu
 * @version 2018-12-25
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/robotConfigUnsafe")
public class RobotConfigUnsafeController extends BaseController {

	@Autowired
	private RobotConfigUnsafeService robotConfigUnsafeService;
	
	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public RobotConfigUnsafe get(@RequestParam(required=false) String id) {
		RobotConfigUnsafe entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = robotConfigUnsafeService.get(id);
		}
		if (entity == null){
			entity = new RobotConfigUnsafe();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:robotConfigUnsafe:view")
	@RequestMapping(value = {"list", ""})
	public String list(RobotConfigUnsafe robotConfigUnsafe, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RobotConfigUnsafe> page = robotConfigUnsafeService.findPage(new Page<RobotConfigUnsafe>(request, response), robotConfigUnsafe); 
		model.addAttribute("page", page);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/robotConfigUnsafeList";
	}

	@RequiresPermissions("plat:robotConfigUnsafe:view")
	@RequestMapping(value = "form")
	public String form(RobotConfigUnsafe robotConfigUnsafe, Model model) {
		model.addAttribute("robotConfigUnsafe", robotConfigUnsafe);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/robotConfigUnsafeForm";
	}

	@RequiresPermissions("plat:robotConfigUnsafe:edit")
	@RequestMapping(value = "save")
	public String save(RobotConfigUnsafe robotConfigUnsafe, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, robotConfigUnsafe)){
			return form(robotConfigUnsafe, model);
		}
		robotConfigUnsafeService.save(robotConfigUnsafe);
		addMessage(redirectAttributes, "保存币安自动化管理成功");
		return "redirect:"+Global.getAdminPath()+"/plat/robotConfigUnsafe/?repage";
	}
	
	@RequiresPermissions("plat:robotConfigUnsafe:edit")
	@RequestMapping(value = "delete")
	public String delete(RobotConfigUnsafe robotConfigUnsafe, RedirectAttributes redirectAttributes) {
		robotConfigUnsafeService.delete(robotConfigUnsafe);
		addMessage(redirectAttributes, "删除币安自动化管理成功");
		return "redirect:"+Global.getAdminPath()+"/plat/robotConfigUnsafe/?repage";
	}

}