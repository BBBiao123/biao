<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>银商列表管理</title>
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
		<li class="active"><a href="${ctx}/otc/otcAgentInfo/">银商列表列表</a></li>
		<shiro:hasPermission name="otc:otcAgentInfo:edit"><li><a href="${ctx}/otc/otcAgentInfo/form">银商列表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="otcAgentInfo" action="${ctx}/otc/otcAgentInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>银商名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('otcAgentStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>银商名称</th>
				<th>状态</th>
				<th>折扣</th>
				<th>用户登录名</th>
				<th>初始邮箱</th>
				<th>币种代号</th>
				<th>说明</th>
				<th>创建日期</th>
				<th>更新时间</th>
				<shiro:hasPermission name="otc:otcAgentInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="otcAgentInfo">
			<tr>
				<td><a href="${ctx}/otc/otcAgentInfo/form?id=${otcAgentInfo.id}">
					${otcAgentInfo.id}
				</a></td>
				<td>
					${otcAgentInfo.name}
				</td>
				<td>
					${fns:getDictLabel(otcAgentInfo.status, 'otcAgentStatus', '')}
				</td>
				<td>
					${otcAgentInfo.discount}
				</td>
				<td>
					${otcAgentInfo.sysUserName}
				</td>
				<td>
					${otcAgentInfo.sysUserMail}
				</td>
				<td>
					${otcAgentInfo.coinSymbol}
				</td>
				<td>
					${otcAgentInfo.remark}
				</td>
				<td>
					<fmt:formatDate value="${otcAgentInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${otcAgentInfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="otc:otcAgentInfo:edit"><td>
    				<a href="${ctx}/otc/otcAgentInfo/form?id=${otcAgentInfo.id}">修改</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>