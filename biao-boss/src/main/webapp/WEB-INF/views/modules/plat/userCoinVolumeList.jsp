<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币币资产管理</title>
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
		<li class="active"><a href="${ctx}/plat/userCoinVolume/">币币资产列表</a></li>
		<shiro:hasPermission name="plat:userCoinVolume:edit"><li><a href="${ctx}/plat/userCoinVolume/form">币币资产添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="userCoinVolume" action="${ctx}/plat/userCoinVolume/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>user_id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>币种代号：</label>
				<%--<form:select path="coinSymbol" class="input-medium" htmlEscape="false" maxlength="64">--%>
					<%--<form:option value="" label=""/>--%>
					<%--<form:options items="${coinList}" itemLabel="name" itemValue="name" htmlEscape="false" />--%>
				<%--</form:select>--%>
				<form:input path="coinSymbol" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>user_id</th>
				<th>邮箱</th>
				<th>手机</th>
				<th>币种代号</th>
				<th>币种数量</th>
				<th>冻结资产</th>
				<th>总资产</th>
				<th>创建日期</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:userCoinVolume:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userCoinVolume">
			<tr>
				<td><a href="${ctx}/plat/userCoinVolume/form?id=${userCoinVolume.id}">
						${userCoinVolume.userId}
				</a></td>
				<td>${userCoinVolume.mail}</td>
				<td>
					${userCoinVolume.mobile}
				</td>
				<td>
					${userCoinVolume.coinSymbol}
				</td>
				<td>
					<fmt:formatNumber value="${userCoinVolume.volume}" pattern="#0.00000000" />
				</td>
				<td>
					<fmt:formatNumber value="${userCoinVolume.lockVolume}" pattern="#0.00000000" />
				</td>
				<td>
					<fmt:formatNumber value="${userCoinVolume.totalVolume}" pattern="#0.00000000" />
				</td>
				<td>
					<fmt:formatDate value="${userCoinVolume.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${userCoinVolume.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:userCoinVolume:edit"><td>
    				<%--<a href="${ctx}/plat/userCoinVolume/form?id=${userCoinVolume.id}">修改</a>--%>
					<%--<a href="${ctx}/plat/userCoinVolume/delete?id=${userCoinVolume.id}" onclick="return confirmx('确认要删除该币币资产吗？', this.href)">删除</a>--%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>