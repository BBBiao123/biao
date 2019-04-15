<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分红规则管理管理</title>
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
		<li class="active"><a href="${ctx}/market/mkDividendManage/">分红规则管理列表</a></li>
		<shiro:hasPermission name="market:mkDividendManage:add"><li><a href="${ctx}/market/mkDividendManage/form">分红规则管理添加</a></li></shiro:hasPermission>
	</ul>
	<%--<form:form id="searchForm" modelAttribute="mkDividendManage" action="${ctx}/market/mkDividendManage/" method="post" class="breadcrumb form-search">--%>
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
				<th>分红规则</th>
				<th>状态</th>
				<th>说明</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mkDividendManage:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkDividendManage">
			<tr>
				<td><a href="${ctx}/market/mkDividendManage/form?id=${mkDividendManage.id}">
						${fns:getDictLabel(mkDividendManage.type, 'mkDividendManageService', '')}
				</a></td>
				<td>
					${fns:getDictLabel(mkDividendManage.status, 'mkRuleManageStatus', '')}
				</td>
				<td>
					${mkDividendManage.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkDividendManage.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkDividendManage.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkDividendManage.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkDividendManage.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="market:mkDividendManage:edit">
    					<a href="${ctx}/market/mkDividendManage/form?id=${mkDividendManage.id}">修改</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="market:mkDividendManage:delete">
						<a href="${ctx}/market/mkDividendManage/delete?id=${mkDividendManage.id}" onclick="return confirmx('确认要删除该分红规则管理吗？', this.href)">删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>