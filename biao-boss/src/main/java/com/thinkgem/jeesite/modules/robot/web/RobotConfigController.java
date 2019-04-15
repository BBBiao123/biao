/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.robot.web;

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
import com.thinkgem.jeesite.modules.robot.entity.RobotConfig;
import com.thinkgem.jeesite.modules.robot.service.RobotConfigService;

/**
 * 价格机器人Controller
 * @author dazi
 * @version 2018-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/robot/robotConfig")
public class RobotConfigController extends BaseController {

	@Autowired
	private RobotConfigService robotConfigService;
	
	@ModelAttribute
	public RobotConfig get(@RequestParam(required=false) String id) {
		RobotConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = robotConfigService.get(id);
		}
		if (entity == null){
			entity = new RobotConfig();
		}
		return entity;
	}
	
	@RequiresPermissions("robot:robotConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(RobotConfig robotConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RobotConfig> page = robotConfigService.findPage(new Page<RobotConfig>(request, response), robotConfig); 
		model.addAttribute("page", page);
		return "modules/robot/robotConfigList";
	}

	@RequiresPermissions("robot:robotConfig:view")
	@RequestMapping(value = "form")
	public String form(RobotConfig robotConfig, Model model) {
		model.addAttribute("robotConfig", robotConfig);
		return "modules/robot/robotConfigForm";
	}

	@RequiresPermissions("robot:robotConfig:edit")
	@RequestMapping(value = "save")
	public String save(RobotConfig robotConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, robotConfig)){
			return form(robotConfig, model);
		}
		robotConfigService.save(robotConfig);
		addMessage(redirectAttributes, "保存机器人配置成功");
		return "redirect:"+Global.getAdminPath()+"/robot/robotConfig/?repage";
	}
	
	@RequiresPermissions("robot:robotConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(RobotConfig robotConfig, RedirectAttributes redirectAttributes) {
		robotConfigService.delete(robotConfig);
		addMessage(redirectAttributes, "删除机器人配置成功");
		return "redirect:"+Global.getAdminPath()+"/robot/robotConfig/?repage";
	}

}