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
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.entity.mongo.UserLoginLog;
import com.thinkgem.jeesite.modules.plat.service.mongo.UserLoginLogService;

/**
 * 用户登录日志查询Controller
 * @author ruoyu
 * @version 2018-09-17
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/userLoginLog")
public class UserLoginLogController extends BaseController {

	@Autowired
	private UserLoginLogService userLoginLogService;
	
	@RequiresPermissions("plat:userLoginLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(UserLoginLog userLoginLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserLoginLog> page = userLoginLogService.findPage(new Page<UserLoginLog>(request, response), userLoginLog); 
		model.addAttribute("page", page);
		model.addAttribute(userLoginLog);
		return "modules/plat/userLoginLogList";
	}

}