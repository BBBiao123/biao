
package com.thinkgem.jeesite.modules.plat.web;

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
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.service.CoinService;

import java.util.List;

/**
 * 币种资料Controller
 * @author dazi
 * @version 2018-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/coin")
public class CoinController extends BaseController {

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public Coin get(@RequestParam(required=false) String id) {
		Coin entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coinService.get(id);
		}
		if (entity == null){
			entity = new Coin();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:coin:view")
	@RequestMapping(value = {"list", ""})
	public String list(Coin coin, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Coin> page = coinService.findPage(new Page<Coin>(request, response), coin); 
		model.addAttribute("page", page);
		return "modules/plat/coinList";
	}

	@RequiresPermissions("plat:coin:view")
	@RequestMapping(value = "form")
	public String form(Coin coin, Model model) {
		model.addAttribute("coin", coin);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/coinForm";
	}

	@RequiresPermissions("plat:coin:edit")
	@RequestMapping(value = "save")
	public String save(Coin coin, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, coin)){
			return form(coin, model);
		}
		coinService.save(coin);
		addMessage(redirectAttributes, "保存币种成功");
		return "redirect:"+Global.getAdminPath()+"/plat/coin/?repage";
	}
	
	@RequiresPermissions("plat:coin:edit")
	@RequestMapping(value = "delete")
	public String delete(Coin coin, RedirectAttributes redirectAttributes) {
		coinService.delete(coin);
		addMessage(redirectAttributes, "删除币种成功");
		return "redirect:"+Global.getAdminPath()+"/plat/coin/?repage";
	}

}