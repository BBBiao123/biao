/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.gen.util.StringHelp;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeHistory;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.JsPlatUserCoinVolumeHistoryService;
import com.thinkgem.jeesite.modules.plat.service.PlatUserService;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 手动转账Controller
 * @author ruoyu
 * @version 2018-08-09
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/jsPlatUserCoinVolumeHistoryOther")
public class JsPlatUserCoinVolumeHistoryFromOtherController extends BaseController {

	@Autowired
	private JsPlatUserCoinVolumeHistoryService jsPlatUserCoinVolumeHistoryService;
	@Autowired
	private PlatUserService platUserService;
	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public JsPlatUserCoinVolumeHistory get(@RequestParam(required=false) String id) {
		JsPlatUserCoinVolumeHistory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsPlatUserCoinVolumeHistoryService.get(id);
		}
		if (entity == null){
			entity = new JsPlatUserCoinVolumeHistory();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:jsPlatUserCoinVolumeHistoryOther:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsPlatUserCoinVolumeHistory jsPlatUserCoinVolumeHistory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JsPlatUserCoinVolumeHistory> page = jsPlatUserCoinVolumeHistoryService.findPage(new Page<JsPlatUserCoinVolumeHistory>(request, response), jsPlatUserCoinVolumeHistory); 
		model.addAttribute("page", page);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/jsPlatUserCoinVolumeHistoryListOther";
	}

	@RequiresPermissions("plat:jsPlatUserCoinVolumeHistoryOther:view")
	@RequestMapping(value = "form")
	public String form(JsPlatUserCoinVolumeHistory jsPlatUserCoinVolumeHistory, Model model) {
		model.addAttribute("jsPlatUserCoinVolumeHistory", jsPlatUserCoinVolumeHistory);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		//是否现在用户tag
		model.addAttribute("userTag", "true");
		return "modules/plat/jsPlatUserCoinVolumeHistoryFormOther";
	}

	@RequiresPermissions("plat:jsPlatUserCoinVolumeHistoryOther:edit")
	@RequestMapping(value = "save")
	public String save(JsPlatUserCoinVolumeHistory jsPlatUserCoinVolumeHistory, Model model, RedirectAttributes redirectAttributes) {
		String account = jsPlatUserCoinVolumeHistory.getAccount() ;
		String userTag = jsPlatUserCoinVolumeHistory.getUserTag();
		if(StringUtils.isBlank(userTag)) {
			model.addAttribute("message", "用户Tag标签必须输入");
			return form(jsPlatUserCoinVolumeHistory, model);
		}
		if(StringUtils.isNotBlank(account)) {
			PlatUser platUser = new PlatUser();
			if(StringHelp.checkMobileNumber(account)) {
				platUser.setMobile(account);
			}else {
				platUser.setMail(account);
			}
			List<PlatUser> platUsers = platUserService.findList(platUser);
			if(!CollectionUtils.isEmpty(platUsers)&&platUsers.size()==1) {
				User user = new User();
				user.setId(platUsers.get(0).getId());
				jsPlatUserCoinVolumeHistory.setUser(user);
				//判断用户的tag是否正确
				if(platUsers.get(0).getTag()==null) {
					model.addAttribute("message", "用户Tag标签不正确");
					return form(jsPlatUserCoinVolumeHistory, model);
				}
				if(!userTag.equals(platUsers.get(0).getTag())) {
					model.addAttribute("message", "用户Tag标签不正确");
					return form(jsPlatUserCoinVolumeHistory, model);
				}
			}
		}
		if (!beanValidator(model, jsPlatUserCoinVolumeHistory)){
			return form(jsPlatUserCoinVolumeHistory, model);
		}
		Coin mainCoin = coinService.get(jsPlatUserCoinVolumeHistory.getCoinId());
		jsPlatUserCoinVolumeHistory.setCoinSymbol(mainCoin.getName());
		BigDecimal bigDecimal = new BigDecimal(jsPlatUserCoinVolumeHistory.getVolume());
		if(mainCoin.getName().equalsIgnoreCase("BTC")&&bigDecimal.compareTo(new BigDecimal("10"))>0) {
			model.addAttribute("message", "BTC转账不能超过10个");
			return form(jsPlatUserCoinVolumeHistory, model);
		}
		if(!mainCoin.getName().equalsIgnoreCase("BTC")&&bigDecimal.compareTo(new BigDecimal("10000000"))>0) {
			model.addAttribute("message", mainCoin.getName()+"转账不能超过10000000个");
			return form(jsPlatUserCoinVolumeHistory, model);
		}
		jsPlatUserCoinVolumeHistory.setAccount(account+"("+userTag+")");
		jsPlatUserCoinVolumeHistoryService.save(jsPlatUserCoinVolumeHistory);
		addMessage(redirectAttributes, "保存手动转账成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatUserCoinVolumeHistoryOther/?repage";
	}
	
	@RequiresPermissions("plat:jsPlatUserCoinVolumeHistoryOther:edit")
	@RequestMapping(value = "delete")
	public String delete(JsPlatUserCoinVolumeHistory jsPlatUserCoinVolumeHistory, RedirectAttributes redirectAttributes) {
		jsPlatUserCoinVolumeHistoryService.delete(jsPlatUserCoinVolumeHistory);
		addMessage(redirectAttributes, "删除手动转账成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatUserCoinVolumeHistoryOther/?repage";
	}

}