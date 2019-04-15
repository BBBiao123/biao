<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>金额盈收对帐报表</title>
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
		<li class="active"><a href="${ctx}/report/reportOfflineReconciliation/">金额盈收对帐报表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportOfflineReconciliation" action="${ctx}/report/reportOfflineReconciliation/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">

			<li><label>账户类型：</label>
				<form:select path="tag" class="input-medium">
					<form:options items="${fns:getDictList('reportOfflineUserTag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>日期：</label>
				<input name="beginDayTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${reportOfflineReconciliation.beginDayTime}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -
				<input name="endDayTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${reportOfflineReconciliation.endDayTime}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>手机</th>
				<th>邮箱</th>
				<th>日期</th>
				<th>收入金额</th>
				<th>支出金额</th>
				<th>余额</th>
				<th>用户标签</th>
				<shiro:hasPermission name="report:reportOfflineReconciliation:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportOfflineReconciliation">
			<tr>
				<td>
					${reportOfflineReconciliation.mobile}
				</a></td>
				<td>
					${reportOfflineReconciliation.mail}
				</td>
				<td>
					${reportOfflineReconciliation.dayTime}
				</td>
				<td>
					<fmt:formatNumber value="${reportOfflineReconciliation.income}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${reportOfflineReconciliation.payout}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				<td>
					<fmt:formatNumber value="${reportOfflineReconciliation.balance}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					${reportOfflineReconciliation.tag}
				</td>

				<shiro:hasPermission name="report:reportOfflineReconciliation:edit"><td>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>