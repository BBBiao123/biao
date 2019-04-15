<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>金额个人盈收对帐报表</title>
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
		<li class="active"><a href="${ctx}/report/reportUserOfflineReconciliation/">金额个人盈收对帐报表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportUserOfflineReconciliation" action="${ctx}/report/reportUserOfflineReconciliation/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">

			<li><label>手机：</label>
				<form:input path="userMobile" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</li>
			<li><label>日期：</label>
				<input name="beginDayTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${reportUserOfflineReconciliation.beginDayTime}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -
				<input name="endDayTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${reportUserOfflineReconciliation.endDayTime}" pattern="yyyy-MM-dd"/>"
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
				<th>成交数量</th>
				<th>价格</th>
				<th>总价</th>
				<th>创建时间</th>
				<th>确认收到款时间</th>
				<th>备注</th>
				<shiro:hasPermission name="report:reportUserOfflineReconciliation:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportUserOfflineReconciliation">
			<tr>
				<td>
					${reportUserOfflineReconciliation.userMobile}
				</a></td>
				<td>
					<fmt:formatNumber value="${reportUserOfflineReconciliation.volume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${reportUserOfflineReconciliation.price}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${reportUserOfflineReconciliation.totalPrice}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatDate value="${reportUserOfflineReconciliation.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<td>
					<fmt:formatDate value="${reportUserOfflineReconciliation.confirmReceiptDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${reportUserOfflineReconciliation.memo}
				</td>

				<shiro:hasPermission name="report:reportUserOfflineReconciliation:edit"><td>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>