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
import com.thinkgem.jeesite.modules.market.entity.MkPromoteDetail;
import com.thinkgem.jeesite.modules.market.service.MkPromoteDetailService;

/**
 * 会员推广明细Controller
 * @author zhangzijun
 * @version 2018-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkPromoteDetail")
public class MkPromoteDetailController extends BaseController {

	@Autowired
	private MkPromoteDetailService mkPromoteDetailService;
	
	@ModelAttribute
	public MkPromoteDetail get(@RequestParam(required=false) String id) {
		MkPromoteDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkPromoteDetailService.get(id);
		}
		if (entity == null){
			entity = new MkPromoteDetail();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkPromoteDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkPromoteDetail mkPromoteDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkPromoteDetail> page = mkPromoteDetailService.findPage(new Page<MkPromoteDetail>(request, response), mkPromoteDetail); 
		model.addAttribute("page", page);
		return "modules/market/mkPromoteDetailList";
	}

	@RequiresPermissions("market:mkPromoteDetail:view")
	@RequestMapping(value = "form")
	public String form(MkPromoteDetail mkPromoteDetail, Model model) {
		model.addAttribute("mkPromoteDetail", mkPromoteDetail);
		return "modules/market/mkPromoteDetailForm";
	}

	@RequiresPermissions("market:mkPromoteDetail:edit")
	@RequestMapping(value = "save")
	public String save(MkPromoteDetail mkPromoteDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkPromoteDetail)){
			return form(mkPromoteDetail, model);
		}
		mkPromoteDetailService.save(mkPromoteDetail);
		addMessage(redirectAttributes, "保存会员推广明细成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkPromoteDetail/?repage";
	}
	
	@RequiresPermissions("market:mkPromoteDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(MkPromoteDetail mkPromoteDetail, RedirectAttributes redirectAttributes) {
		mkPromoteDetailService.delete(mkPromoteDetail);
		addMessage(redirectAttributes, "删除会员推广明细成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkPromoteDetail/?repage";
	}

}