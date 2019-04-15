/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.market.entity.MkPromoteDetail;
import com.thinkgem.jeesite.modules.market.service.MkPromoteDetailService;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.market.entity.MkDistributePromote;
import com.thinkgem.jeesite.modules.market.service.MkDistributePromoteService;

import java.util.List;

/**
 * 会员推广Controller
 * @author zhangzijun
 * @version 2018-07-05
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkDistributePromote")
public class MkDistributePromoteController extends BaseController {

	@Autowired
	private MkDistributePromoteService mkDistributePromoteService;

	@Autowired
	private MkPromoteDetailService mkPromoteDetailService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public MkDistributePromote get(@RequestParam(required=false) String id) {
		MkDistributePromote entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkDistributePromoteService.get(id);
		}
		if (entity == null){
			entity = new MkDistributePromote();
		}
		return entity;
	}
	
	@RequiresPermissions("market:promote:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkDistributePromote mkDistributePromote, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkDistributePromote> page = mkDistributePromoteService.findPage(new Page<MkDistributePromote>(request, response), mkDistributePromote);
		List<MkPromoteDetail> mkPromoteDetailList = mkPromoteDetailService.findList(null);
		model.addAttribute("page", page);
		model.addAttribute("details", mkPromoteDetailList);
		return "modules/market/mkDistributePromoteList";
	}

	@RequiresPermissions("market:promote:view")
	@RequestMapping(value = "form")
	public String form(MkDistributePromote mkDistributePromote, Model model) {
		model.addAttribute("mkDistributePromote", mkDistributePromote);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/market/mkDistributePromoteForm";
	}

	@RequiresPermissions("market:promote:edit")
	@RequestMapping(value = "save")
	public String save(MkDistributePromote mkDistributePromote, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkDistributePromote)){
			return form(mkDistributePromote, model);
		}
		mkDistributePromoteService.save(mkDistributePromote);
		addMessage(redirectAttributes, "保存会员推广成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDistributePromote/?repage";
	}

	@RequiresPermissions("market:promote:edit")
	@RequestMapping(value = "saveDetails")
	@ResponseBody
	public String saveDetails(@RequestBody List<MkPromoteDetail> mkPromoteDetails) {

		JSONObject object = new JSONObject();
		String checkResult = mkPromoteDetailService.checkDetails(mkPromoteDetails);

		if(StringUtils.isNotEmpty(checkResult)){
			object.put("code","8888");
			object.put("message",checkResult);
			return object.toString();
		}
		mkPromoteDetailService.saveDetails(mkPromoteDetails);
		object.put("code","0000");
		object.put("message","保存成功！");
		return object.toString();
	}

	@RequiresPermissions("market:promote:edit")
	@RequestMapping(value = "deleteDetail")
	@ResponseBody
	public String deleteDetail(String id) {

		if(StringUtils.isNotEmpty(id)){
			mkPromoteDetailService.deleteById(id);
		}

		JSONObject object = new JSONObject();
		object.put("code","0000");
		return object.toString();
	}

	@RequiresPermissions("market:promote:edit")
	@RequestMapping(value = "delete")
	public String delete(MkDistributePromote mkDistributePromote, RedirectAttributes redirectAttributes) {
		mkDistributePromoteService.delete(mkDistributePromote);
		addMessage(redirectAttributes, "删除会员推广成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDistributePromote/?repage";
	}

}