<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>挖矿持币量查询管理</title>
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
		<li class="active"><a href="${ctx}/market/mk2PopularizeMiningHoldCoinQuery/">挖矿持币量查询列表</a></li>
		<shiro:hasPermission name="market:mk2PopularizeMiningHoldCoinQuery:edit"><li><a href="${ctx}/market/mk2PopularizeMiningHoldCoinQuery/form">挖矿持币量查询添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeMiningHoldCoinQuery" action="${ctx}/market/mk2PopularizeMiningHoldCoinQuery/" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>用户ID：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>类别</th>
				<th>持币数量</th>
				<th>挖矿时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="mk2PopularizeMiningHoldCoinQuery">
			<tr>
				<td>
					${mk2PopularizeMiningHoldCoinQuery.type}
				</td>
				<td>
					${mk2PopularizeMiningHoldCoinQuery.holdVolume}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeMiningHoldCoinQuery.countDate}" pattern="yyyy-MM-dd"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>