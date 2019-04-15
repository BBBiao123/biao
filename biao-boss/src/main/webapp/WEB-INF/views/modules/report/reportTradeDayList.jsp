<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>主区交易量管理</title>
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
		<li class="active"><a href="${ctx}/report/reportTradeDay/">主区交易量列表</a></li>
		<shiro:hasPermission name="report:reportTradeDay:edit"><li><a href="${ctx}/report/reportTradeDay/form">主区交易量添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="reportTradeDay" action="${ctx}/report/reportTradeDay/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主区：</label>
				<form:input path="coinMain" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li><label>被交易区：</label>
				<form:input path="coinOther" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li><label>统计时间：</label>
				<input name="countTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${reportTradeDay.countTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>主区</th>
				<th>被交易区</th>
				<th>收盘价</th>
				<th>开盘价</th>
				<th>最高价</th>
				<th>最低价</th>
				<th>交易量</th>
				<th>统计时间</th>
				<shiro:hasPermission name="report:reportTradeDay:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportTradeDay">
			<tr>
				<td><a href="${ctx}/report/reportTradeDay/form?id=${reportTradeDay.id}">
					${reportTradeDay.coinMain}
				</a></td>
				<td>
					${reportTradeDay.coinOther}
				</td>
				<td>
					${reportTradeDay.latestPrice}
				</td>
				<td>
					${reportTradeDay.firstPrice}
				</td>
				<td>
					${reportTradeDay.highestPrice}
				</td>
				<td>
					${reportTradeDay.lowerPrice}
				</td>
				<td>
					${reportTradeDay.dayCount}
				</td>
				<td>
					<fmt:formatDate value="${reportTradeDay.countTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="report:reportTradeDay:edit"><td>
    				<a href="${ctx}/report/reportTradeDay/form?id=${reportTradeDay.id}">修改</a>
					<a href="${ctx}/report/reportTradeDay/delete?id=${reportTradeDay.id}" onclick="return confirmx('确认要删除该主区交易量吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>