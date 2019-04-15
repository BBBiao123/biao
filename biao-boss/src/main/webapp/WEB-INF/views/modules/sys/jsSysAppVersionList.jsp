<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>App版本管理管理</title>
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
		<li class="active"><a href="${ctx}/sys/jsSysAppVersion/">App版本管理列表</a></li>
		<shiro:hasPermission name="sys:jsSysAppVersion:edit"><li><a href="${ctx}/sys/jsSysAppVersion/form">App版本管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsSysAppVersion" action="${ctx}/sys/jsSysAppVersion/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>APP类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('appType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>主键id</th>
				<th>APP类型</th>
				<th>版本号</th>
				<th>说明</th>
				<th>下载地址</th>
				<th>是否强制更新</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="sys:jsSysAppVersion:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsSysAppVersion">
			<tr>
				<td><a href="${ctx}/sys/jsSysAppVersion/form?id=${jsSysAppVersion.id}">
					${jsSysAppVersion.id}
				</a></td>
				<td>
					${fns:getDictLabel(jsSysAppVersion.type, 'appType', '')}
				</td>
				<td>
					${jsSysAppVersion.version}
				</td>
				<td>
					${jsSysAppVersion.remarks}
				</td>
				<td>
					${jsSysAppVersion.address}
				</td>
				<td>
						${fns:getDictLabel(jsSysAppVersion.isUpgrade, 'sysIsUpgrade', '')}
				</td>
				<td>
					<fmt:formatDate value="${jsSysAppVersion.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${jsSysAppVersion.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="sys:jsSysAppVersion:edit"><td>
    				<a href="${ctx}/sys/jsSysAppVersion/form?id=${jsSysAppVersion.id}">修改</a>
					<a href="${ctx}/sys/jsSysAppVersion/delete?id=${jsSysAppVersion.id}" onclick="return confirmx('确认要删除该App版本管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>