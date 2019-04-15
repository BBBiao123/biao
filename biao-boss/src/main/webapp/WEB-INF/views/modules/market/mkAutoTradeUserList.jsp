<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>自动交易用户管理</title>
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
		<li class="active"><a href="${ctx}/market/mkAutoTradeUser/">自动交易用户列表</a></li>
		<shiro:hasPermission name="market:mkAutoTradeUser:edit"><li><a href="${ctx}/market/mkAutoTradeUser/form">自动交易用户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkAutoTradeUser" action="${ctx}/market/mkAutoTradeUser/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>userId：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>userId</th>
				<th>手机</th>
				<th>邮箱</th>
				<th>真实姓名</th>
				<th>身份证</th>
				<th>来源</th>
				<shiro:hasPermission name="market:mkAutoTradeUser:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkAutoTradeUser">
			<tr>
				<td><a href="${ctx}/market/mkAutoTradeUser/form?id=${mkAutoTradeUser.id}">
					${mkAutoTradeUser.userId}
				</a></td>
				<td>
					${mkAutoTradeUser.mobile}
				</td>
				<td>
					${mkAutoTradeUser.mail}
				</td>
				<td>
					${mkAutoTradeUser.realName}
				</td>
				<td>
					${mkAutoTradeUser.idCard}
				</td>
				<td>
					${mkAutoTradeUser.source}
				</td>
				<shiro:hasPermission name="market:mkAutoTradeUser:edit"><td>
					<c:if test="${mkAutoTradeUser.source eq 'add'}"><a href="${ctx}/market/mkAutoTradeUser/form?id=${mkAutoTradeUser.id}">修改</a></c:if>
					<c:if test="${mkAutoTradeUser.source eq 'add'}"><a href="${ctx}/market/mkAutoTradeUser/delete?id=${mkAutoTradeUser.id}" onclick="return confirmx('确认要删除该自动交易用户吗？', this.href)">删除</a></c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>