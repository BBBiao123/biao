<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>邮件发送日志管理管理</title>
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
		<li class="active"><a href="${ctx}/sys/emailSendLog/">邮件发送日志管理列表</a></li>
		<shiro:hasPermission name="sys:emailSendLog:edit"><li><a href="${ctx}/sys/emailSendLog/form">邮件发送日志管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="emailSendLog" action="${ctx}/sys/emailSendLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<!-- <ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul> -->
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			    <th>id</th>
			    <th>发送目的</th>
			    <th>主题</th>
			    <th>发送内容</th>
			    <th>模板id</th>
			    <th>业务类型</th>
			    <th>创建时间</th>
			    <th>失效时间</th>
				<th>备注信息</th>
				<th>update_date</th>
				<shiro:hasPermission name="sys:emailSendLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="emailSendLog">
			<tr>
				<td><a href="${ctx}/sys/emailSendLog/form?id=${emailSendLog.id}">
					${emailSendLog.id}
				</a></td>
				<td>
					${emailSendLog.email}
				</td>
				<td>
					${emailSendLog.subject}
				</td>
				<td>
					${emailSendLog.content}
				</td>
				<td>
					${emailSendLog.templateId}
				</td>
				<td>
					${emailSendLog.businessType}
				</td>
				<td>
					<fmt:formatDate value="${emailSendLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${emailSendLog.expireTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${emailSendLog.remarks}
				</td>
				<shiro:hasPermission name="sys:emailSendLog:edit"><td>
    				<a href="${ctx}/sys/emailSendLog/form?id=${emailSendLog.id}">修改</a>
					<a href="${ctx}/sys/emailSendLog/delete?id=${emailSendLog.id}" onclick="return confirmx('确认要删除该邮件发送日志管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>