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
import com.thinkgem.jeesite.modules.market.entity.MkLuckyDrawRecord;
import com.thinkgem.jeesite.modules.market.service.MkLuckyDrawRecordService;

/**
 * 抽奖活动开奖记录Controller
 * @author zzj
 * @version 2018-11-01
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkLuckyDrawRecord")
public class MkLuckyDrawRecordController extends BaseController {

	@Autowired
	private MkLuckyDrawRecordService mkLuckyDrawRecordService;
	
	@ModelAttribute
	public MkLuckyDrawRecord get(@RequestParam(required=false) String id) {
		MkLuckyDrawRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkLuckyDrawRecordService.get(id);
		}
		if (entity == null){
			entity = new MkLuckyDrawRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkLuckyDrawRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkLuckyDrawRecord mkLuckyDrawRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkLuckyDrawRecord> page = mkLuckyDrawRecordService.findPage(new Page<MkLuckyDrawRecord>(request, response), mkLuckyDrawRecord); 
		model.addAttribute("page", page);
		return "modules/market/mkLuckyDrawRecordList";
	}

	@RequiresPermissions("market:mkLuckyDrawRecord:view")
	@RequestMapping(value = "form")
	public String form(MkLuckyDrawRecord mkLuckyDrawRecord, Model model) {
		model.addAttribute("mkLuckyDrawRecord", mkLuckyDrawRecord);
		return "modules/market/mkLuckyDrawRecordForm";
	}

	@RequiresPermissions("market:mkLuckyDrawRecord:edit")
	@RequestMapping(value = "save")
	public String save(MkLuckyDrawRecord mkLuckyDrawRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkLuckyDrawRecord)){
			return form(mkLuckyDrawRecord, model);
		}
		mkLuckyDrawRecordService.save(mkLuckyDrawRecord);
		addMessage(redirectAttributes, "保存抽奖活动开奖记录成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkLuckyDrawRecord/?repage";
	}
	
	@RequiresPermissions("market:mkLuckyDrawRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(MkLuckyDrawRecord mkLuckyDrawRecord, RedirectAttributes redirectAttributes) {
		mkLuckyDrawRecordService.delete(mkLuckyDrawRecord);
		addMessage(redirectAttributes, "删除抽奖活动开奖记录成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkLuckyDrawRecord/?repage";
	}

}