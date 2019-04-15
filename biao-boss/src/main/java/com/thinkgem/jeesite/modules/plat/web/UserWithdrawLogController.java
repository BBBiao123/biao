/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.UserCoinVolume;
import com.thinkgem.jeesite.modules.plat.entity.UserWithdrawLog;
import com.thinkgem.jeesite.modules.plat.entity.UserWithdrawLogCount;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.UserCoinVolumeService;
import com.thinkgem.jeesite.modules.plat.service.UserWithdrawLogService;

/**
 * 用户提现管理Controller
 * @author dazi
 * @version 2018-05-04
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/userWithdrawLog")
public class UserWithdrawLogController extends BaseController {

	@Autowired
	private UserWithdrawLogService userWithdrawLogService;
	@Autowired
	private UserCoinVolumeService userCoinVolumeService;
	
	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public UserWithdrawLog get(@RequestParam(required=false) String id) {
		UserWithdrawLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userWithdrawLogService.get(id);
		}
		if (entity == null){
			entity = new UserWithdrawLog();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:userWithdrawLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(UserWithdrawLog userWithdrawLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserWithdrawLog> page = userWithdrawLogService.findPage(new Page<UserWithdrawLog>(request, response), userWithdrawLog); 
		model.addAttribute("page", page);
		return "modules/plat/userWithdrawLogList";
	}
	
	@RequiresPermissions("plat:userWithdrawLog:count")
	@RequestMapping("count")
	public String listCount(UserWithdrawLog userWithdrawLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String firstTime = request.getParameter("firstTime");
		List<UserWithdrawLog> pageList = new ArrayList<>();
		if(StringUtils.isBlank(firstTime)) {
			 pageList = userWithdrawLogService.findListCount(userWithdrawLog); 
		}
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		model.addAttribute("pageList", pageList);
		return "modules/plat/userWithdrawLogListCount";
	}

	@RequiresPermissions("plat:userWithdrawLog:view")
	@RequestMapping(value = "form")
	public String form(UserWithdrawLog userWithdrawLog, Model model) {
		model.addAttribute("userWithdrawLog", userWithdrawLog);
		return "modules/plat/userWithdrawLogForm";
	}

	@RequiresPermissions("plat:userWithdrawLog:edit")
	@RequestMapping(value = "save")
	public String save(UserWithdrawLog userWithdrawLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, userWithdrawLog)){
			return form(userWithdrawLog, model);
		}
		userWithdrawLogService.save(userWithdrawLog);
		addMessage(redirectAttributes, "保存用户提现管理成功");
		return "redirect:"+Global.getAdminPath()+"/plat/userWithdrawLog/?repage";
	}
	
	@RequiresPermissions("plat:userWithdrawLog:edit")
	@RequestMapping(value = "checkUserWithdrawLog/{userId}")
	public String checkUserWithdrawLog(UserWithdrawLog userWithdrawLog,@PathVariable("userId") String userId,RedirectAttributes redirectAttributes,HttpServletResponse response) {
		userWithdrawLog.setUserId(userId);
		UserWithdrawLogCount userWithdrawLogCount = userWithdrawLogService.checkUserWithdrawLog(userWithdrawLog);
		renderString(response, "{\"success\": \"1\", \"msg\" : \""+userWithdrawLogCount.getPlusVolumn()+"\"}");
		addMessage(redirectAttributes,"成功");
        return null;
	}
	
	
	@RequiresPermissions("plat:userWithdrawLog:edit")
	@RequestMapping(value = "delete")
	public String delete(UserWithdrawLog userWithdrawLog, RedirectAttributes redirectAttributes) {
		userWithdrawLogService.delete(userWithdrawLog);
		addMessage(redirectAttributes, "删除用户提现管理成功");
		return "redirect:"+Global.getAdminPath()+"/plat/userWithdrawLog/?repage";
	}

	@RequiresPermissions("plat:userWithdrawLog:edit")
	@RequestMapping(value = "audit/{value}")
	public String audit(UserWithdrawLog userWithdrawLog,@PathVariable("value")Integer value, RedirectAttributes redirectAttributes,HttpServletResponse response) {
		UserWithdrawLog dbLog = userWithdrawLogService.get(userWithdrawLog.getId());
		if(dbLog.getStatus().equals("0")){
			dbLog.setStatus(value+"");
		}
		dbLog.setAuditDate(new Date());
		if(StringUtils.isNotBlank(userWithdrawLog.getAuditReason())) {
			dbLog.setAuditReason(userWithdrawLog.getAuditReason());
		}
		userWithdrawLogService.save(dbLog);
		UserCoinVolume userCoinVolume = userCoinVolumeService.getByUserIdAndCoinId(dbLog.getUserId(),dbLog.getCoinId());
		if(value == 2){
			//将提现资产退还给用户
			BigDecimal outlockVolume = userCoinVolume.getOutLockVolume().subtract(userWithdrawLog.getVolume());
			if(outlockVolume.compareTo(BigDecimal.ZERO) >=0){
                userCoinVolume.setOutLockVolume(outlockVolume);
                userCoinVolumeService.updateOutLockVolumeById(userCoinVolume,userWithdrawLog);
            }
		}
		// 删除该用户该币种的redis缓存
		renderString(response, "{\"success\": \"1\", \"msg\" : \"成功\"}");
		addMessage(redirectAttributes, "处理成功");
		//"redirect:"+Global.getAdminPath()+"/plat/userWithdrawLog/?repage"
		return null;
	}

	@RequiresPermissions("plat:userWithdrawLog:edit")
	@RequestMapping(value = "bufa/{value}")
	public String bufa(UserWithdrawLog userWithdrawLog,@PathVariable("value")Integer value, RedirectAttributes redirectAttributes,HttpServletResponse response) {
		UserWithdrawLog dbLog = userWithdrawLogService.get(userWithdrawLog.getId());
		if(dbLog.getStatus().equals("3") && dbLog.getConfirmStatus()==2){
			if(value == 3){
				dbLog.setConfirmStatus(3);
				userWithdrawLogService.save(dbLog);
			}
		}
		renderString(response, "{\"success\": \"1\", \"msg\" : \"成功\"}");
		addMessage(redirectAttributes, "处理成功");
		return "redirect:"+Global.getAdminPath()+"/plat/userWithdrawLog/?repage";
	}

}