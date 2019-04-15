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
import com.thinkgem.jeesite.modules.market.entity.MkRelayAutoRecord;
import com.thinkgem.jeesite.modules.market.service.MkRelayAutoRecordService;

/**
 * 接力自动撞奖配置Controller
 * @author zzj
 * @version 2018-09-26
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkRelayAutoRecord")
public class MkRelayAutoRecordController extends BaseController {

	@Autowired
	private MkRelayAutoRecordService mkRelayAutoRecordService;
	
	@ModelAttribute
	public MkRelayAutoRecord get(@RequestParam(required=false) String id) {
		MkRelayAutoRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkRelayAutoRecordService.get(id);
		}
		if (entity == null){
			entity = new MkRelayAutoRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkRelayAutoRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkRelayAutoRecord mkRelayAutoRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkRelayAutoRecord> page = mkRelayAutoRecordService.findPage(new Page<MkRelayAutoRecord>(request, response), mkRelayAutoRecord); 
		model.addAttribute("page", page);
		return "modules/market/mkRelayAutoRecordList";
	}

	@RequiresPermissions("market:mkRelayAutoRecord:view")
	@RequestMapping(value = "form")
	public String form(MkRelayAutoRecord mkRelayAutoRecord, Model model) {
		model.addAttribute("mkRelayAutoRecord", mkRelayAutoRecord);
		return "modules/market/mkRelayAutoRecordForm";
	}

	@RequiresPermissions("market:mkRelayAutoRecord:edit")
	@RequestMapping(value = "save")
	public String save(MkRelayAutoRecord mkRelayAutoRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkRelayAutoRecord)){
			return form(mkRelayAutoRecord, model);
		}
		mkRelayAutoRecordService.save(mkRelayAutoRecord);
		addMessage(redirectAttributes, "保存接力自动撞奖配置成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayAutoRecord/?repage";
	}
	
	@RequiresPermissions("market:mkRelayAutoRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(MkRelayAutoRecord mkRelayAutoRecord, RedirectAttributes redirectAttributes) {
		mkRelayAutoRecordService.delete(mkRelayAutoRecord);
		addMessage(redirectAttributes, "删除接力自动撞奖配置成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayAutoRecord/?repage";
	}

}