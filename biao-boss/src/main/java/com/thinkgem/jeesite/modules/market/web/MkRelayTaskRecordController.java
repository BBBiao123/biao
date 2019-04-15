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
import com.thinkgem.jeesite.modules.market.entity.MkRelayTaskRecord;
import com.thinkgem.jeesite.modules.market.service.MkRelayTaskRecordService;

/**
 * 接力撞奖执行记录Controller
 * @author zzj
 * @version 2018-09-05
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkRelayTaskRecord")
public class MkRelayTaskRecordController extends BaseController {

	@Autowired
	private MkRelayTaskRecordService mkRelayTaskRecordService;
	
	@ModelAttribute
	public MkRelayTaskRecord get(@RequestParam(required=false) String id) {
		MkRelayTaskRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkRelayTaskRecordService.get(id);
		}
		if (entity == null){
			entity = new MkRelayTaskRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkRelayTaskRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkRelayTaskRecord mkRelayTaskRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkRelayTaskRecord> page = mkRelayTaskRecordService.findPage(new Page<MkRelayTaskRecord>(request, response), mkRelayTaskRecord); 
		model.addAttribute("page", page);
		return "modules/market/mkRelayTaskRecordList";
	}

	@RequiresPermissions("market:mkRelayTaskRecord:view")
	@RequestMapping(value = "form")
	public String form(MkRelayTaskRecord mkRelayTaskRecord, Model model) {
		model.addAttribute("mkRelayTaskRecord", mkRelayTaskRecord);
		return "modules/market/mkRelayTaskRecordForm";
	}

	@RequiresPermissions("market:mkRelayTaskRecord:edit")
	@RequestMapping(value = "save")
	public String save(MkRelayTaskRecord mkRelayTaskRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkRelayTaskRecord)){
			return form(mkRelayTaskRecord, model);
		}
		mkRelayTaskRecordService.save(mkRelayTaskRecord);
		addMessage(redirectAttributes, "保存接力撞奖执行记录成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayTaskRecord/?repage";
	}
	
	@RequiresPermissions("market:mkRelayTaskRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(MkRelayTaskRecord mkRelayTaskRecord, RedirectAttributes redirectAttributes) {
		mkRelayTaskRecordService.delete(mkRelayTaskRecord);
		addMessage(redirectAttributes, "删除接力撞奖执行记录成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayTaskRecord/?repage";
	}

}