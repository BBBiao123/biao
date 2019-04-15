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
import com.thinkgem.jeesite.modules.market.entity.MkRelayRemitLog;
import com.thinkgem.jeesite.modules.market.service.MkRelayRemitLogService;

/**
 * 接力撞奖打款日志Controller
 * @author zzj
 * @version 2018-09-05
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkRelayRemitLog")
public class MkRelayRemitLogController extends BaseController {

	@Autowired
	private MkRelayRemitLogService mkRelayRemitLogService;
	
	@ModelAttribute
	public MkRelayRemitLog get(@RequestParam(required=false) String id) {
		MkRelayRemitLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkRelayRemitLogService.get(id);
		}
		if (entity == null){
			entity = new MkRelayRemitLog();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkRelayRemitLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkRelayRemitLog mkRelayRemitLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkRelayRemitLog> page = mkRelayRemitLogService.findPage(new Page<MkRelayRemitLog>(request, response), mkRelayRemitLog); 
		model.addAttribute("page", page);
		return "modules/market/mkRelayRemitLogList";
	}

	@RequiresPermissions("market:mkRelayRemitLog:view")
	@RequestMapping(value = "form")
	public String form(MkRelayRemitLog mkRelayRemitLog, Model model) {
		model.addAttribute("mkRelayRemitLog", mkRelayRemitLog);
		return "modules/market/mkRelayRemitLogForm";
	}

	@RequiresPermissions("market:mkRelayRemitLog:edit")
	@RequestMapping(value = "save")
	public String save(MkRelayRemitLog mkRelayRemitLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkRelayRemitLog)){
			return form(mkRelayRemitLog, model);
		}
		mkRelayRemitLogService.save(mkRelayRemitLog);
		addMessage(redirectAttributes, "保存接力撞奖打款日志成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayRemitLog/?repage";
	}
	
	@RequiresPermissions("market:mkRelayRemitLog:edit")
	@RequestMapping(value = "delete")
	public String delete(MkRelayRemitLog mkRelayRemitLog, RedirectAttributes redirectAttributes) {
		mkRelayRemitLogService.delete(mkRelayRemitLog);
		addMessage(redirectAttributes, "删除接力撞奖打款日志成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayRemitLog/?repage";
	}

}