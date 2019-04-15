/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
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
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeAreaMember;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeAreaMemberService;

import java.util.List;

/**
 * 区域合伙人售卖规则Controller
 * @author dongfeng
 * @version 2018-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeAreaMember")
public class Mk2PopularizeAreaMemberController extends BaseController {

	@Autowired
	private Mk2PopularizeAreaMemberService mk2PopularizeAreaMemberService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public Mk2PopularizeAreaMember get(@RequestParam(required=false) String id) {
		Mk2PopularizeAreaMember entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeAreaMemberService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeAreaMember();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mk2PopularizeAreaMember:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeAreaMember mk2PopularizeAreaMember, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeAreaMember> page = mk2PopularizeAreaMemberService.findPage(new Page<Mk2PopularizeAreaMember>(request, response), mk2PopularizeAreaMember); 
		model.addAttribute("page", page);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		request.getSession().setAttribute("searchMk2PopularizeAreaMember", mk2PopularizeAreaMember);
		return "modules/market/mk2PopularizeAreaMemberList";
	}

	@RequiresPermissions("market:mk2PopularizeAreaMember:edit")
	@RequestMapping(value = "batchset")
	public String batchSetAreaMember(Mk2PopularizeAreaMember mk2PopularizeAreaMember, RedirectAttributes redirectAttributes) {
		mk2PopularizeAreaMemberService.batchSetAreaMember(mk2PopularizeAreaMember);
		addMessage(redirectAttributes, "批量设置区域合伙人成功！");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeAreaMember/?repage";
	}

	@RequiresPermissions("market:mk2PopularizeAreaMember:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeAreaMember mk2PopularizeAreaMember, Model model) {
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		model.addAttribute("mk2PopularizeAreaMember", mk2PopularizeAreaMember);
		model.addAttribute("shareholderMemberList", mk2PopularizeAreaMemberService.findShareholder(mk2PopularizeAreaMember));
		return "modules/market/mk2PopularizeAreaMemberForm";
	}

	@RequiresPermissions("market:mk2PopularizeAreaMember:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeAreaMember mk2PopularizeAreaMember, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, mk2PopularizeAreaMember)){
			return form(mk2PopularizeAreaMember, model);
		}
		try {
			mk2PopularizeAreaMemberService.save(mk2PopularizeAreaMember);
			model.addAttribute("sucMessage", "保存区域合伙人售卖成功");
		} catch (ServiceException e) {
			logger.error("保存区域合伙人失败", e);
			model.addAttribute("errorMessage", e.getMessage());
		} catch (Exception e) {
			logger.error("保存区域合伙人失败", e);
			model.addAttribute("errorMessage", "保存区域合伙人售卖失败");
		}
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		model.addAttribute("mk2PopularizeAreaMember", mk2PopularizeAreaMemberService.get(mk2PopularizeAreaMember.getId()));
		model.addAttribute("shareholderMemberList", mk2PopularizeAreaMemberService.findShareholder(mk2PopularizeAreaMember));
		redirectAttributes.addFlashAttribute("mk2PopularizeAreaMember", request.getSession().getAttribute("searchMk2PopularizeAreaMember"));
		return "modules/market/mk2PopularizeAreaMemberForm";
	}
	
	@RequiresPermissions("market:mk2PopularizeAreaMember:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeAreaMember mk2PopularizeAreaMember, RedirectAttributes redirectAttributes) {
		mk2PopularizeAreaMemberService.delete(mk2PopularizeAreaMember);
		addMessage(redirectAttributes, "删除区域合伙人售卖规则成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeAreaMember/?repage";
	}

}