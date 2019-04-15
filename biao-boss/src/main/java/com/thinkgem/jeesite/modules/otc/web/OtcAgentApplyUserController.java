/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.web;

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
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentApplyUser;
import com.thinkgem.jeesite.modules.otc.service.OtcAgentApplyUserService;

/**
 * 拨币申请会员列表Controller
 * @author zzj
 * @version 2018-09-19
 */
@Controller
@RequestMapping(value = "${adminPath}/otc/otcAgentApplyUser")
public class OtcAgentApplyUserController extends BaseController {

	@Autowired
	private OtcAgentApplyUserService otcAgentApplyUserService;
	
	@ModelAttribute
	public OtcAgentApplyUser get(@RequestParam(required=false) String id) {
		OtcAgentApplyUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = otcAgentApplyUserService.get(id);
		}
		if (entity == null){
			entity = new OtcAgentApplyUser();
		}
		return entity;
	}
	
	@RequiresPermissions("otc:otcAgentApplyUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(OtcAgentApplyUser otcAgentApplyUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		otcAgentApplyUser.setStatus("1");
		Page<OtcAgentApplyUser> page = otcAgentApplyUserService.findPage(new Page<OtcAgentApplyUser>(request, response), otcAgentApplyUser); 
		model.addAttribute("page", page);
		return "modules/otc/otcAgentApplyUserList";
	}

	@RequiresPermissions("otc:otcAgentApplyUser:view")
	@RequestMapping(value = "form")
	public String form(OtcAgentApplyUser otcAgentApplyUser, Model model) {
		model.addAttribute("otcAgentApplyUser", otcAgentApplyUser);
		return "modules/otc/otcAgentApplyUserForm";
	}

	@RequiresPermissions("otc:otcAgentApplyUser:edit")
	@RequestMapping(value = "save")
	public String save(OtcAgentApplyUser otcAgentApplyUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, otcAgentApplyUser)){
			return form(otcAgentApplyUser, model);
		}
		otcAgentApplyUserService.save(otcAgentApplyUser);
		addMessage(redirectAttributes, "保存拨币申请会员列表成功");
		return "redirect:"+Global.getAdminPath()+"/otc/otcAgentApplyUser/?repage";
	}
	
	@RequiresPermissions("otc:otcAgentApplyUser:edit")
	@RequestMapping(value = "delete")
	public String delete(OtcAgentApplyUser otcAgentApplyUser, RedirectAttributes redirectAttributes) {
		otcAgentApplyUserService.delete(otcAgentApplyUser);
		addMessage(redirectAttributes, "删除拨币申请会员列表成功");
		return "redirect:"+Global.getAdminPath()+"/otc/otcAgentApplyUser/?repage";
	}

}