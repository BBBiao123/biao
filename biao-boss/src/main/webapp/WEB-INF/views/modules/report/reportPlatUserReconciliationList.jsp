<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>个人资产对帐</title>
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
		<li class="active"><a href="${ctx}/report/reportPlatUserReconciliation/">金额盈收对帐报表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportPlatUserReconciliation" action="${ctx}/report/reportPlatUserReconciliation/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">

			<li><label>userId：</label>
				<form:input path="userId" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="45" class="input-medium"/>
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
				<th>币币资产</th>
				<th>c2c资产</th>
				<th>超级钱包资产</th>
				<th>冻结资产</th>
				<th>总资产</th>
				<th>流水汇总</th>
				<th>币币实际金额</th>
				<th>差额(-为会员亏)</th>
				<shiro:hasPermission name="report:reportPlatUserReconciliation:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportPlatUserReconciliation">
			<tr>
				<td>
					${reportPlatUserReconciliation.coinSymbol}
				</td>
				<td>
					<fmt:formatNumber value="${reportPlatUserReconciliation.tradeVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${reportPlatUserReconciliation.offlineVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${reportPlatUserReconciliation.superVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${reportPlatUserReconciliation.lockVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${reportPlatUserReconciliation.totalVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${reportPlatUserReconciliation.billSumVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${reportPlatUserReconciliation.tradeRealVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${reportPlatUserReconciliation.balance}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<shiro:hasPermission name="report:reportPlatUserReconciliation:edit"><td>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>