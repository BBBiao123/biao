/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.market.entity.MkSysUserExPair;
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

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.market.entity.MkAutoTradeOrder;
import com.thinkgem.jeesite.modules.market.service.MkAutoTradeOrderService;

import java.util.List;

/**
 * 自动交易挂单Controller
 * @author zhangzijun
 * @version 2018-08-13
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkAutoTradeOrder")
public class MkAutoTradeOrderController extends BaseController {

	@Autowired
	private MkAutoTradeOrderService mkAutoTradeOrderService;

	@Autowired
	private MkSysUserExPairService mkSysUserExPairService;
	
	@ModelAttribute
	public MkAutoTradeOrder get(@RequestParam(required=false) String id) {
		MkAutoTradeOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkAutoTradeOrderService.get(id);
		}
		if (entity == null){
			entity = new MkAutoTradeOrder();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkAutoTradeOrder:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkAutoTradeOrder mkAutoTradeOrder, HttpServletRequest request, HttpServletResponse response, Model model) {

		MkSysUserExPair mkSysUserExPair = new MkSysUserExPair();
		mkSysUserExPair.setSysUserId(UserUtils.getUser().getId());
		List<MkSysUserExPair> mkSysUserExPairs = mkSysUserExPairService.findAllList(mkSysUserExPair);
		if(CollectionUtils.isNotEmpty(mkSysUserExPairs)){
			mkAutoTradeOrder.setCreateBy(UserUtils.getUser());
		}

		Page<MkAutoTradeOrder> page = mkAutoTradeOrderService.findPage(new Page<MkAutoTradeOrder>(request, response), mkAutoTradeOrder); 
		model.addAttribute("page", page);
		return "modules/market/mkAutoTradeOrderList";
	}

	@RequiresPermissions("market:mkAutoTradeOrder:view")
	@RequestMapping(value = "form")
	public String form(MkAutoTradeOrder mkAutoTradeOrder, Model model) {
		model.addAttribute("mkAutoTradeOrder", mkAutoTradeOrder);
		return "modules/market/mkAutoTradeOrderForm";
	}

	@RequiresPermissions("market:mkAutoTradeOrder:edit")
	@RequestMapping(value = "save")
	public String save(MkAutoTradeOrder mkAutoTradeOrder, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkAutoTradeOrder)){
			return form(mkAutoTradeOrder, model);
		}
		mkAutoTradeOrderService.save(mkAutoTradeOrder);
		addMessage(redirectAttributes, "保存自动交易挂单成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkAutoTradeOrder/?repage";
	}
	
	@RequiresPermissions("market:mkAutoTradeOrder:edit")
	@RequestMapping(value = "delete")
	public String delete(MkAutoTradeOrder mkAutoTradeOrder, RedirectAttributes redirectAttributes) {
		mkAutoTradeOrderService.delete(mkAutoTradeOrder);
		addMessage(redirectAttributes, "删除自动交易挂单成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkAutoTradeOrder/?repage";
	}

}