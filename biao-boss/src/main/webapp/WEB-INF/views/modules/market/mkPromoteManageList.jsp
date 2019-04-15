<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员推广规则管理管理</title>
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
		<li class="active"><a href="${ctx}/market/mkPromoteManage/">会员推广规则管理列表</a></li>
		<shiro:hasPermission name="market:mkPromoteManage:add"><li><a href="${ctx}/market/mkPromoteManage/form">会员推广规则管理添加</a></li></shiro:hasPermission>
	</ul>
	<%--<form:form id="searchForm" modelAttribute="mkPromoteManage" action="${ctx}/market/mkPromoteManage/" method="post" class="breadcrumb form-search">--%>
		<%--<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>--%>
		<%--<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>--%>
		<%--<ul class="ul-form">--%>
			<%--<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>--%>
			<%--<li class="clearfix"></li>--%>
		<%--</ul>--%>
	<%--</form:form>--%>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>会员推广规则</th>
				<th>状态</th>
				<th>说明</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mkPromoteManage:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkPromoteManage">
			<tr>
				<td><a href="${ctx}/market/mkPromoteManage/form?id=${mkPromoteManage.id}">
					${fns:getDictLabel(mkPromoteManage.type, 'distributePromoteService', '')}
				</a></td>
				<td>
					${fns:getDictLabel(mkPromoteManage.status, 'mkRuleManageStatus', '')}
				</td>
				<td>
					${mkPromoteManage.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkPromoteManage.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkPromoteManage.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkPromoteManage.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkPromoteManage.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="market:mkPromoteManage:edit">
    					<a href="${ctx}/market/mkPromoteManage/form?id=${mkPromoteManage.id}">修改</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="market:mkPromoteManage:delete">
						<a href="${ctx}/market/mkPromoteManage/delete?id=${mkPromoteManage.id}" onclick="return confirmx('确认要删除该会员推广规则管理吗？', this.href)">删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>