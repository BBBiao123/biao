<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员推广明细管理</title>
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
		<li class="active"><a href="${ctx}/market/mkPromoteDetail/">会员推广明细列表</a></li>
		<shiro:hasPermission name="market:mkPromoteDetail:edit"><li><a href="${ctx}/market/mkPromoteDetail/form">会员推广明细添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkPromoteDetail" action="${ctx}/market/mkPromoteDetail/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键id</th>
				<th>分红规则ID</th>
				<th>奖励数量</th>
				<th>层级</th>
				<th>创建日期</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mkPromoteDetail:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkPromoteDetail">
			<tr>
				<td><a href="${ctx}/market/mkPromoteDetail/form?id=${mkPromoteDetail.id}">
					${mkPromoteDetail.id}
				</a></td>
				<td>
					${mkPromoteDetail.promoteId}
				</td>
				<td>
					${mkPromoteDetail.volume}
				</td>
				<td>
					${mkPromoteDetail.level}
				</td>
				<td>
					<fmt:formatDate value="${mkPromoteDetail.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkPromoteDetail.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkPromoteDetail:edit"><td>
    				<a href="${ctx}/market/mkPromoteDetail/form?id=${mkPromoteDetail.id}">修改</a>
					<a href="${ctx}/market/mkPromoteDetail/delete?id=${mkPromoteDetail.id}" onclick="return confirmx('确认要删除该会员推广明细吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>