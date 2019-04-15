/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import com.csvreader.CsvWriter;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.entity.ExOrder;
import com.thinkgem.jeesite.modules.plat.entity.mongo.TradeDetail;
import com.thinkgem.jeesite.modules.plat.service.ExOrderService;
import com.thinkgem.jeesite.modules.plat.service.mongo.TradeDetailService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

/**
 * 币币订单Controller
 * @author dazi
 * @version 2018-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/exOrder")
public class ExOrderController extends BaseController {

	@Autowired
	private ExOrderService exOrderService;

	@Autowired
	private TradeDetailService tradeDetailService;
	
	@ModelAttribute
	public ExOrder get(@RequestParam(required=false) String id) {
		ExOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = exOrderService.get(id);
		}
		if (entity == null){
			entity = new ExOrder();
		}
		return entity;
	}

	@RequiresPermissions("plat:exOrder:view")
	@RequestMapping(value = {"list", ""})
	public String list(ExOrder exOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ExOrder> page = exOrderService.findPage(new Page<ExOrder>(request, response), exOrder); 
		model.addAttribute("page", page);
		return "modules/plat/exOrderList";
	}

	@RequiresPermissions("plat:exOrder:details")
	@RequestMapping(value = "details")
	public String details(ExOrder exOrder, Model model) {
		final List<TradeDetail> details = tradeDetailService.findByOrderNo(exOrder.getId());
		model.addAttribute("details", details);
		return "modules/plat/exOrderDetails";
	}

	 /**
	 * 导出CVS
	 * @param response
	 * @throws IOException
	 */
	 @RequiresPermissions("plat:exOrder:export")
	 @RequestMapping(value = "exportFile")
	public void exportFile(ExOrder exOrder, HttpServletRequest request, HttpServletResponse response, Model model)
			throws IOException {

		List<ExOrder> exOrderList = null;
	 	if(StringUtils.isEmpty(exOrder.getExType()) && StringUtils.isEmpty(exOrder.getUserId()) && StringUtils.isEmpty(exOrder.getCoinSymbol()) && StringUtils.isEmpty(exOrder.getStatus())
				&& Objects.isNull(exOrder.getBeginCreateDate()) && Objects.isNull(exOrder.getEndCreateDate())){
		}else{
			exOrderList = exOrderService.findList(exOrder);
		}


		response.setContentType("application/csv;charset=GBK");
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("币币交易导出报表.csv", "UTF-8"));

		char delimiter = ',';
		try {
			response.setCharacterEncoding("GBK");
			CsvWriter csvOutput = new CsvWriter(response.getOutputStream(), delimiter, Charset.forName("GBK"));
			csvOutput.setEscapeMode(CsvWriter.ESCAPE_MODE_DOUBLED);
			//第一行
			this.writeExportFileHeader(csvOutput);
			//循环写出数据
			if(CollectionUtils.isNotEmpty(exOrderList)){
				for(ExOrder data : exOrderList){
					csvOutput.write(data.getUserId());
					csvOutput.write(String.valueOf(data.getAskVolume()));
					csvOutput.write(String.valueOf(data.getPrice()));
					csvOutput.write(String.valueOf(data.getSuccessVolume()));
					csvOutput.write(data.getCoinSymbol());
					csvOutput.write(String.valueOf(data.getExFee()));
					csvOutput.write(this.getOrderStatusStr(data));
					csvOutput.write(data.getToCoinSymbol());
					csvOutput.write(String.valueOf(data.getToCoinVolume()));
					csvOutput.write(data.getExType().equals("0") ? "买" : "卖");
					csvOutput.write(DateUtils.formatDateTime(data.getCreateDate()));
					csvOutput.write(DateUtils.formatDateTime(data.getUpdateDate()));
					csvOutput.endRecord();
				}
			}
			csvOutput.close();
		}catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void writeExportFileHeader(CsvWriter csvOutput) throws IOException{
		csvOutput.write("用户id");
		csvOutput.write("挂单数量");
		csvOutput.write("挂单价格");
		csvOutput.write("消费数量");
		csvOutput.write("挂单币种标识");
		csvOutput.write("手续费")
		;csvOutput.write("成交状态");
		csvOutput.write("换取的币种符号");
		csvOutput.write("交易得到的币的数量");
		csvOutput.write("挂单类型");
		csvOutput.write("创建时间");
		csvOutput.write("更新时间");
		csvOutput.endRecord();
	}

	private String getOrderStatusStr(ExOrder exOrder){
	 	if("0".equals(exOrder.getStatus())){
	 		return "未成交";
		}else if("1".equals(exOrder.getStatus())){
			return "部分成功";
		}else if("2".equals(exOrder.getStatus())){
			return "全部成功";
		}else if("3".equals(exOrder.getStatus())){
			return "部分取消";
		}else if("4".equals(exOrder.getStatus())){
			return "全部取消";
		}else{
	 		return "未知";
		}
	}


	@RequiresPermissions("plat:exOrder:view")
	@RequestMapping(value = "form")
	public String form(ExOrder exOrder, Model model) {
		model.addAttribute("exOrder", exOrder);
		return "modules/plat/exOrderForm";
	}


}