<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>营销账户管理</title>
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
		<li class="active"><a href="${ctx}/market/mkDistributeAccount/">挖矿冻结账户列表</a></li>
		<shiro:hasPermission name="market:account:edit"><li><a href="${ctx}/market/mkDistributeAccount/form">挖矿冻结账户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkDistributeAccount" action="${ctx}/market/mkDistributeAccount/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<%--<li><label>账户类型：</label>--%>
				<%--<form:select path="type" class="input-medium">--%>
					<%--<form:option value="" label=""/>--%>
					<%--<form:options items="${fns:getDictList('mkAccountType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
				<%--</form:select>--%>
			<%--</li>--%>
			<li><label>账户名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="45" class="input-medium"/>
			</li>
			<li><label>手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="45" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<%--<th>主键id</th>--%>
				<%--<th>账户类型</th>--%>
				<th>账户名称</th>
				<th>状态</th>
				<th>用户名(真实姓名)</th>
				<th>邮箱</th>
				<th>手机</th>
				<th>身份证</th>
				<th>币种代号</th>
				<th>释放币种数量</th>
				<th>冻结资产</th>
				<th>创建日期</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:account:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkDistributeAccount">
			<tr>
				<%--<td><a href="${ctx}/market/mkDistributeAccount/form?id=${mkDistributeAccount.id}">--%>
					<%--${mkDistributeAccount.id}--%>
				<%--</a></td>--%>
				<%--<td>--%>
					<%--${fns:getDictLabel(mkDistributeAccount.type, 'mkAccountType', '')}--%>
				<%--</td>--%>
				<td>
					<a href="${ctx}/market/mkDistributeAccount/form?id=${mkDistributeAccount.id}">
							${mkDistributeAccount.name}
					</a>
				</td>
				<td>
					${fns:getDictLabel(mkDistributeAccount.status, 'distributeAccountStatus', '')}
				</td>
				<td>
					${mkDistributeAccount.realName}
				</td>
				<td>
					${mkDistributeAccount.mail}
				</td>
				<td>
					${mkDistributeAccount.mobile}
				</td>
				<td>
					${mkDistributeAccount.idCard}
				</td>
				<td>
					${mkDistributeAccount.coinSymbol}
				</td>
				<td>
					${mkDistributeAccount.releaseVolume}
				</td>
				<td>
					${mkDistributeAccount.lockVolume}
				</td>
				<td>
					<fmt:formatDate value="${mkDistributeAccount.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkDistributeAccount.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:account:edit"><td>
    				<a href="${ctx}/market/mkDistributeAccount/form?id=${mkDistributeAccount.id}">修改</a>
					<a href="${ctx}/market/mkDistributeAccount/delete?id=${mkDistributeAccount.id}" onclick="return confirmx('确认要删除该营销账户吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>