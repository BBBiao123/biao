<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币币交易价格区间统计报表</title>
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
		<li class="active"><a href="${ctx}/report/reportTradePriceRange/">币币交易价格区间统计报表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportTradePriceRange" action="${ctx}/report/reportTradePriceRange/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>类型</th>
				<th>价格区间</th>
				<th>挂单数量</th>
				<shiro:hasPermission name="report:reportTradePriceRange:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportTradePriceRange">
			<tr>
				<td>
					${reportTradePriceRange.type}
				</td>
				<td>
					${reportTradePriceRange.priceRange}
				</td>
				<td>
					<fmt:formatNumber value="${reportTradePriceRange.volume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<shiro:hasPermission name="report:reportTradePriceRange:edit"><td>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>