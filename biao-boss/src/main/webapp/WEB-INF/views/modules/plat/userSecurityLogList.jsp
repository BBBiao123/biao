<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户安全日志查询管理</title>
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
		<li class="active"><a href="${ctx}/plat/userSecurityLog/">用户安全日志查询列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="securityLog" action="${ctx}/plat/userSecurityLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>userId：</label>
				<form:input path="userId" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>mobile：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>type：</label>
				<form:select path="type" class="input-medium">
				    <form:option value=""  label="-----"/>
				    <form:option value="1" label="修改密码"/>
				    <form:option value="2" label="绑定手机号"/>
				    <form:option value="3" label="重置密码"/>
				    <form:option value="4" label="修改手机号"/>
				    <form:option value="5" label="绑定谷歌"/>
				    <form:option value="6" label="绑定交易密码"/>
				    <form:option value="7" label="修改交易密码"/>
				    <form:option value="8" label="切换交易类型"/>
				    <form:option value="9" label="设置交易类型"/>
				</form:select>
			</li>
			<li><label>status：</label>
				<form:select path="status" class="input-medium">
				    <form:option value=""  label="-----"/>
				    <form:option value="0" label="成功"/>
				    <form:option value="1" label="失败"/>
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
				<th>type</th>
				<th>userId</th>
				<th>mobile</th>
				<th>mail</th>
				<th>status</th>
				<th>updateTime</th>
				<th>remark</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userScutiyLog">
			<tr>
				<td>
				   <c:choose>
				      <c:when test="${userScutiyLog.type==1}">修改密码</c:when>
				      <c:when test="${userScutiyLog.type==2}">绑定手机号</c:when>
				      <c:when test="${userScutiyLog.type==3}">重置密码</c:when>
				      <c:when test="${userScutiyLog.type==4}">修改手机号</c:when>
				      <c:when test="${userScutiyLog.type==5}">绑定谷歌</c:when>
				      <c:when test="${userScutiyLog.type==6}">绑定交易密码</c:when>
				      <c:when test="${userScutiyLog.type==7}">修改交易密码</c:when>
				      <c:when test="${userScutiyLog.type==8}">切换交易类型</c:when>
				      <c:when test="${userScutiyLog.type==9}">设置交易类型</c:when>
				      <c:otherwise></c:otherwise>
				   </c:choose>
				</td>
				<td>
					${userScutiyLog.userId}
				</td>
				<td>
					${userScutiyLog.mobile}
				</td>
				<td>
					${userScutiyLog.mail}
				</td>
				<td>
				   <c:choose>
				      <c:when test="${userScutiyLog.status==1}">
				          	失败
				      </c:when>
				      <c:otherwise>
				         	成功
				      </c:otherwise>
				   </c:choose>
				</td>
				<td>
					${userScutiyLog.updateTimeStr}
				</td>
				<td>
				   ${userScutiyLog.remark}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>