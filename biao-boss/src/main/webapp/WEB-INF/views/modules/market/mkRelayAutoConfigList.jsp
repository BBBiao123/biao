<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接力自动撞奖配置管理</title>
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
		<li class="active"><a href="${ctx}/market/mkRelayAutoConfig/">接力自动撞奖配置列表</a></li>
		<%--<shiro:hasPermission name="market:mkRelayAutoConfig:edit"><li><a href="${ctx}/market/mkRelayAutoConfig/form">接力自动撞奖配置添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="mkRelayAutoConfig" action="${ctx}/market/mkRelayAutoConfig/" method="post" class="breadcrumb form-search">
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
				<th>状态</th>
				<th>启动中奖次数</th>
				<th>说明</th>
				<th>创建日期</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mkRelayAutoConfig:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkRelayAutoConfig">
			<tr>
				<td><a href="${ctx}/market/mkRelayAutoConfig/form?id=${mkRelayAutoConfig.id}">
					${mkRelayAutoConfig.id}
				</a></td>
				<td>
					${fns:getDictLabel(mkRelayAutoConfig.status, 'mkRelayConfigStatus', '')}
				</td>
				<td>
					${mkRelayAutoConfig.startRewardNumber}
				</td>
				<td>
					${mkRelayAutoConfig.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkRelayAutoConfig.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkRelayAutoConfig.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkRelayAutoConfig:edit"><td>
    				<a href="${ctx}/market/mkRelayAutoConfig/form?id=${mkRelayAutoConfig.id}">修改</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>