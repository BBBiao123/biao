<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>平台充值提现统计管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/report/platDepositAndWithdrawStat/">平台充值提现统计列表</a></li>
		<shiro:hasPermission name="report:platDepositAndWithdrawStat:edit"><li><a href="${ctx}/report/platDepositAndWithdrawStat/form">平台充值提现统计添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="platDepositAndWithdrawStat" action="${ctx}/report/platDepositAndWithdrawStat/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${platDepositAndWithdrawStat.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> -
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${platDepositAndWithdrawStat.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>币种</th>
				<th>充值次数</th>
				<th>归集手续费支出</th>
				<th>充币数量</th>
				<th>提币次数</th>
				<th>提币申请数量</th>
				<th>实际打出数量</th>
				<th>提币手续费</th>
				<th>区块链手续费支出</th>
				<shiro:hasPermission name="report:platDepositAndWithdrawStat:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="platDepositAndWithdrawStat">
			<tr>
				<td>
					${platDepositAndWithdrawStat.coinSymbol}
				</td>
				<td>
					${platDepositAndWithdrawStat.depositNumber}
				</td>
				<td>
					<fmt:formatNumber value="${platDepositAndWithdrawStat.allocationFee}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${platDepositAndWithdrawStat.depositVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					${platDepositAndWithdrawStat.withdrawNumber}
				</td>
				<td>
					<fmt:formatNumber value="${platDepositAndWithdrawStat.withdrawNumber}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${platDepositAndWithdrawStat.withdrawApplyVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${platDepositAndWithdrawStat.withdrawVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${platDepositAndWithdrawStat.blockFeeVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>

				<shiro:hasPermission name="report:platDepositAndWithdrawStat:edit"><td>
    				<a href="${ctx}/report/platDepositAndWithdrawStat/form?id=${platDepositAndWithdrawStat.id}">修改</a>
					<a href="${ctx}/report/platDepositAndWithdrawStat/delete?id=${platDepositAndWithdrawStat.id}" onclick="return confirmx('确认要删除该操盘手资产快照吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>