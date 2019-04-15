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
import com.thinkgem.jeesite.modules.market.entity.MkCoinDestroyRecord;
import com.thinkgem.jeesite.modules.market.service.MkCoinDestroyRecordService;

import java.util.List;

/**
 * 币种销毁记录Controller
 * @author zzj
 * @version 2018-10-09
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkCoinDestroyRecord")
public class MkCoinDestroyRecordController extends BaseController {

	@Autowired
	private MkCoinDestroyRecordService mkCoinDestroyRecordService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public MkCoinDestroyRecord get(@RequestParam(required=false) String id) {
		MkCoinDestroyRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkCoinDestroyRecordService.get(id);
		}
		if (entity == null){
			entity = new MkCoinDestroyRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkCoinDestroyRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkCoinDestroyRecord mkCoinDestroyRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkCoinDestroyRecord> page = mkCoinDestroyRecordService.findPage(new Page<MkCoinDestroyRecord>(request, response), mkCoinDestroyRecord); 
		model.addAttribute("page", page);
		return "modules/market/mkCoinDestroyRecordList";
	}

	@RequiresPermissions("market:mkCoinDestroyRecord:view")
	@RequestMapping(value = "form")
	public String form(MkCoinDestroyRecord mkCoinDestroyRecord, Model model) {
		model.addAttribute("mkCoinDestroyRecord", mkCoinDestroyRecord);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/market/mkCoinDestroyRecordForm";
	}

	@RequiresPermissions("market:mkCoinDestroyRecord:edit")
	@RequestMapping(value = "save")
	public String save(MkCoinDestroyRecord mkCoinDestroyRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkCoinDestroyRecord)){
			return form(mkCoinDestroyRecord, model);
		}
		mkCoinDestroyRecordService.save(mkCoinDestroyRecord);
		addMessage(redirectAttributes, "保存币种销毁记录成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkCoinDestroyRecord/?repage";
	}
	
	@RequiresPermissions("market:mkCoinDestroyRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(MkCoinDestroyRecord mkCoinDestroyRecord, RedirectAttributes redirectAttributes) {
		mkCoinDestroyRecordService.delete(mkCoinDestroyRecord);
		addMessage(redirectAttributes, "删除币种销毁记录成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkCoinDestroyRecord/?repage";
	}

}