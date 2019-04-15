<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币安k线配置管理</title>
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
		<li class="active"><a href="${ctx}/plat/klinePullConfig/">币安k线配置列表</a></li>
		<shiro:hasPermission name="plat:klinePullConfig:edit"><li><a href="${ctx}/plat/klinePullConfig/form">币安k线配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="klinePullConfig" action="${ctx}/plat/klinePullConfig/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主币：</label>
				<form:select path="coinMain" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="name" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>副币：</label>
				<form:select path="coinOther" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="name" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>状态：</label>
				<form:select path="status">
				   <form:option value="0">禁用</form:option>
				   <form:option value="1">启用</form:option>
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
				<th>主币</th>
				<th>副币</th>
				<th>交易所名称</th>
				<th>交易所url</th>
				<th>是否使用代理</th>
				<th>状态</th>
				<th>create_date</th>
				<th>update_date</th>
				<shiro:hasPermission name="plat:klinePullConfig:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="klinePullConfig">
			<tr>
				<td><a href="${ctx}/plat/klinePullConfig/form?id=${klinePullConfig.id}">
					${klinePullConfig.coinMain}
				</a></td>
				<td>
					${klinePullConfig.coinOther}
				</td>
				<td>
					${klinePullConfig.exchangeName}
				</td>
				<td>
					${klinePullConfig.pullUrl}
				</td>
				<td>
					<c:choose>
					    <c:when test="${klinePullConfig.proxyed==0}">否</c:when>
					    <c:when test="${klinePullConfig.proxyed==1}">是</c:when>
					    <c:otherwise></c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:choose>
					    <c:when test="${klinePullConfig.status==0}">禁用</c:when>
					    <c:when test="${klinePullConfig.status==1}">启用</c:when>
					    <c:otherwise></c:otherwise>
					</c:choose>
				</td>
				<td>
					<fmt:formatDate value="${klinePullConfig.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${klinePullConfig.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:klinePullConfig:edit"><td>
    				<a href="${ctx}/plat/klinePullConfig/form?id=${klinePullConfig.id}">修改</a>
					<a href="${ctx}/plat/klinePullConfig/delete?id=${klinePullConfig.id}" onclick="return confirmx('确认要删除该币安k线配置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>