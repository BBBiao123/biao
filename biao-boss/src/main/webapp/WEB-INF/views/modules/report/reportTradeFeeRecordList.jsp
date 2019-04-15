<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>日手续费按交易对统计</title>
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
		<li class="active"><a href="${ctx}/report/reportTradeFeeRecord/">日手续费按交易对统计列表</a></li>
		<shiro:hasPermission name="report:reportTradeFeeRecord:edit"><li><a href="${ctx}/report/reportTradeFeeRecord/form">日手续费按交易对统计添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="reportTradeFeeRecord" action="${ctx}/report/reportTradeFeeRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主币：</label>
				<form:input path="coinMain" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>被交易币：</label>
				<form:input path="coinOther" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>统计时间：</label>
				<input name="countTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${reportTradeFeeRecord.countTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>创建时间：</label>
				<input name="createTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${reportTradeFeeRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>主币</th>
				<th>被交易币</th>
				<th>主币手续费</th>
				<th>被交易币手续费</th>
				<th>统计时间</th>
				<th>创建时间</th>
				<shiro:hasPermission name="report:reportTradeFeeRecord:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportTradeFeeRecord">
			<tr>
				<td><a href="${ctx}/report/reportTradeFeeRecord/form?id=${reportTradeFeeRecord.id}">
					${reportTradeFeeRecord.coinMain}
				</a></td>
				<td>
					${reportTradeFeeRecord.coinOther}
				</td>
				<td>
					${reportTradeFeeRecord.mainFree}
				</td>
				<td>
					${reportTradeFeeRecord.otherFree}
				</td>
				<td>
					<fmt:formatDate value="${reportTradeFeeRecord.countTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${reportTradeFeeRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="report:reportTradeFeeRecord:edit"><td>
    				<a href="${ctx}/report/reportTradeFeeRecord/form?id=${reportTradeFeeRecord.id}">修改</a>
					<a href="${ctx}/report/reportTradeFeeRecord/delete?id=${reportTradeFeeRecord.id}" onclick="return confirmx('确认要删除该dd吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>