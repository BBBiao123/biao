<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>邮件模板管理管理</title>
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
		<li class="active"><a href="${ctx}/sys/emailTemplate/">邮件模板管理列表</a></li>
		<shiro:hasPermission name="sys:emailTemplate:edit"><li><a href="${ctx}/sys/emailTemplate/form">邮件模板管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="emailTemplate" action="${ctx}/sys/emailTemplate/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>模版名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>模版编码：</label>
				<form:input path="code" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>模版名称</th>
				<th>模版编码</th>
				<th>业务类型</th>
				<th>模版主题</th>
				<th>模版内容</th>
				<th>备注信息</th>
				<th>更新时间</th>
				<th>验证码失效时间（秒）</th>
				<shiro:hasPermission name="sys:emailTemplate:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="emailTemplate">
			<tr>
				<td><a href="${ctx}/sys/emailTemplate/form?id=${emailTemplate.id}">
					${emailTemplate.name}
				</a></td>
				<td>
					${emailTemplate.code}
				</td>
				<td>
					${emailTemplate.businessType}
				</td>
				<td>
					${emailTemplate.templateSubject}
				</td>
				<td>
					${emailTemplate.templateContent}
				</td>
				<td>
					${emailTemplate.remarks}
				</td>
				<td>
					<fmt:formatDate value="${emailTemplate.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${emailTemplate.expireTime}
				</td>
				<shiro:hasPermission name="sys:emailTemplate:edit"><td>
    				<a href="${ctx}/sys/emailTemplate/form?id=${emailTemplate.id}">修改</a>
					<a href="${ctx}/sys/emailTemplate/delete?id=${emailTemplate.id}" onclick="return confirmx('确认要删除该邮件模板管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>