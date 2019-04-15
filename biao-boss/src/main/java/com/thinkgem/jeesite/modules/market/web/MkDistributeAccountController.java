/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.PlatUserService;
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
import com.thinkgem.jeesite.modules.market.entity.MkDistributeAccount;
import com.thinkgem.jeesite.modules.market.service.MkDistributeAccountService;

import java.util.List;

/**
 * 营销账户Controller
 * @author zhangzijun
 * @version 2018-07-05
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkDistributeAccount")
public class MkDistributeAccountController extends BaseController {

	@Autowired
	private MkDistributeAccountService mkDistributeAccountService;

	@Autowired
	private CoinService coinService;

	@Autowired
	private PlatUserService platUserService;
	
	@ModelAttribute
	public MkDistributeAccount get(@RequestParam(required=false) String id) {
		MkDistributeAccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkDistributeAccountService.get(id);
		}
		if (entity == null){
			entity = new MkDistributeAccount();
		}
		return entity;
	}
	
	@RequiresPermissions("market:account:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkDistributeAccount mkDistributeAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkDistributeAccount> page = mkDistributeAccountService.findPage(new Page<MkDistributeAccount>(request, response), mkDistributeAccount); 
		model.addAttribute("page", page);
		return "modules/market/mkDistributeAccountList";
	}

	@RequiresPermissions("market:account:view")
	@RequestMapping(value = "form")
	public String form(MkDistributeAccount mkDistributeAccount, Model model) {
		model.addAttribute("mkDistributeAccount", mkDistributeAccount);

		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);

		List<PlatUser> platUserList = platUserService.findList(new PlatUser());
		model.addAttribute(platUserList);
		return "modules/market/mkDistributeAccountForm";
	}

	@RequiresPermissions("market:account:edit")
	@RequestMapping(value = "save")
	public String save(MkDistributeAccount mkDistributeAccount, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkDistributeAccount)){
			return form(mkDistributeAccount, model);
		}
		mkDistributeAccountService.save(mkDistributeAccount);
		addMessage(redirectAttributes, "保存营销账户成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDistributeAccount/?repage";
	}
	
	@RequiresPermissions("market:account:edit")
	@RequestMapping(value = "delete")
	public String delete(MkDistributeAccount mkDistributeAccount, RedirectAttributes redirectAttributes) {
		mkDistributeAccountService.delete(mkDistributeAccount);
		addMessage(redirectAttributes, "删除营销账户成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDistributeAccount/?repage";
	}

}