/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.market.entity.MkAutoTradeMonitor;
import com.thinkgem.jeesite.modules.market.entity.MkSysUserExPair;
import com.thinkgem.jeesite.modules.market.service.MkAutoTradeMonitorService;
import com.thinkgem.jeesite.modules.market.service.MkSysUserExPairService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 自动交易监控Controller
 * @author zhangzijun
 * @version 2018-08-13
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkAutoTradeRecord")
public class MkAutoTradeRecordController extends BaseController {

	@Autowired
	private MkAutoTradeMonitorService mkAutoTradeMonitorService;

	@Autowired
	private MkSysUserExPairService mkSysUserExPairService;
	
	@ModelAttribute
	public MkAutoTradeMonitor get(@RequestParam(required=false) String id) {
		MkAutoTradeMonitor entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkAutoTradeMonitorService.get(id);
		}
		if (entity == null){
			entity = new MkAutoTradeMonitor();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkAutoTradeRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkAutoTradeMonitor mkAutoTradeMonitor, HttpServletRequest request, HttpServletResponse response, Model model) {

		MkSysUserExPair mkSysUserExPair = new MkSysUserExPair();
		mkSysUserExPair.setSysUserId(UserUtils.getUser().getId());
		List<MkSysUserExPair> mkSysUserExPairs = mkSysUserExPairService.findAllList(mkSysUserExPair);
		if(CollectionUtils.isNotEmpty(mkSysUserExPairs)){
			mkAutoTradeMonitor.setCreateBy(UserUtils.getUser());
		}

		mkAutoTradeMonitor.setStatus("4");
		Page<MkAutoTradeMonitor> page = mkAutoTradeMonitorService.findPage(new Page<MkAutoTradeMonitor>(request, response), mkAutoTradeMonitor);
		model.addAttribute("page", page);
		return "modules/market/mkAutoTradeRecordList";
	}

	@RequiresPermissions("market:mkAutoTradeRecord:view")
	@RequestMapping(value = "form")
	public String form(MkAutoTradeMonitor mkAutoTradeMonitor, Model model) {
		model.addAttribute("mkAutoTradeMonitor", mkAutoTradeMonitor);
		return "modules/market/mkAutoTradeMonitorForm";
	}

	@RequiresPermissions("market:mkAutoTradeRecord:edit")
	@RequestMapping(value = "save")
	public String save(MkAutoTradeMonitor mkAutoTradeMonitor, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkAutoTradeMonitor)){
			return form(mkAutoTradeMonitor, model);
		}
		mkAutoTradeMonitorService.save(mkAutoTradeMonitor);
		addMessage(redirectAttributes, "保存自动交易监控成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkAutoTradeMonitor/?repage";
	}
	
	@RequiresPermissions("market:mkAutoTradeRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(MkAutoTradeMonitor mkAutoTradeMonitor, RedirectAttributes redirectAttributes) {
		mkAutoTradeMonitorService.delete(mkAutoTradeMonitor);
		addMessage(redirectAttributes, "删除自动交易监控成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkAutoTradeMonitor/?repage";
	}


}