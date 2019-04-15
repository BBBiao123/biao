<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>银商账户管理</title>
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
		<li class="active"><a href="${ctx}/otc/otcAgentUser/">银商账户列表</a></li>
		<shiro:hasPermission name="otc:otcAgentUser:edit"><li><a href="${ctx}/otc/otcAgentUser/form">银商账户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="otcAgentUser" action="${ctx}/otc/otcAgentUser/" method="post" class="breadcrumb form-search">
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
				<th>更新时间</th>
				<shiro:hasPermission name="otc:otcAgentUser:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="otcAgentUser">
			<tr>
				<td><a href="${ctx}/otc/otcAgentUser/form?id=${otcAgentUser.id}">
					<fmt:formatDate value="${otcAgentUser.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</a></td>
				<shiro:hasPermission name="otc:otcAgentUser:edit"><td>
    				<a href="${ctx}/otc/otcAgentUser/form?id=${otcAgentUser.id}">修改</a>
					<a href="${ctx}/otc/otcAgentUser/delete?id=${otcAgentUser.id}" onclick="return confirmx('确认要删除该银商账户吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>