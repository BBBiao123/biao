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
import com.thinkgem.jeesite.modules.market.entity.MkLuckyDrawPlayer;
import com.thinkgem.jeesite.modules.market.service.MkLuckyDrawPlayerService;

/**
 * 抽奖活动参与者Controller
 * @author zzj
 * @version 2018-11-01
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkLuckyDrawPlayer")
public class MkLuckyDrawPlayerController extends BaseController {

	@Autowired
	private MkLuckyDrawPlayerService mkLuckyDrawPlayerService;
	
	@ModelAttribute
	public MkLuckyDrawPlayer get(@RequestParam(required=false) String id) {
		MkLuckyDrawPlayer entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkLuckyDrawPlayerService.get(id);
		}
		if (entity == null){
			entity = new MkLuckyDrawPlayer();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkLuckyDrawPlayer:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkLuckyDrawPlayer mkLuckyDrawPlayer, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkLuckyDrawPlayer> page = mkLuckyDrawPlayerService.findPage(new Page<MkLuckyDrawPlayer>(request, response), mkLuckyDrawPlayer); 
		model.addAttribute("page", page);
		return "modules/market/mkLuckyDrawPlayerList";
	}

	@RequiresPermissions("market:mkLuckyDrawPlayer:view")
	@RequestMapping(value = "form")
	public String form(MkLuckyDrawPlayer mkLuckyDrawPlayer, Model model) {
		model.addAttribute("mkLuckyDrawPlayer", mkLuckyDrawPlayer);
		return "modules/market/mkLuckyDrawPlayerForm";
	}

	@RequiresPermissions("market:mkLuckyDrawPlayer:edit")
	@RequestMapping(value = "save")
	public String save(MkLuckyDrawPlayer mkLuckyDrawPlayer, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkLuckyDrawPlayer)){
			return form(mkLuckyDrawPlayer, model);
		}
		mkLuckyDrawPlayerService.save(mkLuckyDrawPlayer);
		addMessage(redirectAttributes, "保存抽奖活动参与者成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkLuckyDrawPlayer/?repage";
	}
	
	@RequiresPermissions("market:mkLuckyDrawPlayer:edit")
	@RequestMapping(value = "delete")
	public String delete(MkLuckyDrawPlayer mkLuckyDrawPlayer, RedirectAttributes redirectAttributes) {
		mkLuckyDrawPlayerService.delete(mkLuckyDrawPlayer);
		addMessage(redirectAttributes, "删除抽奖活动参与者成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkLuckyDrawPlayer/?repage";
	}

}