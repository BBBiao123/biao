/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.web;

import com.csvreader.CsvWriter;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.ExOrder;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.report.entity.ReportPlatUserReconciliationDetail;
import com.thinkgem.jeesite.modules.report.service.ReportPlatUserReconciliationDetailService;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * 主区各币种交易量Controller
 * @author zzj
 * @version 2018-10-10
 */
@Controller
@RequestMapping(value = "${adminPath}/report/reportPlatUserReconciliationDetail")
public class ReportPlatUserReconciliationDetailController extends BaseController {

	@Autowired
	private ReportPlatUserReconciliationDetailService reportPlatUserReconciliationDetailService;

	@Autowired
	private CoinService coinService;

	
	@ModelAttribute
	public ReportPlatUserReconciliationDetail get(@RequestParam(required=false) String id) {
		ReportPlatUserReconciliationDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = reportPlatUserReconciliationDetailService.get(id);
		}
		if (entity == null){
			entity = new ReportPlatUserReconciliationDetail();
		}
		return entity;
	}

	@RequiresPermissions("report:reportPlatUserReconciliationDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportPlatUserReconciliationDetail reportPlatUserReconciliationDetail, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<ReportPlatUserReconciliationDetail> page = new Page<ReportPlatUserReconciliationDetail>(request, response);
		String userId = reportPlatUserReconciliationDetail.getUserId();
		if((StringUtils.isNotEmpty(reportPlatUserReconciliationDetail.getUserId()) || StringUtils.isNotEmpty(reportPlatUserReconciliationDetail.getMail()) || StringUtils.isNotEmpty(reportPlatUserReconciliationDetail.getMobile()))
				){
			page = reportPlatUserReconciliationDetailService.findPage(page, reportPlatUserReconciliationDetail);
		}else{
			page.setList(null);
		}
		reportPlatUserReconciliationDetail.setUserId(userId);
		model.addAttribute("page", page);

		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);

		return "modules/report/reportPlatUserReconciliationDetailList";
	}

	@RequiresPermissions("report:reportPlatUserReconciliationDetail:view")
	@RequestMapping(value = "form")
	public String form(ReportPlatUserReconciliationDetail reportPlatUserReconciliationDetail, Model model) {
		model.addAttribute("reportPlatUserReconciliationDetail", reportPlatUserReconciliationDetail);
		return "modules/report/reportPlatUserReconciliationDetailForm";
	}


	/**
	 * 导出CVS
	 * @param response
	 * @throws IOException
	 */
	@RequiresPermissions("report:reportPlatUserReconciliationDetail:view")
	@RequestMapping(value = "exportFile")
	public void exportFile(ReportPlatUserReconciliationDetail reportPlatUserReconciliationDetail, HttpServletRequest request, HttpServletResponse response, Model model)
			throws IOException {

		List<ReportPlatUserReconciliationDetail> reportPlatUserReconciliationDetailList = null;
		if((StringUtils.isNotEmpty(reportPlatUserReconciliationDetail.getUserId()) || StringUtils.isNotEmpty(reportPlatUserReconciliationDetail.getMail()) || StringUtils.isNotEmpty(reportPlatUserReconciliationDetail.getMobile()))
				){
			reportPlatUserReconciliationDetailList = reportPlatUserReconciliationDetailService.findList(reportPlatUserReconciliationDetail);
		}

		response.setContentType("application/csv;charset=GBK");
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("会员资产明细核对导出报表("+ DateUtils.formatDate(new Date())+").csv", "UTF-8"));

		char delimiter = ',';
		try {
			response.setCharacterEncoding("GBK");
			CsvWriter csvOutput = new CsvWriter(response.getOutputStream(), delimiter, Charset.forName("GBK"));
			csvOutput.setEscapeMode(CsvWriter.ESCAPE_MODE_DOUBLED);
			//第一行
			this.writeExportFileHeader(csvOutput);
			//循环写出数据
			if(CollectionUtils.isNotEmpty(reportPlatUserReconciliationDetailList)){
				for(ReportPlatUserReconciliationDetail data : reportPlatUserReconciliationDetailList){
					csvOutput.write(DateUtils.formatDateTime(data.getCreateDate()));
					csvOutput.write(data.getType());
					csvOutput.write(data.getCoinSymbol());
					csvOutput.write(String.valueOf(data.getVolume()));
					csvOutput.endRecord();
				}
			}
			csvOutput.close();
		}catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void writeExportFileHeader(CsvWriter csvOutput) throws IOException{
		csvOutput.write("时间");
		csvOutput.write("资产获得类型");
		csvOutput.write("币种");
		csvOutput.write("数量");
		csvOutput.endRecord();
	}


}