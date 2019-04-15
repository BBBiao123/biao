<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户登录日志查询管理</title>
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
		<li class="active"><a href="${ctx}/plat/userLoginLog/">用户登录日志查询列表</a></li>
		<shiro:hasPermission name="plat:userLoginLog:edit"><li><a href="${ctx}/plat/userLoginLog/form">用户登录日志查询添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="userLoginLog" action="${ctx}/plat/userLoginLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>loginName：</label>
				<form:input path="loginName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>status：</label>
				<form:select path="status" class="input-medium">
				    <form:option value=""  label="-----"/>
				    <form:option value="0" label="登录成功"/>
				    <form:option value="1" label="登录失败"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>loginName</th>
				<th>ip</th>
				<th>loginTime</th>
				<th>source</th>
				<th>remark</th>
				<th>status</th>
				<th>userId</th>
				<th>mobile</th>
				<shiro:hasPermission name="plat:userLoginLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userLoginLog">
			<tr>
				<td><a href="${ctx}/plat/userLoginLog/form?id=${userLoginLog.id}">
					${userLoginLog.loginName}
				</a></td>
				<td>
					${userLoginLog.ip}
				</td>
				<td>
					${userLoginLog.loginDate}
				</td>
				<td>
					${userLoginLog.source}
				</td>
				<td>
					${userLoginLog.remark}
				</td>
				<td>
				   <c:choose>
				      <c:when test="${userLoginLog.status==0}">
				          	登录成功
				      </c:when>
				      <c:otherwise>
				         	登录失败
				      </c:otherwise>
				   </c:choose>
				</td>
				<td>
					${userLoginLog.userId}
				</td>
				<td>
					${userLoginLog.mobile}
				</td>
				<shiro:hasPermission name="plat:userLoginLog:edit"><td>
    				<a href="${ctx}/plat/userLoginLog/form?id=${userLoginLog.id}">修改</a>
					<a href="${ctx}/plat/userLoginLog/delete?id=${userLoginLog.id}" onclick="return confirmx('确认要删除该用户登录日志查询吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>