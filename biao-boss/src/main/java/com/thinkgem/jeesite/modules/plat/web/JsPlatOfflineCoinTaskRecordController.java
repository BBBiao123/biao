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
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineCoinTaskRecord;
import com.thinkgem.jeesite.modules.plat.service.JsPlatOfflineCoinTaskRecordService;

/**
 * C2C币种价格更新记录Controller
 * @author zzj
 * @version 2018-10-09
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/jsPlatOfflineCoinTaskRecord")
public class JsPlatOfflineCoinTaskRecordController extends BaseController {

	@Autowired
	private JsPlatOfflineCoinTaskRecordService jsPlatOfflineCoinTaskRecordService;
	
	@ModelAttribute
	public JsPlatOfflineCoinTaskRecord get(@RequestParam(required=false) String id) {
		JsPlatOfflineCoinTaskRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsPlatOfflineCoinTaskRecordService.get(id);
		}
		if (entity == null){
			entity = new JsPlatOfflineCoinTaskRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:jsPlatOfflineCoinTaskRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsPlatOfflineCoinTaskRecord jsPlatOfflineCoinTaskRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JsPlatOfflineCoinTaskRecord> page = jsPlatOfflineCoinTaskRecordService.findPage(new Page<JsPlatOfflineCoinTaskRecord>(request, response), jsPlatOfflineCoinTaskRecord); 
		model.addAttribute("page", page);
		return "modules/plat/jsPlatOfflineCoinTaskRecordList";
	}

	@RequiresPermissions("plat:jsPlatOfflineCoinTaskRecord:view")
	@RequestMapping(value = "form")
	public String form(JsPlatOfflineCoinTaskRecord jsPlatOfflineCoinTaskRecord, Model model) {
		model.addAttribute("jsPlatOfflineCoinTaskRecord", jsPlatOfflineCoinTaskRecord);
		return "modules/plat/jsPlatOfflineCoinTaskRecordForm";
	}

	@RequiresPermissions("plat:jsPlatOfflineCoinTaskRecord:edit")
	@RequestMapping(value = "save")
	public String save(JsPlatOfflineCoinTaskRecord jsPlatOfflineCoinTaskRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsPlatOfflineCoinTaskRecord)){
			return form(jsPlatOfflineCoinTaskRecord, model);
		}
		jsPlatOfflineCoinTaskRecordService.save(jsPlatOfflineCoinTaskRecord);
		addMessage(redirectAttributes, "保存C2C币种价格更新记录成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatOfflineCoinTaskRecord/?repage";
	}
	
	@RequiresPermissions("plat:jsPlatOfflineCoinTaskRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(JsPlatOfflineCoinTaskRecord jsPlatOfflineCoinTaskRecord, RedirectAttributes redirectAttributes) {
		jsPlatOfflineCoinTaskRecordService.delete(jsPlatOfflineCoinTaskRecord);
		addMessage(redirectAttributes, "删除C2C币种价格更新记录成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatOfflineCoinTaskRecord/?repage";
	}

}