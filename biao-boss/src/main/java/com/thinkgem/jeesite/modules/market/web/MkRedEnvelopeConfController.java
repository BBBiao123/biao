/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.modules.market.entity.MkRedEnvelopeConf;
import com.thinkgem.jeesite.modules.market.service.MkRedEnvelopeConfService;

import java.util.List;

/**
 * 红包配置Controller
 * @author zijun
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkRedEnvelopeConf")
public class MkRedEnvelopeConfController extends BaseController {

	@Autowired
	private MkRedEnvelopeConfService mkRedEnvelopeConfService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public MkRedEnvelopeConf get(@RequestParam(required=false) String id) {
		MkRedEnvelopeConf entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkRedEnvelopeConfService.get(id);
		}
		if (entity == null){
			entity = new MkRedEnvelopeConf();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkRedEnvelopeConf:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkRedEnvelopeConf mkRedEnvelopeConf, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkRedEnvelopeConf> page = mkRedEnvelopeConfService.findPage(new Page<MkRedEnvelopeConf>(request, response), mkRedEnvelopeConf); 
		model.addAttribute("page", page);
		return "modules/market/mkRedEnvelopeConfList";
	}

	@RequiresPermissions("market:mkRedEnvelopeConf:view")
	@RequestMapping(value = "form")
	public String form(MkRedEnvelopeConf mkRedEnvelopeConf, Model model) {
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		model.addAttribute("mkRedEnvelopeConf", mkRedEnvelopeConf);
		return "modules/market/mkRedEnvelopeConfForm";
	}

	@RequiresPermissions("market:mkRedEnvelopeConf:edit")
	@RequestMapping(value = "save")
	public String save(MkRedEnvelopeConf mkRedEnvelopeConf, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkRedEnvelopeConf)){
			return form(mkRedEnvelopeConf, model);
		}
		mkRedEnvelopeConfService.save(mkRedEnvelopeConf);
		addMessage(redirectAttributes, "保存红包配置成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRedEnvelopeConf/?repage";
	}
	
	@RequiresPermissions("market:mkRedEnvelopeConf:edit")
	@RequestMapping(value = "delete")
	public String delete(MkRedEnvelopeConf mkRedEnvelopeConf, RedirectAttributes redirectAttributes) {
		mkRedEnvelopeConfService.delete(mkRedEnvelopeConf);
		addMessage(redirectAttributes, "删除红包配置成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRedEnvelopeConf/?repage";
	}

}