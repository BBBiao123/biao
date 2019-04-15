/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import java.util.List;

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
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineOrderDetailLog;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.JsPlatOfflineOrderDetailLogService;
import com.thinkgem.jeesite.modules.plat.service.PlatUserService;

/**
 * c2c流水统计表Controller
 * @author ruoyu
 * @version 2018-10-24
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/jsPlatOfflineOrderDetailLog")
public class JsPlatOfflineOrderDetailLogController extends BaseController {

	@Autowired
	private JsPlatOfflineOrderDetailLogService jsPlatOfflineOrderDetailLogService;
	
	@Autowired
	private CoinService coinService;
	
	@Autowired
	private PlatUserService platUserService ;
	
	@ModelAttribute
	public JsPlatOfflineOrderDetailLog get(@RequestParam(required=false) String id) {
		JsPlatOfflineOrderDetailLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsPlatOfflineOrderDetailLogService.get(id);
		}
		if (entity == null){
			entity = new JsPlatOfflineOrderDetailLog();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:jsPlatOfflineOrderDetailLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsPlatOfflineOrderDetailLog jsPlatOfflineOrderDetailLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isNotBlank(jsPlatOfflineOrderDetailLog.getMobile())) {
			PlatUser platUser = new PlatUser();
			platUser.setMobile(jsPlatOfflineOrderDetailLog.getMobile());
			List<PlatUser> users = platUserService.findList(platUser);
			if(users!=null&&users.size()==1) {
				jsPlatOfflineOrderDetailLog.setUserId(users.get(0).getId());
			}
		}
		Page<JsPlatOfflineOrderDetailLog> page = jsPlatOfflineOrderDetailLogService.findPage(new Page<JsPlatOfflineOrderDetailLog>(request, response), jsPlatOfflineOrderDetailLog); 
		if(StringUtils.isNotBlank(jsPlatOfflineOrderDetailLog.getMobile())) {
			jsPlatOfflineOrderDetailLog.setUserId(null);
		}
		model.addAttribute("page", page);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/jsPlatOfflineOrderDetailLogList";
	}

	@RequiresPermissions("plat:jsPlatOfflineOrderDetailLog:view")
	@RequestMapping(value = "form")
	public String form(JsPlatOfflineOrderDetailLog jsPlatOfflineOrderDetailLog, Model model) {
		model.addAttribute("jsPlatOfflineOrderDetailLog", jsPlatOfflineOrderDetailLog);
		return "modules/plat/jsPlatOfflineOrderDetailLogForm";
	}

	@RequiresPermissions("plat:jsPlatOfflineOrderDetailLog:view")
	@RequestMapping(value = "save")
	public String save(JsPlatOfflineOrderDetailLog jsPlatOfflineOrderDetailLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsPlatOfflineOrderDetailLog)){
			return form(jsPlatOfflineOrderDetailLog, model);
		}
		JsPlatOfflineOrderDetailLog query = new JsPlatOfflineOrderDetailLog();
		query.setCountLessDate(jsPlatOfflineOrderDetailLog.getCountDate());
		List<JsPlatOfflineOrderDetailLog> detailLogs = jsPlatOfflineOrderDetailLogService.findList(query);
		if(detailLogs!=null&&detailLogs.size()>0) {
			model.addAttribute("message", "改时间已经统计过了，请勿重复统计");
			return form(jsPlatOfflineOrderDetailLog, model);
		}
		jsPlatOfflineOrderDetailLogService.save(jsPlatOfflineOrderDetailLog);
		addMessage(redirectAttributes, "保存c2c流水统计表成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatOfflineOrderDetailLog/?repage";
	}
	
	@RequiresPermissions("plat:jsPlatOfflineOrderDetailLog:edit")
	@RequestMapping(value = "delete")
	public String delete(JsPlatOfflineOrderDetailLog jsPlatOfflineOrderDetailLog, RedirectAttributes redirectAttributes) {
		jsPlatOfflineOrderDetailLogService.delete(jsPlatOfflineOrderDetailLog);
		addMessage(redirectAttributes, "删除c2c流水统计表成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatOfflineOrderDetailLog/?repage";
	}

}