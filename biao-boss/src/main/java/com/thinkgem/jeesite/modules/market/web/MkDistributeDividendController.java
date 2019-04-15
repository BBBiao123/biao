/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.market.entity.MkDistributeDividendDetail;
import com.thinkgem.jeesite.modules.market.service.MkDistributeDividendDetailService;
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
import com.thinkgem.jeesite.modules.market.entity.MkDistributeDividend;
import com.thinkgem.jeesite.modules.market.service.MkDistributeDividendService;

import java.util.List;

/**
 * 分红规则Controller
 * @author zhangzijun
 * @version 2018-07-05
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkDistributeDividend")
public class MkDistributeDividendController extends BaseController {

	@Autowired
	private MkDistributeDividendService mkDistributeDividendService;

	@Autowired
	private MkDistributeDividendDetailService mkDistributeDividendDetailService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public MkDistributeDividend get(@RequestParam(required=false) String id) {
		MkDistributeDividend entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkDistributeDividendService.get(id);
		}
		if (entity == null){
			entity = new MkDistributeDividend();
		}
		return entity;
	}
	
	@RequiresPermissions("market:dividend:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkDistributeDividend mkDistributeDividend, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkDistributeDividend> page = mkDistributeDividendService.findPage(new Page<MkDistributeDividend>(request, response), mkDistributeDividend);
		List<MkDistributeDividendDetail> mkDistributeDividendList = mkDistributeDividendDetailService.findList(null);
		model.addAttribute("page", page);
		model.addAttribute("details", mkDistributeDividendList);
		return "modules/market/mkDistributeDividendList";
	}

	@RequiresPermissions("market:dividend:view")
	@RequestMapping(value = "form")
	public String form(MkDistributeDividend mkDistributeDividend, Model model) {
		model.addAttribute("mkDistributeDividend", mkDistributeDividend);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/market/mkDistributeDividendForm";
	}

	@RequiresPermissions("market:dividend:edit")
	@RequestMapping(value = "save")
	public String save(MkDistributeDividend mkDistributeDividend, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkDistributeDividend)){
			return form(mkDistributeDividend, model);
		}
		mkDistributeDividendService.save(mkDistributeDividend);
		addMessage(redirectAttributes, "保存分红规则成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDistributeDividend/?repage";
	}

	@RequiresPermissions("market:dividend:edit")
	@RequestMapping(value = "saveDetails")
	@ResponseBody
	public String saveDetails(@RequestBody List<MkDistributeDividendDetail> mkDistributeDividendDetails) {
        JSONObject object = new JSONObject();
        String checkResult = mkDistributeDividendDetailService.checkDetails(mkDistributeDividendDetails);

	    if(StringUtils.isNotEmpty(checkResult)){
            object.put("code","9999");
            object.put("message",checkResult);
            return object.toString();
        }

		mkDistributeDividendDetailService.saveDetails(mkDistributeDividendDetails);

		object.put("code","0000");
		object.put("message","保存成功！");
		return object.toString();
	}

	@RequiresPermissions("market:dividend:edit")
	@RequestMapping(value = "deleteDetail")
	@ResponseBody
	public String deleteDetail(String id) {

		if(StringUtils.isNotEmpty(id)){
			mkDistributeDividendDetailService.deleteById(id);
		}

		JSONObject object = new JSONObject();
		object.put("code","0000");
		return object.toString();
	}

	@RequiresPermissions("market:dividend:edit")
	@RequestMapping(value = "delete")
	public String delete(MkDistributeDividend mkDistributeDividend, RedirectAttributes redirectAttributes) {
		mkDistributeDividendService.delete(mkDistributeDividend);
		addMessage(redirectAttributes, "删除分红规则成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDistributeDividend/?repage";
	}

}