<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>实名认证限制管理</title>
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
		<li class="active"><a href="${ctx}/plat/jsPlatCardStatusConfig/">实名认证限制列表</a></li>
		<shiro:hasPermission name="plat:jsPlatCardStatusConfig:edit"><li><a href="${ctx}/plat/jsPlatCardStatusConfig/form">实名认证限制添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsPlatCardStatusConfig" action="${ctx}/plat/jsPlatCardStatusConfig/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>value：</label>
				<form:input path="value" htmlEscape="false" maxlength="1" class="input-medium"/>
			</li>
			<li><label>label：</label>
				<form:input path="label" htmlEscape="false" maxlength="225" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>value</th>
				<th>label</th>
				<th>limit_out</th>
				<th>update_date</th>
				<shiro:hasPermission name="plat:jsPlatCardStatusConfig:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsPlatCardStatusConfig">
			<tr>
				<td><a href="${ctx}/plat/jsPlatCardStatusConfig/form?id=${jsPlatCardStatusConfig.id}">
					${jsPlatCardStatusConfig.value}
				</a></td>
				<td>
					${jsPlatCardStatusConfig.label}
				</td>
				<td>
					${jsPlatCardStatusConfig.limitOut}
				</td>
				<td>
					<fmt:formatDate value="${jsPlatCardStatusConfig.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:jsPlatCardStatusConfig:edit"><td>
    				<a href="${ctx}/plat/jsPlatCardStatusConfig/form?id=${jsPlatCardStatusConfig.id}">修改</a>
					<a href="${ctx}/plat/jsPlatCardStatusConfig/delete?id=${jsPlatCardStatusConfig.id}" onclick="return confirmx('确认要删除该实名认证限制吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>