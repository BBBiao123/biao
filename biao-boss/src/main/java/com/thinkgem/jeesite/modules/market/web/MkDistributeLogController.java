/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

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
import com.thinkgem.jeesite.modules.market.entity.MkDistributeLog;
import com.thinkgem.jeesite.modules.market.service.MkDistributeLogService;

/**
 * 营销营销账户资产流水Controller
 * @author zhangzijun
 * @version 2018-07-06
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkDistributeLog")
public class MkDistributeLogController extends BaseController {

	@Autowired
	private MkDistributeLogService mkDistributeLogService;
	
	@ModelAttribute
	public MkDistributeLog get(@RequestParam(required=false) String id) {
		MkDistributeLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkDistributeLogService.get(id);
		}
		if (entity == null){
			entity = new MkDistributeLog();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkDistributeLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkDistributeLog mkDistributeLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkDistributeLog> page = mkDistributeLogService.findPage(new Page<MkDistributeLog>(request, response), mkDistributeLog); 
		model.addAttribute("page", page);
		return "modules/market/mkDistributeLogList";
	}

	@RequiresPermissions("market:mkDistributeLog:view")
	@RequestMapping(value = "form")
	public String form(MkDistributeLog mkDistributeLog, Model model) {
		model.addAttribute("mkDistributeLog", mkDistributeLog);
		return "modules/market/mkDistributeLogForm";
	}

	@RequiresPermissions("market:mkDistributeLog:edit")
	@RequestMapping(value = "save")
	public String save(MkDistributeLog mkDistributeLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkDistributeLog)){
			return form(mkDistributeLog, model);
		}
		mkDistributeLogService.save(mkDistributeLog);
		addMessage(redirectAttributes, "保存营销账户资产流水成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDistributeLog/?repage";
	}
	
	@RequiresPermissions("market:mkDistributeLog:edit")
	@RequestMapping(value = "delete")
	public String delete(MkDistributeLog mkDistributeLog, RedirectAttributes redirectAttributes) {
		mkDistributeLogService.delete(mkDistributeLog);
		addMessage(redirectAttributes, "删除营销账户资产流水成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDistributeLog/?repage";
	}

}