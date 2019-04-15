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
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeNodalMember;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeNodalMemberService;

import java.util.List;

/**
 * 节点人Controller
 * @author dongfeng
 * @version 2018-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeNodalMember")
public class Mk2PopularizeNodalMemberController extends BaseController {

	@Autowired
	private Mk2PopularizeNodalMemberService mk2PopularizeNodalMemberService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public Mk2PopularizeNodalMember get(@RequestParam(required=false) String id) {
		Mk2PopularizeNodalMember entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeNodalMemberService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeNodalMember();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mk2PopularizeNodalMember:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeNodalMember mk2PopularizeNodalMember, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeNodalMember> page = mk2PopularizeNodalMemberService.findPage(new Page<Mk2PopularizeNodalMember>(request, response), mk2PopularizeNodalMember);
		model.addAttribute("page", page);
		return "modules/market/mk2PopularizeNodalMemberList";
	}

	@RequiresPermissions("market:mk2PopularizeNodalMember:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeNodalMember mk2PopularizeNodalMember, Model model) {
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		model.addAttribute("mk2PopularizeNodalMember", mk2PopularizeNodalMember);
		model.addAttribute("mk2NodalLockReleaseInfoList", mk2PopularizeNodalMemberService.findLockReleaseInfo(mk2PopularizeNodalMember.getId()));
		if (StringUtils.isNotBlank(mk2PopularizeNodalMember.getReturnMsg())) {
			model.addAttribute("sucMessage", mk2PopularizeNodalMember.getReturnMsg());
		}
		return "modules/market/mk2PopularizeNodalMemberForm";
	}

	@RequiresPermissions("market:mk2PopularizeNodalMember:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeNodalMember mk2PopularizeNodalMember, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mk2PopularizeNodalMember)){
			return form(mk2PopularizeNodalMember, model);
		}
		try {
			mk2PopularizeNodalMemberService.save(mk2PopularizeNodalMember);
			model.addAttribute("sucMessage", "保存节点人成功");
		} catch (ServiceException e) {
			logger.error("保存节点人失败", e);
			model.addAttribute("errorMessage", e.getMessage());
		} catch (Exception e) {
			logger.error("保存节点人失败", e);
			model.addAttribute("errorMessage", "保存节点人失败");
		}
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		model.addAttribute("mk2PopularizeNodalMember", mk2PopularizeNodalMember);
//		model.addAttribute("mk2NodalLockReleaseInfoList", mk2PopularizeNodalMemberService.findLockReleaseInfo());
		return "modules/market/mk2PopularizeNodalMemberForm";
	}

	@RequiresPermissions("market:mk2PopularizeNodalMember:edit")
	@RequestMapping(value = "saveLockRelease")
	public void saveLockRelease(Mk2PopularizeNodalMember mk2PopularizeNodalMember, HttpServletResponse response) {

		StringBuilder resultMsg = new StringBuilder();
		try {
			mk2PopularizeNodalMemberService.saveLockReleaseInfo(mk2PopularizeNodalMember);
			if ("1".equals(mk2PopularizeNodalMember.getLockStatus())) {
				resultMsg.append("{").append("\"success\":").append("1,").append("\"msg\":").append("\"节点人资产冻结成功!\"").append("}");
			} else {
				resultMsg.append("{").append("\"success\":").append("1,").append("\"msg\":").append("\"保存节点人锁定和释放规则成功!\"").append("}");
			}
		} catch (ServiceException e) {
			logger.error("保存节点人锁定和释放规则失败", e);
			resultMsg.append("{").append("\"success\":").append("0,").append("\"msg\":").append("\"").append(e.getMessage()).append("\"").append("}");
		} catch (Exception e) {
			logger.error("保存节点人锁定和释放规则失败", e);
			resultMsg.append("{").append("\"success\":").append("0,").append("\"msg\":").append("\"保存节点人锁定和释放规则失败!\"").append("}");
		}
		renderString(response, resultMsg.toString());
	}

	@RequiresPermissions("market:mk2PopularizeNodalMember:edit")
	@RequestMapping(value = "deleteLockRelease")
	public void deleteLockRelease(Mk2PopularizeNodalMember mk2PopularizeNodalMember, HttpServletResponse response) {

		StringBuilder resultMsg = new StringBuilder();
		try {
			mk2PopularizeNodalMemberService.deleteLockRelease(mk2PopularizeNodalMember);
			resultMsg.append("{").append("\"success\":").append("1,").append("\"msg\":").append("\"节点人冻结释放规则删除成功!\"").append("}");
		} catch (ServiceException e) {
			logger.error("节点人冻结释放规则删除失败", e);
			resultMsg.append("{").append("\"success\":").append("0,").append("\"msg\":").append("\"").append(e.getMessage()).append("\"").append("}");
		} catch (Exception e) {
			logger.error("节点人冻结释放规则删除失败", e);
			resultMsg.append("{").append("\"success\":").append("0,").append("\"msg\":").append("\"节点人冻结释放规则删除失败!\"").append("}");
		}
		renderString(response, resultMsg.toString());
	}

//	@RequiresPermissions("market:mk2PopularizeNodalMember:edit")
//	@RequestMapping(value = "saveLockRelease")
//	public String saveLockRelease(Mk2PopularizeNodalMember mk2PopularizeNodalMember, Model model, RedirectAttributes redirectAttributes) {
//		if (!beanValidator(model, mk2PopularizeNodalMember)){
//			return form(mk2PopularizeNodalMember, model);
//		}
//		try {
//			mk2PopularizeNodalMemberService.saveLockReleaseInfo(mk2PopularizeNodalMember);
//			model.addAttribute("sucMessage", "保存节点人锁定和释放规则成功");
//		} catch (ServiceException e) {
//			logger.error("保存节点人锁定和释放规则失败", e);
//			model.addAttribute("errorMessage", e.getMessage());
//		} catch (Exception e) {
//			logger.error("保存节点人锁定和释放规则失败", e);
//			model.addAttribute("errorMessage", "保存节点人锁定和释放规则失败");
//		}
//		List<Coin> coinList = coinService.findList(new Coin());
//		model.addAttribute(coinList);
//		model.addAttribute("mk2PopularizeNodalMember", mk2PopularizeNodalMemberService.get(mk2PopularizeNodalMember.getParentId()));
//		model.addAttribute("mk2NodalLockReleaseInfoList", mk2PopularizeNodalMemberService.findLockReleaseInfo(mk2PopularizeNodalMember.getParentId()));
//		return "modules/market/mk2PopularizeNodalMemberForm";
//	}

	@RequiresPermissions("market:mk2PopularizeNodalMember:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeNodalMember mk2PopularizeNodalMember, RedirectAttributes redirectAttributes) {
		try {
			mk2PopularizeNodalMemberService.delete(mk2PopularizeNodalMember);
			addMessage(redirectAttributes, "删除节点人成功");
		} catch (ServiceException e) {
			logger.error("删除节点人失败", e);
			addMessage(redirectAttributes, e.getMessage());
		} catch (Exception e) {
			logger.error("删除节点人失败", e);
			addMessage(redirectAttributes, "数据有误，删除节点人失败！");
		}
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeNodalMember/?repage";
	}

}