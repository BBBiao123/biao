<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>dd管理</title>
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
		<li class="active"><a href="${ctx}/report/reportTradeFee/">dd列表</a></li>
		<shiro:hasPermission name="report:reportTradeFee:edit"><li><a href="${ctx}/report/reportTradeFee/form">dd添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="reportTradeFee" action="${ctx}/report/reportTradeFee/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>交易主币：</label>
				<form:input path="coinMain" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>交易副币：</label>
				<form:input path="coinOther" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>统计时间：</label>
				<input name="countTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${reportTradeFee.countTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>手续费币种：</label>
				<form:input path="coin" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>记录创建时间：</label>
				<input name="createTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${reportTradeFee.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>主键</th>
				<th>交易主币</th>
				<th>交易副币</th>
				<th>手续费总量</th>
				<th>统计时间</th>
				<th>手续费币种</th>
				<th>记录创建时间</th>
				<shiro:hasPermission name="report:reportTradeFee:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportTradeFee">
			<tr>
				<td><a href="${ctx}/report/reportTradeFee/form?id=${reportTradeFee.id}">
					${reportTradeFee.id}
				</a></td>
				<td>
					${reportTradeFee.coinMain}
				</td>
				<td>
					${reportTradeFee.coinOther}
				</td>
				<td>
					${reportTradeFee.sumFee}
				</td>
				<td>
					<fmt:formatDate value="${reportTradeFee.countTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${reportTradeFee.coin}
				</td>
				<td>
					<fmt:formatDate value="${reportTradeFee.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="report:reportTradeFee:edit"><td>
    				<a href="${ctx}/report/reportTradeFee/form?id=${reportTradeFee.id}">修改</a>
					<a href="${ctx}/report/reportTradeFee/delete?id=${reportTradeFee.id}" onclick="return confirmx('确认要删除该dd吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>