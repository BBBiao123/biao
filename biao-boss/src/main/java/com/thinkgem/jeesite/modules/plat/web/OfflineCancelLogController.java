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
import com.thinkgem.jeesite.modules.plat.entity.OfflineCancelLog;
import com.thinkgem.jeesite.modules.plat.service.OfflineCancelLogService;

/**
 * C2C取消记录Controller
 * @author zzj
 * @version 2018-11-06
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/offlineCancelLog")
public class OfflineCancelLogController extends BaseController {

	@Autowired
	private OfflineCancelLogService offlineCancelLogService;
	
	@ModelAttribute
	public OfflineCancelLog get(@RequestParam(required=false) String id) {
		OfflineCancelLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = offlineCancelLogService.get(id);
		}
		if (entity == null){
			entity = new OfflineCancelLog();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:offlineCancelLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(OfflineCancelLog offlineCancelLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OfflineCancelLog> page = offlineCancelLogService.findPage(new Page<OfflineCancelLog>(request, response), offlineCancelLog); 
		model.addAttribute("page", page);
		return "modules/plat/offlineCancelLogList";
	}

	@RequiresPermissions("plat:offlineCancelLog:view")
	@RequestMapping(value = "form")
	public String form(OfflineCancelLog offlineCancelLog, Model model) {
		model.addAttribute("offlineCancelLog", offlineCancelLog);
		return "modules/plat/offlineCancelLogForm";
	}

	@RequiresPermissions("plat:offlineCancelLog:edit")
	@RequestMapping(value = "save")
	public String save(OfflineCancelLog offlineCancelLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, offlineCancelLog)){
			return form(offlineCancelLog, model);
		}
		offlineCancelLogService.save(offlineCancelLog);
		addMessage(redirectAttributes, "保存C2C取消记录成功");
		return "redirect:"+Global.getAdminPath()+"/plat/offlineCancelLog/?repage";
	}
	
	@RequiresPermissions("plat:offlineCancelLog:edit")
	@RequestMapping(value = "delete")
	public String delete(OfflineCancelLog offlineCancelLog, RedirectAttributes redirectAttributes) {
		offlineCancelLogService.delete(offlineCancelLog);
		addMessage(redirectAttributes, "删除C2C取消记录成功");
		return "redirect:"+Global.getAdminPath()+"/plat/offlineCancelLog/?repage";
	}

}