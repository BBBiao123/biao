/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.utils.PlatApiService;
import com.thinkgem.jeesite.common.utils.RsaUtils;
import com.thinkgem.jeesite.modules.market.entity.MkAutoTradeUser;
import com.thinkgem.jeesite.modules.market.entity.MkSysUserExPair;
import com.thinkgem.jeesite.modules.market.service.MkAutoTradeMonitorService;
import com.thinkgem.jeesite.modules.market.service.MkAutoTradeUserService;
import com.thinkgem.jeesite.modules.market.service.MkSysUserExPairService;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.ExPair;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.ExPairService;
import com.thinkgem.jeesite.modules.plat.service.PlatUserService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
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
import com.thinkgem.jeesite.modules.market.entity.MkAutoTradeSetting;
import com.thinkgem.jeesite.modules.market.service.MkAutoTradeSettingService;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动交易Controller
 * @author zhangzijun
 * @version 2018-08-07
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkAutoTradeSetting")
public class MkAutoTradeSettingController extends BaseController {

	@Autowired
	private MkAutoTradeSettingService mkAutoTradeSettingService;

	@Autowired
	private MkAutoTradeMonitorService mkAutoTradeMonitorService;

	@Autowired
	private CoinService coinService;

	@Autowired
	private PlatApiService platApiService;

	@Autowired
	private ExPairService exPairService;

	@Autowired
	private MkSysUserExPairService mkSysUserExPairService;

	@Autowired
	private MkAutoTradeUserService mkAutoTradeUserService;

	@ModelAttribute
	public MkAutoTradeSetting get(@RequestParam(required=false) String id) {
		MkAutoTradeSetting entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkAutoTradeSettingService.get(id);
		}
		if (entity == null){
			entity = new MkAutoTradeSetting();
		}
		return entity;
	}

	@RequiresPermissions("market:mkAutoTradeSetting:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkAutoTradeSetting mkAutoTradeSetting, HttpServletRequest request, HttpServletResponse response, Model model) {

		MkSysUserExPair mkSysUserExPair = new MkSysUserExPair();
		mkSysUserExPair.setSysUserId(UserUtils.getUser().getId());
		List<MkSysUserExPair> mkSysUserExPairs = mkSysUserExPairService.findAllList(mkSysUserExPair);
		if(CollectionUtils.isNotEmpty(mkSysUserExPairs)){
			mkAutoTradeSetting.setCreateBy(UserUtils.getUser());
		}

		Page<MkAutoTradeSetting> page = mkAutoTradeSettingService.findPage(new Page<MkAutoTradeSetting>(request, response), mkAutoTradeSetting);
		model.addAttribute("page", page);
		return "modules/market/mkAutoTradeSettingList";
	}

	@RequiresPermissions("market:mkAutoTradeSetting:view")
	@RequestMapping(value = "form")
	public String form(MkAutoTradeSetting mkAutoTradeSetting, Model model) {

		List<ExPair> exPairList = new ArrayList<>();
		MkSysUserExPair mkSysUserExPair = new MkSysUserExPair();
		mkSysUserExPair.setSysUserId(UserUtils.getUser().getId());
		List<MkSysUserExPair> mkSysUserExPairs = mkSysUserExPairService.findAllList(mkSysUserExPair);
		if(CollectionUtils.isNotEmpty(mkSysUserExPairs)){
			for (MkSysUserExPair sysUserExPair : mkSysUserExPairs) {
				ExPair exPair = new ExPair();
				exPair.setId(sysUserExPair.getExPairId());
				exPair.setPairOne(sysUserExPair.getCoinMainSymbol());
				exPair.setPairOther(sysUserExPair.getCoinOtherSymbol());
				exPairList.add(exPair);
			}
		}else{
			ExPair exPair = new ExPair();
			exPair.setStatus("1");
			exPairList = exPairService.findList(exPair);
		}

		model.addAttribute("mkAutoTradeSetting", mkAutoTradeSetting);
		model.addAttribute(exPairList);
		return "modules/market/mkAutoTradeSettingForm";
	}

	@RequiresPermissions("market:mkAutoTradeSetting:edit")
	@RequestMapping(value = "save")
	public String save(MkAutoTradeSetting mkAutoTradeSetting, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkAutoTradeSetting)){
			return form(mkAutoTradeSetting, model);
		}

		if(!mkAutoTradeSetting.getIsNewRecord() && mkAutoTradeMonitorService.findActiveBySetting(mkAutoTradeSetting.getId()) != null){
			addMessage(redirectAttributes, "该规则正在执行中，不能修改！");
		}else{
			if(mkAutoTradeSetting.getIsNewRecord()){
				mkAutoTradeSetting.setCreateBy(UserUtils.getUser());
			}
			mkAutoTradeSettingService.save(mkAutoTradeSetting);
			addMessage(redirectAttributes, "保存自动交易成功");
		}

		return "redirect:"+Global.getAdminPath()+"/market/mkAutoTradeSetting/?repage";
	}
	
	@RequiresPermissions("market:mkAutoTradeSetting:edit")
	@RequestMapping(value = "delete")
	public String delete(MkAutoTradeSetting mkAutoTradeSetting, RedirectAttributes redirectAttributes) {

		if(mkAutoTradeMonitorService.findActiveBySetting(mkAutoTradeSetting.getId()) != null){
			addMessage(redirectAttributes, "该规则正在执行中，不能删除！");
		}else{
			mkAutoTradeSettingService.delete(mkAutoTradeSetting);
			addMessage(redirectAttributes, "删除自动交易成功");
		}

		return "redirect:"+Global.getAdminPath()+"/market/mkAutoTradeSetting/?repage";
	}

	@RequiresPermissions("market:mkAutoTradeSetting:edit")
	@RequestMapping(value = "/checkLogin/{user}/{pass}")
	@ResponseBody
	public String checkLogin(@PathVariable("user") String user, @PathVariable("pass") String pass) {
		JSONObject object = new JSONObject();
		object.put("code","9999");
		try{
			pass = pass.replaceAll("@","/");
			String token = platApiService.login(user, pass);
			if(StringUtils.isNotEmpty(token)){
				object.put("code","0000");
			}
		}catch (Exception e){
			logger.error("验证登录失败，" + e);
		}

		return object.toString();
	}

	@RequiresPermissions("market:mkAutoTradeSetting:view")
	@RequestMapping(value = "PlatUserDialog")
	public String listPlatUserDialog(PlatUser platUser, HttpServletRequest request, HttpServletResponse response, Model model) {

		MkSysUserExPair sysUserExPair = new MkSysUserExPair();
		sysUserExPair.setSysUserId(UserUtils.getUser().getId());
		List<MkSysUserExPair> mkSysUserExPairs = mkSysUserExPairService.findAllList(sysUserExPair);

		List<PlatUser> platUsers = null;
		MkSysUserExPair mkSysUserExPair = new MkSysUserExPair();
		if(CollectionUtils.isNotEmpty(mkSysUserExPairs)){
			mkSysUserExPair.setSysUserId(UserUtils.getUser().getId());
			platUsers = mkSysUserExPairService.getPlatUserBySysUser(mkSysUserExPair);
		}else{
			MkAutoTradeUser mkAutoTradeUser = new MkAutoTradeUser();
			platUsers = mkAutoTradeUserService.getPlatUser(mkAutoTradeUser);
		}

		Page<PlatUser> page = new Page<>();
		page.setList(platUsers);
		page.setPageSize(-1);
		page.setCount(platUsers.size());
		model.addAttribute("page", page);
		return "modules/market/AutoTradePlatUserDialog";
	}

}