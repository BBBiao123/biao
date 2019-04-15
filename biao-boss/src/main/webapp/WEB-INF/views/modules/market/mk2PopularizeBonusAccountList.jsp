<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>平台运营分红账户管理</title>
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
		<li class="active"><a href="${ctx}/market/mk2PopularizeBonusAccount/">平台运营分红账户列表</a></li>
		<shiro:hasPermission name="market:mk2PopularizeBonusAccount:edit"><li><a href="${ctx}/market/mk2PopularizeBonusAccount/form">平台运营分红账户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeBonusAccount" action="${ctx}/market/mk2PopularizeBonusAccount/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>账户类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mk2_bonus_account_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>账户类型</th>
				<th>用户ID</th>
				<th>邮箱</th>
				<th>mobile</th>
				<th>身份证</th>
				<th>真实姓名</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mk2PopularizeBonusAccount:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeBonusAccount">
			<tr>
				<td><a href="${ctx}/market/mk2PopularizeBonusAccount/form?id=${mk2PopularizeBonusAccount.id}">
					${fns:getDictLabel(mk2PopularizeBonusAccount.type, 'mk2_bonus_account_type', '')}
				</a></td>
				<td>
					${mk2PopularizeBonusAccount.userId}
				</td>
				<td>
					${mk2PopularizeBonusAccount.mail}
				</td>
				<td>
					${mk2PopularizeBonusAccount.mobile}
				</td>
				<td>
					${mk2PopularizeBonusAccount.idCard}
				</td>
				<td>
					${mk2PopularizeBonusAccount.realName}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeBonusAccount.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeBonusAccount.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mk2PopularizeBonusAccount:edit"><td>
    				<a href="${ctx}/market/mk2PopularizeBonusAccount/form?id=${mk2PopularizeBonusAccount.id}">修改</a>
					<a href="${ctx}/market/mk2PopularizeBonusAccount/delete?id=${mk2PopularizeBonusAccount.id}" onclick="return confirmx('确认要删除该平台运营分红账户吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>