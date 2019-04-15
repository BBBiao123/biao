<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>手机模板管理</title>
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
		<li class="active"><a href="${ctx}/sys/mobileTemplate/">手机模板列表</a></li>
		<shiro:hasPermission name="sys:mobileTemplate:edit"><li><a href="${ctx}/sys/mobileTemplate/form">手机模板添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mobileTemplate" action="${ctx}/sys/mobileTemplate/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>业务类型：</label>
				<form:input path="code" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>短信key：</label>
				<form:input path="accessKey" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>短信secret：</label>
				<form:input path="accessSecret" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>模板参数：</label>
				<form:input path="templateParam" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键非空</th>
				<th>业务类型</th>
				<th>短信key</th>
				<th>短信secret</th>
				<th>短信签名</th>
				<th>模板参数</th>
				<th>模板id</th>
				<th>业务标识</th>
				<th>备注描述</th>
				<th>扩展字段</th>
				<th>超时时间</th>
				<shiro:hasPermission name="sys:mobileTemplate:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mobileTemplate">
			<tr>
				<td><a href="${ctx}/sys/mobileTemplate/form?id=${mobileTemplate.id}">
					${mobileTemplate.id}
				</a></td>
				<td>
					${mobileTemplate.code}
				</td>
				<td>
					${mobileTemplate.accessKey}
				</td>
				<td>
					${mobileTemplate.accessSecret}
				</td>
				<td>
					${mobileTemplate.signName}
				</td>
				<td>
					${mobileTemplate.templateParam}
				</td>
				<td>
					${mobileTemplate.templateCode}
				</td>
				<td>
					${mobileTemplate.workSign}
				</td>
				<td>
					${mobileTemplate.remark}
				</td>
				<td>
					${mobileTemplate.expandJson}
				</td>
				<td>
					${mobileTemplate.timeOut}
				</td>
				<shiro:hasPermission name="sys:mobileTemplate:edit"><td>
    				<a href="${ctx}/sys/mobileTemplate/form?id=${mobileTemplate.id}">修改</a>
					<a href="${ctx}/sys/mobileTemplate/delete?id=${mobileTemplate.id}" onclick="return confirmx('确认要删除该dd吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>