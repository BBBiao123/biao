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
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeReleaseLog;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeReleaseLogService;

/**
 * 冻结数量释放记录Controller
 * @author dongfeng
 * @version 2018-08-09
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeReleaseLog")
public class Mk2PopularizeReleaseLogController extends BaseController {

	@Autowired
	private Mk2PopularizeReleaseLogService mk2PopularizeReleaseLogService;
	
	@ModelAttribute
	public Mk2PopularizeReleaseLog get(@RequestParam(required=false) String id) {
		Mk2PopularizeReleaseLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeReleaseLogService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeReleaseLog();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mk2PopularizeReleaseLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeReleaseLog mk2PopularizeReleaseLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeReleaseLog> page = mk2PopularizeReleaseLogService.findPage(new Page<Mk2PopularizeReleaseLog>(request, response), mk2PopularizeReleaseLog); 
		model.addAttribute("page", page);
		request.getSession().setAttribute("searchMk2PopularizeReleaseLog", mk2PopularizeReleaseLog);
		return "modules/market/mk2PopularizeReleaseLogList";
	}

	@RequiresPermissions("market:mk2PopularizeReleaseLog:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeReleaseLog mk2PopularizeReleaseLog, Model model) {
		model.addAttribute("mk2PopularizeReleaseLog", mk2PopularizeReleaseLog);
		return "modules/market/mk2PopularizeReleaseLogForm";
	}

	@RequiresPermissions("market:mk2PopularizeReleaseLog:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeReleaseLog mk2PopularizeReleaseLog, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, mk2PopularizeReleaseLog)){
			return form(mk2PopularizeReleaseLog, model);
		}
		mk2PopularizeReleaseLogService.save(mk2PopularizeReleaseLog);
		addMessage(redirectAttributes, "保存冻结数量释放记录成功");
		redirectAttributes.addFlashAttribute("mk2PopularizeReleaseLog", request.getSession().getAttribute("searchMk2PopularizeReleaseLog"));
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeReleaseLog/?repage";
	}
	
	@RequiresPermissions("market:mk2PopularizeReleaseLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeReleaseLog mk2PopularizeReleaseLog, RedirectAttributes redirectAttributes) {
		mk2PopularizeReleaseLogService.delete(mk2PopularizeReleaseLog);
		addMessage(redirectAttributes, "删除冻结数量释放记录成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeReleaseLog/?repage";
	}

}