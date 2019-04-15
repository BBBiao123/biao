<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>eth token withdraw管理</title>
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
		<li class="active"><a href="${ctx}/plat/ethTokenWithdraw/">eth token withdraw列表</a></li>
		<shiro:hasPermission name="plat:ethTokenWithdraw:edit"><li><a href="${ctx}/plat/ethTokenWithdraw/form">eth token withdraw添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="ethTokenWithdraw" action="${ctx}/plat/ethTokenWithdraw/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>name：</label>
				<form:input path="name" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label>from_address：</label>
				<form:input path="fromAddress" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>to_address：</label>
				<form:input path="toAddress" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>status：</label>
				<form:input path="status" htmlEscape="false" maxlength="1" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>name</th>
				<th>volume</th>
				<th>from_address</th>
				<th>to_address</th>
				<th>status</th>
				<th>创建者</th>
				<th>创建时间</th>
				<th>更新者</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:ethTokenWithdraw:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ethTokenWithdraw">
			<tr>
				<td><a href="${ctx}/plat/ethTokenWithdraw/form?id=${ethTokenWithdraw.id}">
					${ethTokenWithdraw.name}
				</a></td>
				<td>
					${ethTokenWithdraw.volume}
				</td>
				<td>
					${ethTokenWithdraw.fromAddress}
				</td>
				<td>
					${ethTokenWithdraw.toAddress}
				</td>
				<td>
				    ${fns:getDictLabel(ethTokenWithdraw.status, 'eth_token_status', '')}
				</td>
				<td>
					${ethTokenWithdraw.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${ethTokenWithdraw.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${ethTokenWithdraw.updateBy.id}
				</td>
				<td>
					<fmt:formatDate value="${ethTokenWithdraw.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:ethTokenWithdraw:edit"><td>
    				<a href="${ctx}/plat/ethTokenWithdraw/form?id=${ethTokenWithdraw.id}">修改</a>
					<a href="${ctx}/plat/ethTokenWithdraw/delete?id=${ethTokenWithdraw.id}" onclick="return confirmx('确认要删除该eth token withdraw吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>