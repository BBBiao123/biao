/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.thinkgem.jeesite.modules.plat.entity.OfflineAppeal;
import com.thinkgem.jeesite.modules.plat.service.OfflineAppealService;

import java.util.logging.Logger;

/**
 * 申诉Controller
 * @author dongfeng
 * @version 2018-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/offlineAppeal")
public class OfflineAppealController extends BaseController {

	@Autowired
	private OfflineAppealService offlineAppealService;

	@Value("${image.url}")
	private String imageUrl;

	@ModelAttribute
	public OfflineAppeal get(@RequestParam(required=false) String id) {
		OfflineAppeal entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = offlineAppealService.get(id);
		}
		if (entity == null){
			entity = new OfflineAppeal();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:offlineAppeal:view")
	@RequestMapping(value = {"list", ""})
	public String list(OfflineAppeal offlineAppeal, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OfflineAppeal> page = offlineAppealService.findPage(new Page<OfflineAppeal>(request, response), offlineAppeal); 
		model.addAttribute("page", page);
		return "modules/plat/offlineAppealList";
	}

	@RequiresPermissions("plat:offlineAppeal:view")
	@RequestMapping(value = "form")
	public String form(OfflineAppeal offlineAppeal, Model model) {
		model.addAttribute("offlineAppeal", offlineAppeal);
		model.addAttribute("imageUrl", imageUrl);
		return "modules/plat/offlineAppealForm";
	}

	@RequiresPermissions("plat:offlineAppeal:examine")
	@RequestMapping(value = "examineSeller")
	public String examineSeller(String appealId, String examineResultReason, RedirectAttributes redirectAttributes, Model model) {
		String userId = UserUtils.getUser().getLoginName();
		OfflineAppeal offlineAppeal = null;
		try {
			offlineAppealService.examineSeller(appealId, userId, examineResultReason);
			offlineAppeal = offlineAppealService.get(appealId);
			model.addAttribute("offlineAppeal", offlineAppeal);
			model.addAttribute("imageUrl", imageUrl);
			model.addAttribute("message", "判给卖家成功");
		} catch (Exception e) {
			logger.error("判给卖家报错", e);
			offlineAppeal = offlineAppealService.get(appealId);
			model.addAttribute("offlineAppeal", offlineAppeal);
			model.addAttribute("imageUrl", imageUrl);
			model.addAttribute("message", e.getMessage());
		}
		return "modules/plat/offlineAppealForm";
	}

	@RequiresPermissions("plat:offlineAppeal:examine")
	@RequestMapping(value = "examineBuyer")
	public String examineBuyer(String appealId, String examineResultReason, RedirectAttributes redirectAttributes, Model model) {
		String userId = UserUtils.getUser().getLoginName();
		OfflineAppeal offlineAppeal = null;
		try {
			offlineAppealService.examineBuyer(appealId, userId, examineResultReason);
			offlineAppeal = offlineAppealService.get(appealId);
			model.addAttribute("offlineAppeal", offlineAppeal);
			model.addAttribute("imageUrl", imageUrl);
			model.addAttribute("message", "判给买家成功");
		} catch (Exception e) {
			logger.error("判给买家报错", e);
			offlineAppeal = offlineAppealService.get(appealId);
			model.addAttribute("offlineAppeal", offlineAppeal);
			model.addAttribute("imageUrl", imageUrl);
			model.addAttribute("message", e.getMessage());
		}
		return "modules/plat/offlineAppealForm";
	}
}