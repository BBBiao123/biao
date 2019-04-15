/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.OfflineCoin;
import com.thinkgem.jeesite.modules.plat.service.OfflineCoinService;
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
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineChangeLog;
import com.thinkgem.jeesite.modules.plat.service.JsPlatOfflineChangeLogService;

import java.util.List;

/**
 * C2C转账记录Controller
 * @author zzj
 * @version 2018-10-26
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/jsPlatOfflineChangeLog")
public class JsPlatOfflineChangeLogController extends BaseController {

	@Autowired
	private JsPlatOfflineChangeLogService jsPlatOfflineChangeLogService;


	@Autowired
	private OfflineCoinService offlineCoinService;
	
	@ModelAttribute
	public JsPlatOfflineChangeLog get(@RequestParam(required=false) String id) {
		JsPlatOfflineChangeLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsPlatOfflineChangeLogService.get(id);
		}
		if (entity == null){
			entity = new JsPlatOfflineChangeLog();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:jsPlatOfflineChangeLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsPlatOfflineChangeLog jsPlatOfflineChangeLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JsPlatOfflineChangeLog> page = jsPlatOfflineChangeLogService.findPage(new Page<JsPlatOfflineChangeLog>(request, response), jsPlatOfflineChangeLog); 
		model.addAttribute("page", page);

		List<OfflineCoin> coinList = offlineCoinService.findList(new OfflineCoin());
		model.addAttribute("coinList", coinList);
		return "modules/plat/jsPlatOfflineChangeLogList";
	}

	@RequiresPermissions("plat:jsPlatOfflineChangeLog:view")
	@RequestMapping(value = "form")
	public String form(JsPlatOfflineChangeLog jsPlatOfflineChangeLog, Model model) {
		model.addAttribute("jsPlatOfflineChangeLog", jsPlatOfflineChangeLog);
		return "modules/plat/jsPlatOfflineChangeLogForm";
	}

	@RequiresPermissions("plat:jsPlatOfflineChangeLog:edit")
	@RequestMapping(value = "save")
	public String save(JsPlatOfflineChangeLog jsPlatOfflineChangeLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsPlatOfflineChangeLog)){
			return form(jsPlatOfflineChangeLog, model);
		}
		jsPlatOfflineChangeLogService.save(jsPlatOfflineChangeLog);
		addMessage(redirectAttributes, "保存C2C转账记录成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatOfflineChangeLog/?repage";
	}
	
	@RequiresPermissions("plat:jsPlatOfflineChangeLog:edit")
	@RequestMapping(value = "delete")
	public String delete(JsPlatOfflineChangeLog jsPlatOfflineChangeLog, RedirectAttributes redirectAttributes) {
		jsPlatOfflineChangeLogService.delete(jsPlatOfflineChangeLog);
		addMessage(redirectAttributes, "删除C2C转账记录成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatOfflineChangeLog/?repage";
	}

}