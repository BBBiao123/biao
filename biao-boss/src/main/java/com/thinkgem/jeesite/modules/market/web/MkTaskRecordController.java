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
import com.thinkgem.jeesite.modules.market.entity.MkTaskRecord;
import com.thinkgem.jeesite.modules.market.service.MkTaskRecordService;

/**
 * 营销任务执行记录Controller
 * @author zhangzijun
 * @version 2018-07-06
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkTaskRecord")
public class MkTaskRecordController extends BaseController {

	@Autowired
	private MkTaskRecordService mkTaskRecordService;
	
	@ModelAttribute
	public MkTaskRecord get(@RequestParam(required=false) String id) {
		MkTaskRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkTaskRecordService.get(id);
		}
		if (entity == null){
			entity = new MkTaskRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkTaskRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkTaskRecord mkTaskRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkTaskRecord> page = mkTaskRecordService.findPage(new Page<MkTaskRecord>(request, response), mkTaskRecord); 
		model.addAttribute("page", page);
		return "modules/market/mkTaskRecordList";
	}

	@RequiresPermissions("market:mkTaskRecord:view")
	@RequestMapping(value = "form")
	public String form(MkTaskRecord mkTaskRecord, Model model) {
		model.addAttribute("mkTaskRecord", mkTaskRecord);
		return "modules/market/mkTaskRecordForm";
	}

	@RequiresPermissions("market:mkTaskRecord:edit")
	@RequestMapping(value = "save")
	public String save(MkTaskRecord mkTaskRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkTaskRecord)){
			return form(mkTaskRecord, model);
		}
		mkTaskRecordService.save(mkTaskRecord);
		addMessage(redirectAttributes, "保存营销任务执行记录成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkTaskRecord/?repage";
	}
	
	@RequiresPermissions("market:mkTaskRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(MkTaskRecord mkTaskRecord, RedirectAttributes redirectAttributes) {
		mkTaskRecordService.delete(mkTaskRecord);
		addMessage(redirectAttributes, "删除营销任务执行记录成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkTaskRecord/?repage";
	}

}